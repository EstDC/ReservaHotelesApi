package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.RegistrarExtraCommand;
import com.reservahoteles.application.dto.RegistrarExtraResponse;
import com.reservahoteles.application.port.in.RegistrarExtraUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Extra;
import com.reservahoteles.domain.port.out.ExtraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrarExtraService implements RegistrarExtraUseCase {
    private final ExtraRepository repo;

    @Override
    public RegistrarExtraResponse ejecutar(RegistrarExtraCommand cmd) {
        if (cmd.rolActor() != Rol.ADMINISTRADOR)
            return new RegistrarExtraResponse(null, "FAILURE", "Sólo ADMIN puede crear extras");
        Extra e = new Extra(null, cmd.nombre(), cmd.costoAdicional());
        e = repo.save(e);
        return new RegistrarExtraResponse(e.getId(), "SUCCESS", "Extra creado");
    }
}
