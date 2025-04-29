package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.ConsultarHistorialClienteCommand;
import com.reservahoteles.application.dto.ConsultarHistorialClienteResponse;
import com.reservahoteles.application.dto.HistorialReservaResponse;
import com.reservahoteles.application.port.in.ConsultarHistorialClienteUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.domain.entity.HistorialReserva;
import com.reservahoteles.domain.port.out.ClienteRepository;
import com.reservahoteles.domain.port.out.HistorialReservaRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ConsultarHistorialClienteService implements ConsultarHistorialClienteUseCase {

    private final HistorialReservaRepository historialRepo;
    private final ClienteRepository clienteRepo;

    public ConsultarHistorialClienteService(HistorialReservaRepository historialRepo,
                                            ClienteRepository clienteRepo) {
        this.historialRepo = historialRepo;
        this.clienteRepo   = clienteRepo;
    }

    @Override
    public ConsultarHistorialClienteResponse ejecutar(ConsultarHistorialClienteCommand cmd) {
        // 1) idCliente obligatorio
        if (cmd.idCliente() == null) {
            throw new IllegalArgumentException("El id de cliente es obligatorio");
        }

        // 2) Permisos: sólo admin o el propio cliente
        boolean esAdmin  = cmd.rolActor() == Rol.ADMINISTRADOR;
        boolean esPropia = Objects.equals(cmd.idCliente(), cmd.idActor());
        if (!esAdmin && !esPropia) {
            throw new IllegalStateException("No tiene permiso para consultar este historial");
        }

        // 3) Validar existencia de cliente
        clienteRepo.findById(cmd.idCliente())
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró el cliente con id = " + cmd.idCliente()
                ));

        // 4) Validar rango de fechas (si vienen ambas)
        if (cmd.fechaInicio() != null && cmd.fechaFin() != null
                && cmd.fechaInicio().isAfter(cmd.fechaFin())) {
            throw new IllegalArgumentException(
                    "Rango de fechas inválido: fechaInicio > fechaFin");
        }

        // 5) Traer todo el historial del cliente
        List<HistorialReserva> todos = historialRepo.findByClienteId(cmd.idCliente());

        // 6) Filtrar por rango de fecha de archivo (si se indicó)
        List<HistorialReserva> filtrados = todos.stream()
                .filter(hr -> {
                    LocalDate fechaArchivo = hr.getFechaArchivo().toLocalDate();
                    boolean desdeOk = cmd.fechaInicio() == null ||
                            !fechaArchivo.isBefore(cmd.fechaInicio());
                    boolean hastaOk = cmd.fechaFin()   == null ||
                            !fechaArchivo.isAfter(cmd.fechaFin());
                    return desdeOk && hastaOk;
                })
                .collect(Collectors.toList());

        // 7) Mapear a DTO de salida
        List<HistorialReservaResponse> respuesta = filtrados.stream()
                .map(hr -> new HistorialReservaResponse(
                        hr.getId(),
                        hr.getIdReserva(),
                        hr.getIdCliente(),
                        hr.getIdHabitacion(),
                        hr.getFechaEntrada(),
                        hr.getFechaSalida(),
                        hr.getEstadoReserva(),
                        hr.getFechaReserva(),
                        hr.getTotal(),
                        hr.getFechaArchivo()
                ))
                .collect(Collectors.toList());

        return new ConsultarHistorialClienteResponse(
                cmd.idCliente(),
                "SUCCESS",
                "Historial consultado correctamente",
                respuesta
        );
    }
}