package com.servicio;

import com.PageResponse;
import com.reservahoteles.application.dto.*;
import com.reservahoteles.application.port.in.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.security.SecurityUtils.currentActorId;
import static com.security.SecurityUtils.currentActorRole;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
@Tag(name = "Servicios", description = "Operaciones de gestión de servicios y asignación a hoteles")
@SecurityRequirement(name = "bearerAuth")
public class ServicioController {

    // ── Catálogo CRUD ───────────────────────────
    private final RegistrarServicioUseCase    registrarUc;
    private final BuscarServiciosUseCase      buscarUc;
    private final ActualizarServicioUseCase   actualizarUc;
    private final EliminarServicioUseCase     eliminarUc;

    // ── Asignar a hoteles ───────────────────────
    private final AsignarServiciosHotelUseCase asignarHotelesUc;

    /** POST /api/servicios → crea un nuevo servicio (solo ADMIN) */
    @Operation(
            summary     = "Crear servicio",
            description = "Crea un nuevo servicio en el catálogo (solo ADMIN)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description  = "Servicio creado exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = RegistrarServicioResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<RegistrarServicioResponse> crear(
            @Valid @RequestBody RegistrarServicioCommand cmd
    ) {
        var resp = registrarUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**
     * GET /api/servicios
     * → listado / búsqueda paginada de servicios
     */
    @Operation(
            summary     = "Listar o buscar servicios",
            description = "Listado y búsqueda paginada de servicios; si se proporciona idServicio devuelve solo ese servicio"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Servicios obtenidos exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<PageResponse<ServicioResponse>> buscar(
            @RequestParam(required = false) Long idServicio,
            @RequestParam(required = false) String nombreParcial,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        var cmd = new BuscarServiciosCommand(
                idServicio,
                nombreParcial,
                currentActorId(),
                currentActorRole(),
                page,
                size
        );
        var resp = buscarUc.ejecutar(cmd);
        return ResponseEntity.ok(resp);
    }

    /** GET /api/servicios/{id} → obtiene un servicio por su id */
    @Operation(
            summary     = "Detalle de servicio",
            description = "Obtiene un servicio por su ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Servicio obtenido exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ServicioResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServicioResponse> detalle(
            @PathVariable("id") Long idServicio
    ) {
        var page = buscar(
                idServicio,
                null,
                0, 1
        ).getBody();
        if (page == null || page.getItems().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(page.getItems().get(0));
    }

    /** PUT /api/servicios/{id} → actualiza nombre/descr (solo ADMIN) */
    @Operation(
            summary     = "Actualizar servicio",
            description = "Actualiza nombre y descripción de un servicio (solo ADMIN)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Servicio actualizado exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ActualizarServicioResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ActualizarServicioResponse> actualizar(
            @PathVariable("id") Long idServicio,
            @Valid @RequestBody ActualizarServicioCommand body
    ) {
        var cmd = new ActualizarServicioCommand(
                idServicio,
                body.nombre(),
                body.descripcion(),
                body.idActor(),
                body.rolActor()
        );
        var resp = actualizarUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /** DELETE /api/servicios/{id} → elimina un servicio (solo ADMIN) */
    @Operation(
            summary     = "Eliminar servicio",
            description = "Elimina un servicio del catálogo (solo ADMIN)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Servicio eliminado exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = EliminarServicioResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error al eliminar el servicio")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<EliminarServicioResponse> eliminar(
            @PathVariable("id") Long idServicio
    ) {
        var cmd  = new EliminarServicioCommand(
                idServicio,
                currentActorId(),
                currentActorRole()
        );
        var resp = eliminarUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**
     * PUT /api/servicios/hoteles
     * → asignar, quitar o reemplazar la lista de servicios de un hotel
     */
    @Operation(
            summary     = "Asignar servicios a hotel",
            description = "Asigna, quita o reemplaza servicios asociados a un hotel"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Servicios asignados exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = AsignarServiciosHotelResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PutMapping("/hoteles")
    public ResponseEntity<AsignarServiciosHotelResponse> asignarAHotel(
            @Valid @RequestBody AsignarServiciosHotelCommand cmd
    ) {
        var resp = asignarHotelesUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }
}

//CRUD use‑cases
//
//RegistrarServicioUseCase: recibe RegistrarServicioCommand(nombre, descripción, idActor, rolActor)
//
//BuscarServiciosUseCase: unifica listado y consulta (BuscarServiciosCommand(idServicio?, nombreParcial?, idActor, rolActor, page, size))
//
//ActualizarServicioUseCase: recibe ActualizarServicioCommand(idServicio, nombre, descripción, idActor, rolActor)
//
//EliminarServicioUseCase: recibe EliminarServicioCommand(idServicio, idActor, rolActor)
//
//Asignar a hoteles
//
//Asigna/Quita/Reemplaza la lista de servicios de un hotel usando tu AsignarServiciosHotelUseCase. El body es tu AsignarServiciosHotelCommand.
//
//Consistencia con tu estilo
//
//HTTP status → 201 en creación exitosa, 200 en el resto de los éxitos, 400 en errores de validación/negocio.
//
//Búsqueda paginada y consulta por ID via el mismo buscarUc.