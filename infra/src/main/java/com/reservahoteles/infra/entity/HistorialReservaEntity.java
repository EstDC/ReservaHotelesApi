package com.reservahoteles.infra.entity;

import com.reservahoteles.common.enums.EstadoReserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "historial_reservas")
public class HistorialReservaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long id;

    @Column(name = "id_reserva", nullable = false)
    private Long idReserva;

    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    @Column(name = "id_habitacion", nullable = false)
    private Long idHabitacion;

    @Column(name = "fecha_entrada", nullable = false)
    private LocalDate fechaEntrada;

    @Column(name = "fecha_salida", nullable = false)
    private LocalDate fechaSalida;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_reserva", nullable = false)
    private EstadoReserva estadoReserva;  // Se usa el enum de common

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Column(name = "fecha_archivo", nullable = false)
    private LocalDateTime fechaArchivo;

    @ManyToMany
    @JoinTable(
            name = "historial_reserva_extra",
            joinColumns        = @JoinColumn(name = "id_historial"),
            inverseJoinColumns = @JoinColumn(name = "id_extra")
    )
    @Builder.Default
    private List<ExtraEntity> extras = new ArrayList<>();
}