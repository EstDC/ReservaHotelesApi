package com.cliente;

import com.PageResponse;
import com.reservahoteles.application.dto.*;
import com.reservahoteles.application.port.in.*;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.TipoIdentificacion;
import com.security.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * Endpoints para todo lo relativo a Cliente:
 *   - registro (no necesita autenticación)
 *   - listados, búsqueda, obtención por id (requiere actor + rol)
 *   - actualización y baja
 */

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones relacionadas con clientes")//Tag para swagger
@SecurityRequirement(name = "bearerAuth")//Tag para swagger que indica que se requiere autenticación
public class ClienteController {

    private final RegistrarClienteUseCase    registrarCliente;
    private final ActualizarClienteUseCase   actualizarCliente;
    private final EliminarClienteUseCase     eliminarCliente;
    private final BuscarClientesUseCase      buscarClientes;

    /**  POST /api/clientes  → auto‑registro (cliente crea su cuenta) */
    @PostMapping
    @Operation(summary = "Registrar un nuevo cliente")//Tag para swagger
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })//Tag para swagger que documenta las posibles respuestas
    public ResponseEntity<RegistrarClienteResponse> registrar(
            @Valid @RequestBody RegistrarClienteCommand cmd
    ) {
        var resp   = registrarCliente.ejecutar(cmd);
        var status = "SUCCESS".equals(resp.status())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**
     * GET /api/clientes
     * → listado y búsqueda avanzada (ADMIN o perfil propio para CLIENTE)
     * Parámetros de filtro *opcionales* y paginación:
     */
    @GetMapping
    @Operation(summary = "Listado y búsqueda avanzada de clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado de clientes obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    public ResponseEntity<PageResponse<BuscarClientesResponse>> buscar(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) String tipoIdentificacion,
            @RequestParam(required = false) String identificacion,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long idActor = SecurityUtils.currentActorId();
        Rol rolActor = SecurityUtils.currentActorRole();
        var tipoId = tipoIdentificacion == null
                ? null
                : TipoIdentificacion.valueOf(tipoIdentificacion.toUpperCase());
        var cmd = new BuscarClientesCommand(
                id,
                nombre,
                email,
                telefono,
                direccion,
                tipoId,
                identificacion,
                idActor,
                rolActor,
                page,
                size
        );
        var resp = buscarClientes.ejecutar(cmd);
        return ResponseEntity.ok(resp);
    }

    /**  PUT /api/clientes/{id}→ actualizar datos de un cliente */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos de un cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ActualizarClienteResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarClienteCommand body
    ) {
        Long idActor = SecurityUtils.currentActorId();
        Rol rolActor = SecurityUtils.currentActorRole();
        var cmd   = new ActualizarClienteCommand(
                id,
                body.nombre(),
                body.email(),
                body.telefono(),
                body.direccion(),
                body.tipoIdentificacion(),
                body.identificacion(),
                idActor,
                rolActor
        );
        var resp  = actualizarCliente.ejecutar(cmd);
        var status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**  DELETE /api/clientes/{id}  → admin elimina o cliente se da de baja */
    @DeleteMapping("/{id}")
    @Operation(summary = "Administrador elimina cliente o el cliente se da de baja")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error al eliminar el cliente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<EliminarClienteResponse> eliminar(
            @PathVariable Long id
    ) {
        Long idActor = SecurityUtils.currentActorId();
        Rol rolActor = SecurityUtils.currentActorRole();
        var cmd   = new EliminarClienteCommand(id, idActor, rolActor);
        var resp  = eliminarCliente.ejecutar(cmd);
        var status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }
}
