package com.reservahoteles.domain.entity;

import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.TipoHabitacion;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Habitacion {
    private Long id;
    private Long idHotel;
    private String numeroHabitacion;
    private TipoHabitacion tipo;
    private Long capacidad;
    private BigDecimal precioPorNoche;
    private EstadoHabitacion estado;
    private String descripcion;

    @Builder.Default
    private List<Extra> extras = new ArrayList<>();
}
