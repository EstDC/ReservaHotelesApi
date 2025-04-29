package com.extra;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints para todo lo relativo a Extras:
 *  • Catálogo CRUD
 *  • Asignación de extras a habitación
 *  • Solicitud de extras en reserva
 */
@RestController
@RequestMapping("/api/extras")
@RequiredArgsConstructor
@Tag(name = "Extras", description = "Operaciones relacionadas con extras")
@SecurityRequirement(name = "bearerAuth")
public class ExtraController {

    // ── Catálogo CRUD ───────────────────────────
    private final RegistrarExtraUseCase         registrarExtraUc;
    private final BuscarExtrasUseCase           buscarExtrasUc;
    private final EliminarExtraUseCase          eliminarExtraUc;

    // ── Asignar / Solicitar ────────────────────
    private final AsignarExtrasHabitacionUseCase asignarExtrasHabitacionUc;
    private final SolicitarExtrasReservaUseCase  solicitarExtrasReservaUc;

    /** POST /api/extras  → crea un nuevo extra */
    @Operation(
            summary     = "Crear un nuevo extra",
            description = "Crea un nuevo extra en el catálogo"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description  = "Extra creado exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = RegistrarExtraResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<RegistrarExtraResponse> crearExtra(
            @Valid @RequestBody RegistrarExtraCommand cmd
    ) {
        var resp = registrarExtraUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**
     * GET /api/extras
     *  • Si llega `?idExtra=123`, devuelve solo ese extra (paginado 1×1)
     *  • Si no llega `idExtra`, devuelve listado paginado con filtro por nombre
     */
    @Operation(
            summary     = "Listar o buscar extras",
            description = "Si se proporciona idExtra devuelve ese registro; si no, lista todos con paginación y filtro por nombre"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de extras obtenido"),
            @ApiResponse(responseCode = "400", description = "Error en los parámetros de búsqueda"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<PageResponse<ExtraResponse>> buscarExtras(
            @RequestParam(required = false) Long idExtra,
            @RequestParam(required = false) Long idHabitacion,
            @RequestParam(required = false) String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long actor   = SecurityUtils.currentActorId();
        var  rolActor = SecurityUtils.currentActorRole();

        var cmd = new BuscarExtrasCommand(
                /* idExtra      */ idExtra,
                /* idHabitacion */ idHabitacion,
                /* nombre       */ nombre,
                /* idActor      */ actor,
                /* rolActor     */ rolActor,
                /* page         */ page,
                /* size         */ size
        );
        var resp = buscarExtrasUc.ejecutar(cmd);
        return ResponseEntity.ok(resp);
    }

    /** DELETE /api/extras/{id}  → elimina un extra */
    @Operation(
            summary     = "Eliminar un extra",
            description = "Elimina un extra del catálogo"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Extra eliminado exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = EliminarExtraResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error al eliminar el extra")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<EliminarExtraResponse> eliminarExtra(
            @PathVariable Long id
    ) {
        Long actor   = SecurityUtils.currentActorId();
        var  rolActor = SecurityUtils.currentActorRole();

        var cmd = new EliminarExtraCommand(id, actor, rolActor);
        var resp = eliminarExtraUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /** PUT /api/extras/habitaciones  → asigna/quita/reemplaza extras en una habitación */
    @Operation(
            summary     = "Asignar extras a habitación",
            description = "Asignar, quitar o reemplazar extras en una habitación"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Extras asignados/excluidos exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = AsignarExtrasHabitacionResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PutMapping("/habitaciones")
    public ResponseEntity<AsignarExtrasHabitacionResponse> asignarAlaHabitacion(
            @Valid @RequestBody AsignarExtrasHabitacionCommand cmd
    ) {
        var resp = asignarExtrasHabitacionUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /** PUT /api/extras/reservas  → solicita/quita/reemplaza extras en una reserva */
    @Operation(
            summary     = "Solicitar extras en reserva",
            description = "Solicitar, quitar o reemplazar extras en una reserva"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Extras en reserva actualizados exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = SolicitarExtrasReservaResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PutMapping("/reservas")
    public ResponseEntity<SolicitarExtrasReservaResponse> solicitarAReserva(
            @Valid @RequestBody SolicitarExtrasReservaCommand cmd
    ) {
        var resp = solicitarExtrasReservaUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }
}