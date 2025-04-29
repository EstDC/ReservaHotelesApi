package com.reservahoteles.infra.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "extras")
public class ExtraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_extra")
    private Long id;

    @Column(name = "nombre_extra", nullable = false)
    private String nombreExtra;

    @Column(name = "costo_adicional", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoAdicional;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "habitacion_extras",
            joinColumns = @JoinColumn(name = "id_extra"),
            inverseJoinColumns = @JoinColumn(name = "id_habitacion")
    )
    private Set<HabitacionEntity> habitaciones = new HashSet<>();
}