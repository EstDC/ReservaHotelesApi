package com.reservahoteles.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.TipoHabitacion;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Representa una revisión (auditoría) de una habitación,
 * tal y como la almacena Hibernate Envers.
 */
@Schema(name="RevisionHabitacionDTO", description="Revision habitacion dto")
public record RevisionHabitacionDTO(
        Long revisionNumber,         // nº de revisión (auto‐incremental)
        LocalDateTime revisionDate,  // fecha y hora de la revisión
        EstadoHabitacion estado,     // estado de la habitación en esta revisión
        TipoHabitacion tipo,                 // tipo de habitación (p.ej. “SUITE”)
        BigDecimal precioPorNoche        // precio por noche en esta revisión
) { }