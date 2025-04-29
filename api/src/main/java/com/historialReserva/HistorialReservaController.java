package com.historialReserva;
import com.PageResponse;
import com.reservahoteles.application.dto.ConsultarHistorialReservasCommand;
import com.reservahoteles.application.dto.ConsultarHistorialClienteCommand;
import com.reservahoteles.application.dto.ConsultarHistorialClienteResponse;
import com.reservahoteles.application.dto.ConsultarHistorialHabitacionCommand;
import com.reservahoteles.application.dto.HistorialReservaResponse;
import com.reservahoteles.application.dto.RevisionHabitacionDTO;
import com.reservahoteles.application.port.in.ConsultarHistorialReservasUseCase;
import com.reservahoteles.application.port.in.ConsultarHistorialClienteUseCase;
import com.reservahoteles.application.port.in.ConsultarHistorialHabitacionUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.EstadoReserva;
import com.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/historial")
@RequiredArgsConstructor
@Tag(name = "Historial", description = "Consultas de historial de reservas, clientes y habitaciones")
@SecurityRequirement(name = "bearerAuth")
public class HistorialReservaController {

    private final ConsultarHistorialReservasUseCase    historialReservasUc;
    private final ConsultarHistorialClienteUseCase     historialClienteUc;
    private final ConsultarHistorialHabitacionUseCase  historialHabitacionUc;

    /**
     * GET /api/historial/reservas
     */
    @Operation(
            summary     = "Consultar historial de reservas",
            description = "Devuelve el historial de reservas filtrado por reserva, cliente, habitación, fecha y estado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial de reservas obtenido"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/reservas")
    public ResponseEntity<PageResponse<HistorialReservaResponse>> reservas(
            @RequestParam(required = false) Long idReserva,
            @RequestParam(required = false) Long idCliente,
            @RequestParam(required = false) Long idHabitacion,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) EstadoReserva estadoReserva,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long idActor   = SecurityUtils.currentActorId();
        Rol  rolActor  = SecurityUtils.currentActorRole();

        var cmd = new ConsultarHistorialReservasCommand(
                idReserva,
                idCliente,
                idHabitacion,
                fechaInicio,
                fechaFin,
                estadoReserva,
                idActor,
                rolActor,
                page,
                size
        );
        return ResponseEntity.ok(historialReservasUc.ejecutar(cmd));
    }

    /**
     * GET /api/historial/clientes/{id}
     */
    @Operation(
            summary     = "Consultar historial de un cliente",
            description = "Devuelve el historial de reservas de un cliente entre fechas opcionales"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Historial de cliente obtenido",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ConsultarHistorialClienteResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/clientes/{id}")
    public ResponseEntity<ConsultarHistorialClienteResponse> cliente(
            @PathVariable("id") Long idCliente,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate fechaFin
    ) {
        Long idActor   = SecurityUtils.currentActorId();
        Rol  rolActor  = SecurityUtils.currentActorRole();

        var cmd = new ConsultarHistorialClienteCommand(
                idCliente,
                fechaInicio,
                fechaFin,
                idActor,
                rolActor
        );
        return ResponseEntity.ok(historialClienteUc.ejecutar(cmd));
    }

    /**
     * GET /api/historial/habitaciones/{id}
     */
    @Operation(
            summary     = "Consultar historial de habitación",
            description = "Devuelve el historial de revisiones de una habitación"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial de habitación obtenido"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/habitaciones/{id}")
    public ResponseEntity<PageResponse<RevisionHabitacionDTO>> habitacion(
            @PathVariable("id") Long idHabitacion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var cmd = new ConsultarHistorialHabitacionCommand(
                idHabitacion,
                page,
                size
        );
        return ResponseEntity.ok(historialHabitacionUc.ejecutar(cmd));
    }
}