package com.reservahoteles.domain.event;

import com.reservahoteles.common.enums.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationChangeEvent {
    private Long reservaId;
    private EstadoReserva nuevoEstado;
    private LocalDateTime timestamp;
}