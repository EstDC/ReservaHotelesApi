package com.habitacion;

import com.PageResponse;
import com.reservahoteles.application.dto.*;
import com.reservahoteles.application.port.in.*;
import com.reservahoteles.common.enums.EstadoHabitacion;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.TipoHabitacion;
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

import java.math.BigDecimal;

/**
 * CRUD y consultas de Habitaciones
 */
@RestController
@RequestMapping("/api/habitaciones")
@RequiredArgsConstructor
@Tag(name = "Habitaciones", description = "CRUD y consultas de Habitaciones")
@SecurityRequirement(name = "bearerAuth")
public class HabitacionController {

    private final RegistrarHabitacionUseCase            registrarHabitacion;
    private final ActualizarHabitacionUseCase           actualizarHabitacion;
    private final EliminarHabitacionUseCase             eliminarHabitacion;
    private final ConsultarHabitacionesPorHotelUseCase  consultarHabitaciones;
    private final ConsultarHistorialHabitacionUseCase   consultarHistorial;

    /**
     * POST /api/habitaciones
     */
    @Operation(
            summary     = "Crear nueva habitación",
            description = "Crea una nueva habitación asociada a un hotel"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description  = "Habitación creada exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = RegistrarHabitacionResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<RegistrarHabitacionResponse> crear(
            @Valid @RequestBody RegistrarHabitacionCommand body
    ) {
        Long actor   = SecurityUtils.currentActorId();
        Rol  rolActor = SecurityUtils.currentActorRole();

        // inyectamos actor y rol en el command
        var cmd = new RegistrarHabitacionCommand(
                body.idHotel(),
                body.numeroHabitacion(),
                body.tipo(),
                body.capacidad(),
                body.precioPorNoche(),
                body.estado(),
                body.descripcion(),
                actor,
                rolActor
        );
        var resp = registrarHabitacion.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**
     * PUT /api/habitaciones/{idHabitacion}
     */
    @Operation(
            summary     = "Actualizar habitación",
            description = "Actualiza los datos de la habitación especificada"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Habitación actualizada exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ActualizarHabitacionResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PutMapping("/{idHabitacion}")
    public ResponseEntity<ActualizarHabitacionResponse> actualizar(
            @PathVariable Long idHabitacion,
            @Valid @RequestBody ActualizarHabitacionCommand body
    ) {
        // body already has all fields (incluye idHabitacion)
        var cmd = new ActualizarHabitacionCommand(
                idHabitacion,
                body.idHotel(),
                body.numeroHabitacion(),
                body.tipo(),
                body.capacidad(),
                body.precioPorNoche(),
                body.estado(),
                body.descripcion(),
                body.idsExtras()
        );
        var resp = actualizarHabitacion.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**
     * DELETE /api/habitaciones/{idHabitacion}
     */
    @Operation(
            summary     = "Eliminar habitación",
            description = "Elimina la habitación especificada"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Habitación eliminada exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = EliminarHabitacionResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error al eliminar la habitación")
    })
    @DeleteMapping("/{idHabitacion}")
    public ResponseEntity<EliminarHabitacionResponse> eliminar(
            @PathVariable Long idHabitacion
    ) {
        Long actor   = SecurityUtils.currentActorId();
        Rol  rolActor = SecurityUtils.currentActorRole();

        var cmd = new EliminarHabitacionCommand(idHabitacion, actor, rolActor);
        var resp = eliminarHabitacion.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**
     * GET /api/habitaciones?hotel=...&estado=...&tipo=...
     *                     &capMin=...&capMax=...
     *                     &precioMin=...&precioMax=...
     *                     &page=...&size=...
     */
    @Operation(
            summary     = "Listar habitaciones por hotel",
            description = "Lista habitaciones filtradas por hotel y otros parámetros opcionales"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de habitaciones obtenido"),
            @ApiResponse(responseCode = "400", description = "Error en los parámetros de búsqueda"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<PageResponse<HabitacionResponse>> listarPorHotel(
            @RequestParam("hotel") Long idHotel,
            @RequestParam(value="estado",      required=false) EstadoHabitacion estado,
            @RequestParam(value="tipo",        required=false) TipoHabitacion tipo,
            @RequestParam(value="capMin",      required=false) Long capacidadMin,
            @RequestParam(value="capMax",      required=false) Long capacidadMax,
            @RequestParam(value="precioMin",   required=false) BigDecimal precioMin,
            @RequestParam(value="precioMax",   required=false) BigDecimal precioMax,
            @RequestParam(defaultValue="0")    int page,
            @RequestParam(defaultValue="10")   int size
    ) {
        var cmd = new ConsultarHabitacionesCommand(
                idHotel,
                estado,
                tipo,
                capacidadMin,
                capacidadMax,
                precioMin,
                precioMax,
                page,
                size
        );
        var resp = consultarHabitaciones.ejecutar(cmd);
        return ResponseEntity.ok(resp);
    }

    /**
     * GET /api/habitaciones/{idHabitacion}/historial?page=...&size=...
     */
    @Operation(
            summary     = "Historial de la habitación",
            description = "Obtiene el historial de revisiones de la habitación especificada"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los parámetros de consulta"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/{idHabitacion}/historial")
    public ResponseEntity<PageResponse<RevisionHabitacionDTO>> historial(
            @PathVariable Long idHabitacion,
            @RequestParam(defaultValue="0")  int page,
            @RequestParam(defaultValue="10") int size
    ) {
        var cmd = new ConsultarHistorialHabitacionCommand(
                idHabitacion,
                page,
                size
        );
        var resp = consultarHistorial.ejecutar(cmd);
        return ResponseEntity.ok(resp);
    }
}