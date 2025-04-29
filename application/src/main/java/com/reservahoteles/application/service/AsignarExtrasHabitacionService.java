package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.AsignarExtrasHabitacionCommand;
import com.reservahoteles.application.dto.AsignarExtrasHabitacionResponse;
import com.reservahoteles.application.port.in.AsignarExtrasHabitacionUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Extra;
import com.reservahoteles.domain.entity.Habitacion;
import com.reservahoteles.domain.port.out.ExtraRepository;
import com.reservahoteles.domain.port.out.HabitacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AsignarExtrasHabitacionService implements AsignarExtrasHabitacionUseCase {

    private final HabitacionRepository habitacionRepo;
    private final ExtraRepository extraRepo;


    public AsignarExtrasHabitacionService(HabitacionRepository habitacionRepo,
                                          ExtraRepository extraRepo) {
        this.habitacionRepo = habitacionRepo;
        this.extraRepo      = extraRepo;
    }

    @Override
    public AsignarExtrasHabitacionResponse ejecutar(AsignarExtrasHabitacionCommand cmd) {

        /* 1 ── Seguridad */
        boolean esAdmin  = cmd.rolActor() == Rol.ADMINISTRADOR;
        if (!esAdmin) {
            return fail(cmd, "Solo el administrador puede gestionar los extras de las habitaciones");
        }

        /* 2 ── Habitación existente */
        Habitacion hab = habitacionRepo.findById(cmd.idHabitacion())
                .orElseThrow(() -> new NoSuchElementException(
                        "Habitación no encontrada (id=" + cmd.idHabitacion() + ')'));

        List<Extra> listaActual = hab.getExtras() == null
                ? new ArrayList<>()
                : new ArrayList<>(hab.getExtras());
        hab.setExtras(listaActual);

        /* 3 ── Validar extras recibidos */
        List<Extra> extrasSolicitados = extraRepo.findByIds(cmd.idsExtras());
        if (extrasSolicitados.size() != cmd.idsExtras().size()) {
            return fail(cmd, "Alguno de los extras indicados no existe");
        }

        /* 4 ── Aplicar el modo de asignación */
        switch (cmd.modo()) {

            case AGREGAR -> {
                // Agrega solo los que aún no están
                Set<Long> actuales = hab.getExtras().stream()
                        .map(Extra::getId).collect(Collectors.toSet());
                extrasSolicitados.stream()
                        .filter(e -> !actuales.contains(e.getId()))
                        .forEach(hab.getExtras()::add);
            }

            // Elimina únicamente los indicados
            case QUITAR -> hab.getExtras().removeIf(e -> cmd.idsExtras().contains(e.getId()));

            // Sustituye completamente
            case REEMPLAZAR -> hab.setExtras(new ArrayList<>(extrasSolicitados));
        }

        /* 5 ── Persistir */
        habitacionRepo.save(hab);

        return new AsignarExtrasHabitacionResponse(
                hab.getId(), "SUCCESS", "Extras actualizados correctamente (" + cmd.modo() + ')');
    }

    // helper -----------------------------------------------------------------
    private AsignarExtrasHabitacionResponse fail(AsignarExtrasHabitacionCommand cmd, String msg) {
        return new AsignarExtrasHabitacionResponse(cmd.idHabitacion(), "FAILURE", msg);
    }
}