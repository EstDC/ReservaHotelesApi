package com.reservahoteles.application.service;
import com.reservahoteles.application.dto.EliminarCredencialesCommand;
import com.reservahoteles.application.dto.EliminarCredencialesResponse;
import com.reservahoteles.application.port.in.EliminarCredencialesUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Credencial;
import com.reservahoteles.domain.port.out.CredencialRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class EliminarCredencialesService implements EliminarCredencialesUseCase {

    private final CredencialRepository credRepo;

    public EliminarCredencialesService(CredencialRepository credRepo) {
        this.credRepo = credRepo;
    }

    @Override
    public EliminarCredencialesResponse ejecutar(EliminarCredencialesCommand cmd) {
        // 1) Recuperar la credencial
        Credencial cred = credRepo.findById(cmd.idCredencial())
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe la credencial con id = " + cmd.idCredencial()
                ));

        boolean esAdmin  = cmd.rolActor() == Rol.ADMINISTRADOR;
        boolean esPropio = cred.getIdCliente().equals(cmd.idActor());

        // 2) Permisos
        if (!esAdmin && !esPropio) {
            return new EliminarCredencialesResponse(
                    cmd.idCredencial(), "FAILURE",
                    "No tienes permisos para eliminar estas credenciales"
            );
        }

        // 3) Si es el propio usuario → soft‑delete (desactivar)
        if (esPropio && !esAdmin) {
            if (!cred.isActivo()) {
                return new EliminarCredencialesResponse(
                        cmd.idCredencial(), "IGNORED",
                        "Tus credenciales ya estaban desactivadas"
                );
            }
            cred.setActivo(false);
            cred.setUltimaActualizacion(LocalDateTime.now());
            credRepo.save(cred);
            return new EliminarCredencialesResponse(
                    cmd.idCredencial(), "SUCCESS",
                    "Tus credenciales han sido desactivadas"
            );
        }

        // 4) Si es admin → hard‑delete
        credRepo.eliminar(cmd.idCredencial());
        return new EliminarCredencialesResponse(
                cmd.idCredencial(), "SUCCESS",
                "Credenciales eliminadas por administrador"
        );
    }
}