package com.reservahoteles.application.service;

import com.PageResponse;
import com.reservahoteles.application.dto.BuscarServiciosCommand;
import com.reservahoteles.application.dto.RegistrarServicioCommand;
import com.reservahoteles.application.dto.RegistrarServicioResponse;
import com.reservahoteles.application.dto.ServicioResponse;
import com.reservahoteles.application.dto.ActualizarServicioCommand;
import com.reservahoteles.application.dto.ActualizarServicioResponse;
import com.reservahoteles.application.dto.EliminarServicioCommand;
import com.reservahoteles.application.dto.EliminarServicioResponse;
import com.reservahoteles.application.port.in.*;
import com.reservahoteles.domain.entity.Servicio;
import com.reservahoteles.domain.port.out.ServicioRepository;
import com.reservahoteles.common.enums.Rol;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicioService
        implements RegistrarServicioUseCase,
        BuscarServiciosUseCase,
        ActualizarServicioUseCase,
        EliminarServicioUseCase {

    private final ServicioRepository repo;

    @Override
    public RegistrarServicioResponse ejecutar(RegistrarServicioCommand cmd) {
        if (cmd.rolActor() != Rol.ADMINISTRADOR) {
            return new RegistrarServicioResponse(null, "FAILURE", "Solo ADMIN puede crear servicios");
        }
        Servicio s = new Servicio();
        s.setNombre(cmd.nombre());
        s.setDescripcion(cmd.descripcion());
        Servicio saved = repo.save(s);
        return new RegistrarServicioResponse(saved.getId(), "SUCCESS", "Servicio creado");
    }

    @Override
    public PageResponse<ServicioResponse> ejecutar(BuscarServiciosCommand cmd) {
        if (cmd.idActor()==null||cmd.rolActor()==null)
            throw new IllegalArgumentException("idActor y rolActor obligatorios");
        Pageable page = PageRequest.of(cmd.page(), cmd.size(), Sort.by("id").ascending());
        if (cmd.idServicio()!=null) {
            Servicio s = repo.findById(cmd.idServicio())
                    .orElseThrow(() -> new NoSuchElementException("No existe id=" + cmd.idServicio()));
            var dto = new ServicioResponse(s.getId(),s.getNombre(),s.getDescripcion());
            return new PageResponse<>(List.of(dto),0,1,1,1);
        }
        Page<Servicio> p = repo.buscarPorNombre(cmd.nombreParcial(), page);
        List<ServicioResponse> items = p.getContent().stream()
                .map(s->new ServicioResponse(s.getId(),s.getNombre(),s.getDescripcion()))
                .collect(Collectors.toList());
        return new PageResponse<>(
                items,
                p.getNumber(), p.getSize(),
                p.getTotalElements(), p.getTotalPages());
    }

    @Override
    public ActualizarServicioResponse ejecutar(ActualizarServicioCommand cmd) {
        Servicio s = repo.findById(cmd.idServicio())
                .orElseThrow(() -> new NoSuchElementException("No existe id=" + cmd.idServicio()));
        if (cmd.rolActor()!=Rol.ADMINISTRADOR) {
            return new ActualizarServicioResponse(s.getId(),"FAILURE","Solo ADMIN");
        }
        s.setNombre(cmd.nombre());
        s.setDescripcion(cmd.descripcion());
        repo.save(s);
        return new ActualizarServicioResponse(s.getId(),"SUCCESS","Servicio actualizado");
    }

    @Override
    public EliminarServicioResponse ejecutar(EliminarServicioCommand cmd) {
        if (cmd.rolActor()!=Rol.ADMINISTRADOR) {
            return new EliminarServicioResponse(cmd.idServicio(),"FAILURE","Solo ADMIN");
        }
        repo.eliminar(cmd.idServicio());
        return new EliminarServicioResponse(cmd.idServicio(),"SUCCESS","Servicio eliminado");
    }
}