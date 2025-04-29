package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.EliminarExtraCommand;
import com.reservahoteles.application.dto.EliminarExtraResponse;
import com.reservahoteles.application.port.in.EliminarExtraUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Extra;
import com.reservahoteles.domain.port.out.ExtraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class EliminarExtraService implements EliminarExtraUseCase {

    private final ExtraRepository extraRepo;

    public EliminarExtraService(ExtraRepository extraRepo) {
        this.extraRepo = extraRepo;
    }

    @Override
    @Transactional
    public EliminarExtraResponse ejecutar(EliminarExtraCommand cmd) {
        // 1) Recuperar el extra
        Extra extra = extraRepo.findById(cmd.idExtra())
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe el extra con id = " + cmd.idExtra()
                ));

        // 2) Solo ADMIN puede eliminar extras
        boolean esAdmin = cmd.rolActor() == Rol.ADMINISTRADOR;
        if (!esAdmin) {
            return new EliminarExtraResponse(
                    cmd.idExtra(),
                    "FAILURE",
                    "No tienes permisos para eliminar este extra"
            );
        }

        // 3) Hard-delete
        extraRepo.eliminar(cmd.idExtra());

        // 4) Devolver respuesta
        return new EliminarExtraResponse(
                cmd.idExtra(),
                "SUCCESS",
                "Extra eliminado con éxito"
        );
    }
}
