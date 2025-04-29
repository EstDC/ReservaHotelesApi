package com.pago;
import com.PageResponse;
import com.reservahoteles.application.dto.*;
import com.reservahoteles.application.port.in.*;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.EstadoPago;
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
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Operaciones relacionadas con los pagos")
@SecurityRequirement(name = "bearerAuth")
public class PagoController {

    private final RegistrarPagoUseCase        registrarPagoUc;
    private final ConsultarPagosUseCase       consultarPagosUc;
    private final ActualizarEstadoPagoUseCase actualizarEstadoPagoUc;

    /** POST /api/pagos → registra un nuevo pago */
    @Operation(
            summary     = "Registrar nuevo pago",
            description = "Registra un nuevo pago asociado a una reserva"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description  = "Pago registrado exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = RegistrarPagoResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<RegistrarPagoResponse> crear(
            @Valid @RequestBody RegistrarPagoCommand cmd
    ) {
        // inyectamos actor y rol desde el contexto de seguridad
        var authCmd = new RegistrarPagoCommand(
                cmd.idReserva(),
                cmd.idCliente(),
                cmd.monto(),
                cmd.fechaPago(),
                cmd.formaPago(),
                cmd.numeroCuenta(),
                cmd.titular(),
                cmd.expiracion(),
                cmd.otrosDetalles(),
                SecurityUtils.currentActorId(),
                SecurityUtils.currentActorRole()
        );
        var resp = registrarPagoUc.ejecutar(authCmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**
     * GET /api/pagos
     * → búsqueda y listado paginado; si idPago se pasa, devuelve solo ese pago.
     */
    @Operation(
            summary     = "Listar o buscar pagos",
            description = "Listado de pagos con filtros opcionales; si se proporciona idPago devuelve solo ese pago"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de pagos obtenido"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<PageResponse<PagoResponse>> listar(
            @RequestParam(required = false) Long idPago,
            @RequestParam(required = false) Long idReserva,
            @RequestParam(required = false) Long idCliente,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaInicio,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime fechaFin,
            @RequestParam(required = false) EstadoPago estadoPago,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        Long idActor  = SecurityUtils.currentActorId();
        Rol  rolActor = SecurityUtils.currentActorRole();
        var cmd = new ConsultarPagosCommand(
                idPago,
                idReserva,
                idCliente,
                fechaInicio,
                fechaFin,
                estadoPago,
                idActor,
                rolActor,
                page,
                size
        );
        var resp = consultarPagosUc.ejecutar(cmd);
        return ResponseEntity.ok(resp);
    }

    /**
     * PUT /api/pagos/{id}/estado
     * → actualiza el estado del pago según la forma de pago indicada.
     */
    @Operation(
            summary     = "Actualizar estado de pago",
            description = "Actualiza el estado del pago según la forma de pago indicada"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Estado de pago actualizado exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ActualizarEstadoPagoResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error al actualizar el estado del pago")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<ActualizarEstadoPagoResponse> actualizarEstado(
            @PathVariable("id") Long idPago,
            @Valid @RequestBody ActualizarEstadoPagoCommand body
    ) {
        // override any idPago in body with the path variable
        var cmd = new ActualizarEstadoPagoCommand(
                idPago,
                body.formaPago()
        );
        var resp = actualizarEstadoPagoUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }
}
//POST /api/pagos
//
//Creates a new payment via your existing RegistrarPagoCommand
//
//Returns 201 if "SUCCESS", 400 otherwise.
//
//GET /api/pagos
//
//Single unified “search” endpoint controlled by ConsultarPagosCommand
//
//Optional filters: idPago, idReserva, idCliente, fechaInicio, fechaFin, estadoPago
//
//Requires headers X-Actor-Id + X-Actor-Rol
//
//Returns a PageResponse<PagoResponse> with pagination metadata.
//
//PUT /api/pagos/{id}/estado
//
//Updates payment state based on the provided "formaPago" string (e.g. "TARJETA", "TRANSFERENCIA")
//
//Delegates to your ActualizarEstadoPagoService
//
//Returns 200 on success or 400 on failure.