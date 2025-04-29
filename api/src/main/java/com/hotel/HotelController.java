package com.hotel;

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
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hoteles")
@RequiredArgsConstructor
@Tag(name = "Hoteles", description = "Operaciones relacionadas con hoteles")
@SecurityRequirement(name = "bearerAuth")
public class HotelController {

    private final CrearHotelUseCase       crearHotelUc;
    private final ActualizarHotelUseCase  actualizarHotelUc;
    private final EliminarHotelUseCase    eliminarHotelUc;
    private final BuscarHotelesUseCase    buscarHotelesUc;

    /** POST /api/hoteles → crea un hotel (solo ADMIN) */
    @Operation(
            summary     = "Crear hotel",
            description = "Crea un nuevo hotel (solo ADMIN)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description  = "Hotel creado exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = CrearHotelResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PostMapping
    public ResponseEntity<CrearHotelResponse> crear(
            @Valid @RequestBody CrearHotelCommand cmd
    ) {
        var resp = crearHotelUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /** PUT /api/hoteles/{id} → actualiza un hotel (solo ADMIN) */
    @Operation(
            summary     = "Actualizar hotel",
            description = "Actualiza un hotel existente (solo ADMIN)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Hotel actualizado exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ActualizarHotelResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ActualizarHotelResponse> actualizar(
            @PathVariable("id") Long idHotel,
            @Valid @RequestBody ActualizarHotelCommand body
    ) {
        var cmd = new ActualizarHotelCommand(
                idHotel,
                body.rolSolicitante(),
                body.nombre(),
                body.direccion(),
                body.ciudad(),
                body.pais(),
                body.numeroEstrellas(),
                body.telefono(),
                body.email(),
                body.latitud(),
                body.longitud(),
                body.idsServicios()
        );
        var resp = actualizarHotelUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /** DELETE /api/hoteles/{id} → elimina un hotel (solo ADMIN) */
    @Operation(
            summary     = "Eliminar hotel",
            description = "Elimina un hotel existente (solo ADMIN)"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Hotel eliminado exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = EliminarHotelResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error al eliminar el hotel")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<EliminarHotelResponse> eliminar(
            @PathVariable("id") Long idHotel
    ) {
        Long idActor  = SecurityUtils.currentActorId();
        Rol  rolActor = SecurityUtils.currentActorRole();
        var cmd  = new EliminarHotelCommand(idHotel, idActor, rolActor);
        var resp = eliminarHotelUc.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**
     * GET /api/hoteles
     * → listado y búsqueda paginada con filtros opcionales
     */
    @Operation(
            summary     = "Listar y buscar hoteles",
            description = "Listado y búsqueda de hoteles con filtros opcionales"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Hoteles obtenidos exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<PageResponse<ListarHotelesResponse>> buscar(
            @RequestParam(required = false) String pais,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) Integer numeroEstrellas,
            @RequestParam(required = false) String nombreParcial,
            @RequestParam(required = false) String direccionParcial,
            @RequestParam(required = false) String telefonoParcial,
            @RequestParam(required = false) String emailParcial,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        Long idActor  = SecurityUtils.currentActorId();
        Rol  rolActor = SecurityUtils.currentActorRole();
        var cmd = new BuscarHotelesCommand(
                pais,
                ciudad,
                numeroEstrellas,
                nombreParcial,
                direccionParcial,
                telefonoParcial,
                emailParcial,
                idActor,
                rolActor,
                page,
                size
        );
        return ResponseEntity.ok(buscarHotelesUc.ejecutar(cmd));
    }
}