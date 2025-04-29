package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.EliminarClienteCommand;
import com.reservahoteles.application.dto.EliminarClienteResponse;
import com.reservahoteles.application.port.in.EliminarClienteUseCase;
import com.reservahoteles.common.enums.EstadoReserva;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Cliente;
import com.reservahoteles.domain.port.out.ClienteRepository;
import com.reservahoteles.domain.port.out.CredencialRepository;
import com.reservahoteles.domain.port.out.ReservaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class EliminarClienteService implements EliminarClienteUseCase {

    private final ClienteRepository clienteRepo;
    private final ReservaRepository reservaRepo;
    private final CredencialRepository credRepo;

    public EliminarClienteService(ClienteRepository clienteRepo,
                                  ReservaRepository reservaRepo,
                                  CredencialRepository credRepo) {
        this.clienteRepo = clienteRepo;
        this.reservaRepo = reservaRepo;
        this.credRepo    = credRepo;
    }

    @Override
    public EliminarClienteResponse ejecutar(EliminarClienteCommand cmd) {
        boolean esAdmin = cmd.rolActor() == Rol.ADMINISTRADOR;
        boolean esMismo = Objects.equals(cmd.idCliente(), cmd.idActor());

        if (!esAdmin && !esMismo) {
            return new EliminarClienteResponse(
                    cmd.idCliente(), "FAILURE",
                    "No tiene permisos para eliminar a este cliente"
            );
        }

        // 1) Verifico que exista (incluso si está inactivo, para admin sigue existiendo)
        Cliente cliente = clienteRepo.findById(cmd.idCliente())
                .orElseThrow(() -> new NoSuchElementException(
                        "Cliente no encontrado (id=" + cmd.idCliente() + ")"
                ));

        // 2) Compruebo que no tenga reservas activas
        boolean tieneActivas = reservaRepo.buscarPorCriterios(
                        cmd.idCliente(), null, null, null
                ).stream()
                .anyMatch(r ->
                        r.getEstadoReserva() == EstadoReserva.PENDIENTE ||
                                r.getEstadoReserva() == EstadoReserva.CONFIRMADA
                );
        if (tieneActivas) {
            return new EliminarClienteResponse(
                    cmd.idCliente(), "FAILURE",
                    "No se puede eliminar: tiene reservas activas"
            );
        }

        // 3) Camino admin: borrado físico
        if (esAdmin) {
            // también eliminamos credenciales asociadas si quieres
            credRepo.findByClienteId(cmd.idCliente())
                    .ifPresent(c -> credRepo.eliminar(c.getId()));
            clienteRepo.eliminar(cmd.idCliente());

            return new EliminarClienteResponse(
                    cmd.idCliente(), "SUCCESS",
                    "Cliente eliminado físicamente"
            );
        }

        // 4) Camino auto‑baja (cliente): marcamos inactivo
        cliente.setActivo(false);
        clienteRepo.save(cliente);
        return new EliminarClienteResponse(
                cmd.idCliente(), "SUCCESS",
                "Te has dado de baja correctamente"
        );
    }
}