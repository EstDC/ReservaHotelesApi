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
public class Reserva {
    private Long id;
    private Long idCliente;      // Identificador del cliente que realiza la reserva
    private Long idHabitacion;   // Identificador de la habitación reservada
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private EstadoReserva estadoReserva;// Ejemplo: confirmada, cancelada, pendiente
    private LocalDateTime fechaReserva;
    private Double total;
    private LocalDateTime ultimaActualizacion;
    @Builder.Default               // lista vacía por defecto
    private List<Extra> extras = new ArrayList<>();
}
