package com.reservahoteles.infra.entity;

import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.TipoHabitacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "habitaciones")
public class HabitacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_habitacion")
    private Long id;

    // Almacena la referencia al hotel (FK), en este modelo se guarda el id
    @Column(name = "id_hotel", nullable = false)
    private Long idHotel;

    @Column(name = "numero_habitacion", nullable = false)
    private String numeroHabitacion;

    @Column(name = "tipo", nullable = false)
    private TipoHabitacion tipo;

    @Column(name = "capacidad", nullable = false)
    private Long capacidad;

    @Column(name = "precio_por_noche", nullable = false, precision = 8, scale = 2)
    private BigDecimal precioPorNoche;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoHabitacion estado;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "habitacion_extras",
            joinColumns = @JoinColumn(name = "id_habitacion"),
            inverseJoinColumns = @JoinColumn(name = "id_extra"))
    private Set<ExtraEntity> extras;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hotel", insertable = false, updatable = false)
    private HotelEntity hotelEntity;


}