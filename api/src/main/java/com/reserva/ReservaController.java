package com.reserva;
import com.PageResponse;
import com.reservahoteles.application.dto.*;
import com.reservahoteles.application.port.in.*;
import com.reservahoteles.common.enums.*;
import com.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Operaciones de creación, consulta y gestión de reservas")
@SecurityRequirement(name = "bearerAuth")
public class ReservaController {

    private final CrearReservaUseCase              crearUc;
    private final BuscarReservasUseCase            buscarUc;
    private final ActualizarReservaUseCase         actualizarUc;
    private final CancelarReservaUseCase           cancelarUc;
    private final ArchivarReservaUseCase           archivarUc;
    private final EliminarReservaUseCase           eliminarUc;
    private final NotificarCambiosReservasUseCase  notificarUc;

    /** POST /api/reservas → crea una nueva reserva */
    @Operation(
            summary     = "Crear nueva reserva",
            description = "Crea una reserva para el cliente autenticado"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description  = "Reserva creada exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = CrearReservaResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<CrearReservaResponse> crear(
            @Valid @RequestBody CrearReservaCommand cmd
    ) {
        var authCmd = new CrearReservaCommand(
                cmd.idCliente(),
                cmd.idHabitacion(),
                cmd.fechaEntrada(),
                cmd.fechaSalida(),
                cmd.idsExtras(),
                SecurityUtils.currentActorId(),
                SecurityUtils.currentActorRole()
        );
        var resp = crearUc.ejecutar(authCmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**
     * GET /api/reservas
     * → búsqueda y listado paginado (y lookup por idReserva cuando se suministra)
     */
    @Operation(
            summary     = "Listar o buscar reservas",
            description = "Búsqueda y listado paginado de reservas con filtros opcionales"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reservas obtenidas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<PageResponse<ReservaResponse>> buscar(
            @RequestParam(required = false) Long idReserva,
            @RequestParam(required = false) Long idCliente,
            @RequestParam(required = false) Long idHotel,
            @RequestParam(required = false) Long idHabitacion,
            @RequestParam(required = false) EstadoReserva estadoReserva,
            @RequestParam(required = false) EstadoHabitacion estadoHabitacion,
            @RequestParam(required = false) TipoHabitacion tipoHabitacion,
            @RequestParam(required = false) EstadoPago estadoPago,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        var cmd = new BuscarReservasCommand(
                idReserva, idCliente, idHotel, idHabitacion,
                estadoReserva, estadoHabitacion, tipoHabitacion, estadoPago,
                fechaInicio, fechaFin,
                SecurityUtils.currentActorId(),
                SecurityUtils.currentActorRole(),
                page, size
        );
        var resp = buscarUc.ejecutar(cmd);
        return ResponseEntity.ok(resp);
    }

    /**
     * GET /api/reservas/{id}
     * → shortcut to buscar by idReserva, returns a singleton page
     */
    @Operation(
            summary     = "Detalle de reserva",
            description = "Obtiene una reserva por su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PageResponse<ReservaResponse>> detalle(
            @PathVariable("id") Long idReserva
    ) {
        var cmd = new BuscarReservasCommand(
                idReserva,
                null, null, null,
                null, null, null, null,
                null, null,
                SecurityUtils.currentActorId(),
                SecurityUtils.currentActorRole(),
                0, 1
        );
        var resp = buscarUc.ejecutar(cmd);
        return ResponseEntity.ok(resp);
    }

    /**
     * PUT /api/reservas/{id}
     * → actualiza fechas/estado/extras según rol (admin vs cliente)
     */
    @Operation(
            summary     = "Actualizar reserva",
            description = "Actualiza fechas, habitación, estado o extras de una reserva"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Reserva actualizada exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ActualizarReservaResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ActualizarReservaResponse> actualizar(
            @PathVariable("id") Long idReserva,
            @Valid @RequestBody ActualizarReservaCommand body
    ) {
        var cmd = new ActualizarReservaCommand(
                idReserva,
                SecurityUtils.currentActorId(),
                SecurityUtils.currentActorRole(),
                body.fechaEntrada(),
                body.fechaSalida(),
                body.idHabitacion(),
                body.estadoReserva(),
                body.idsExtras()
        );
        var resp = actualizarUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /** PUT /api/reservas/{id}/cancelar → cancela una reserva confirmada */
    @Operation(
            summary     = "Cancelar reserva",
            description = "Cancela una reserva confirmada"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Reserva cancelada exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = CancelarReservaResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "No se pudo cancelar la reserva")
    })
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<CancelarReservaResponse> cancelar(
            @PathVariable("id") Long idReserva
    ) {
        var cmd  = new CancelarReservaCommand(
                idReserva,
                SecurityUtils.currentActorId(),
                SecurityUtils.currentActorRole()
        );
        var resp = cancelarUc.ejecutar(cmd);
        HttpStatus status = resp.nuevoEstadoReserva() == EstadoReserva.CANCELADA
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /** PUT /api/reservas/{id}/archivar → archiva una reserva finalizada o cancelada (solo ADMIN) */
    @Operation(
            summary     = "Archivar reserva",
            description = "Archiva una reserva finalizada o cancelada (solo ADMIN)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Reserva archivada exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ArchivarReservaResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "No se pudo archivar la reserva")
    })
    @PutMapping("/{id}/archivar")
    public ResponseEntity<ArchivarReservaResponse> archivar(
            @PathVariable("id") Long idReserva
    ) {
        var cmd  = new ArchivarReservaCommand(
                idReserva,
                SecurityUtils.currentActorId(),
                SecurityUtils.currentActorRole()
        );
        var resp = archivarUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /** DELETE /api/reservas/{id} → elimina una reserva (admin o propio cliente) */
    @Operation(
            summary     = "Eliminar reserva",
            description = "Elimina una reserva (solo ADMIN o propietario)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Reserva eliminada exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = EliminarReservaResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "No se pudo eliminar la reserva")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<EliminarReservaResponse> eliminar(
            @PathVariable("id") Long idReserva
    ) {
        var cmd  = new EliminarReservaCommand(
                idReserva,
                SecurityUtils.currentActorId(),
                SecurityUtils.currentActorRole()
        );
        var resp = eliminarUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**
     * POST /api/reservas/{id}/notificar
     * → dispara manualmente un evento de cambio de reserva (SSE/WebSocket, etc.)
     */
    @Operation(
            summary     = "Notificar cambios de reserva",
            description = "Dispara un evento de cambio de reserva (SSE/WebSocket, etc.)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Notificación de cambio enviada exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = NotificarCambiosReservasResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error al notificar cambios")
    })
    @PostMapping("/{id}/notificar")
    public ResponseEntity<NotificarCambiosReservasResponse> notificarCambio(
            @PathVariable("id") Long idReserva
    ) {
        var cmd  = new NotificarCambiosReservasCommand(
                idReserva,
                SecurityUtils.currentActorRole(),
                SecurityUtils.currentActorId()
        );
        var resp = notificarUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }
}
//POST /api/reservas → create new reservation
//
//GET /api/reservas → advanced search/list with all filters + pagination
//
//GET /api/reservas/{id} → shortcut to fetch a single reservation by idReserva
//
//PUT /api/reservas/{id} → update dates, room, state or extras (admin vs client rules apply)
//
//PUT /api/reservas/{id}/cancelar → client‐only cancellation with its own rules
//
//PUT /api/reservas/{id}/archivar → admin‐only archiving into history
//
//DELETE /api/reservas/{id} → hard/soft delete (admin or owner)
//
//POST /api/reservas/{id}/notificar → manually fire a real‑time ReservationChangeEvent