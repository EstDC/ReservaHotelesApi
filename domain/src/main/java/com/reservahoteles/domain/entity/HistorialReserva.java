package com.reservahoteles.domain.entity;

import com.reservahoteles.common.enums.EstadoReserva;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialReserva {
    private Long id;
    private Long idReserva;
    private Long idCliente;
    private Long idHabitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private EstadoReserva estadoReserva;
    private LocalDateTime fechaReserva;
    private Double total;
    private LocalDateTime fechaArchivo; // Fecha en la que se archivó la reserva
    @Builder.Default
    private List<Extra> extras = new ArrayList<>();
}
