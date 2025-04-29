package com.clienteBancario;
import com.PageResponse;
import com.reservahoteles.application.dto.*;
import com.reservahoteles.application.port.in.*;
import com.reservahoteles.common.enums.Rol;
import com.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes-bancarios")
@RequiredArgsConstructor
@Tag(name = "ClientesBancarios", description = "Operaciones relacionadas con formas de pago bancario de clientes")
@SecurityRequirement(name = "bearerAuth")
public class ClienteBancarioController {

    private final RegistrarClienteBancarioUseCase registrarUc;
    private final ActualizarClienteBancarioUseCase actualizarUc;
    private final EliminarClienteBancarioUseCase eliminarUc;
    private final BuscarClienteBancarioUseCase buscarUc;

    /** POST→ crea una nueva forma de pago de cliente bancario */
    @Operation(
            summary     = "Crear forma de pago bancario",
            description = "Crea una nueva forma de pago para el cliente autenticado"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description  = "Forma de pago creada exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = RegistrarClienteBancarioResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<RegistrarClienteBancarioResponse> crear(
            @Valid @RequestBody RegistrarClienteBancarioCommand cmd
    ) {
        var resp = registrarUc.ejecutar(cmd);
        return "SUCCESS".equals(resp.status())
                ? ResponseEntity.status(HttpStatus.CREATED).body(resp)
                : ResponseEntity.badRequest().body(resp);
    }

    /** PUT /{id} → actualiza los datos de una forma de pago existente */
    @Operation(
            summary     = "Actualizar forma de pago bancario",
            description = "Actualiza los datos de una forma de pago existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Forma de pago actualizada exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ActualizarClienteBancarioResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ActualizarClienteBancarioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarClienteBancarioCommand body
    ) {
        // construyo el comando igual, usando los datos del body
        var cmd = new ActualizarClienteBancarioCommand(
                id,                     // idFormaPago
                body.tipoPago(),        // tipoPago
                body.numeroCuenta(),    // numeroCuenta
                body.titular(),         // titular
                body.expiracion(),      // expiracion
                body.otrosDetalles(),   // otrosDetalles
                body.idActor(),         // idActor
                body.rolActor()         // rolActor
        );
        var resp = actualizarUc.ejecutar(cmd);
        var status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /** DELETE /{id} → desactiva (o elimina) una forma de pago */
    @DeleteMapping("/{id}")
    public ResponseEntity<EliminarClienteBancarioResponse> eliminar(
            @PathVariable Long id
    ) {
        Long actor  = SecurityUtils.currentActorId();
        Rol  rol     = SecurityUtils.currentActorRole();
        var cmd     = new EliminarClienteBancarioCommand(id, actor, rol);
        var resp    = eliminarUc.ejecutar(cmd);
        var status  = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**
     * GET /api/clientes-bancarios
     * ‑ Si vienen idFormaPago → devuelve ese registro (autorización incluida)
     * ‑ Si no, lista todas las formas (paginado y permisos)
     */
    @Operation(
            summary     = "Eliminar forma de pago bancario",
            description = "Desactiva o elimina una forma de pago existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Forma de pago eliminada exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = EliminarClienteBancarioResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error al eliminar la forma de pago")
    })
    @GetMapping
    public ResponseEntity<PageResponse<ClienteBancarioResponse>> buscar(
            @RequestParam(required = false) Long idFormaPago,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long actor = SecurityUtils.currentActorId();
        Rol  rol    = SecurityUtils.currentActorRole();
        var cmd    = new BuscarClienteBancarioCommand(
                idFormaPago,
                actor,
                rol,
                page,
                size
        );
        return ResponseEntity.ok(buscarUc.ejecutar(cmd));
    }
}