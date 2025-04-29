package com.credencial;
import com.reservahoteles.application.dto.ActualizarCredencialesCommand;
import com.reservahoteles.application.dto.ActualizarCredencialesResponse;
import com.reservahoteles.application.dto.EliminarCredencialesCommand;
import com.reservahoteles.application.dto.EliminarCredencialesResponse;
import com.reservahoteles.application.port.in.ActualizarCredencialesUseCase;
import com.reservahoteles.application.port.in.EliminarCredencialesUseCase;
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
 * Endpoints para gestionar las credenciales de los clientes:
 *   - actualización de username/contraseña
 *   - eliminación/desactivación de credenciales
 */
@RestController
@RequestMapping("/api/credenciales")
@RequiredArgsConstructor
@Tag(name = "Credenciales", description = "Operaciones para gestionar las credenciales de los clientes")
@SecurityRequirement(name = "bearerAuth")
public class CredencialController {

    private final ActualizarCredencialesUseCase actualizarUc;
    private final EliminarCredencialesUseCase eliminarUc;

    /**
     * PUT /api/credenciales/{idCliente}
     * Actualiza el username y/o la contraseña de la credencial del cliente.
     */
    @Operation(
            summary     = "Actualizar credenciales de cliente",
            description = "Actualiza el username y/o la contraseña de la credencial del cliente especificado"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Credenciales actualizadas exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = ActualizarCredencialesResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error en los datos enviados")
    })
    @PutMapping("/{idCliente}")
    public ResponseEntity<ActualizarCredencialesResponse> actualizar(
            @PathVariable Long idCliente,
            @Valid @RequestBody ActualizarCredencialesCommand body
    ) {
        // Inyectamos el path-variable en el comando
        var cmd = new ActualizarCredencialesCommand(
                idCliente,
                body.username(),
                body.currentPassword(),
                body.newPassword(),
                body.confirmPassword()
        );
        var resp = actualizarUc.ejecutar(cmd);

        // SUCCESS → 200 OK, FAILURE → 400 Bad Request
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(resp);
    }

    /**
     * DELETE /api/credenciales/{idCredencial}
     * Borra o desactiva una credencial.
     * Solo el propio cliente o un ADMIN puede hacerlo.
     */
    @Operation(
            summary     = "Eliminar credencial de cliente",
            description = "Borra o desactiva una credencial. Solo el propio cliente o un ADMIN puede hacerlo."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description  = "Credencial eliminada exitosamente",
                    content      = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = EliminarCredencialesResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Error al eliminar la credencial")
    })
    @DeleteMapping("/{idCredencial}")
    public ResponseEntity<EliminarCredencialesResponse> eliminar(
            @PathVariable Long idCredencial
    ) {
        Long actor   = SecurityUtils.currentActorId();
        var  rolActor = SecurityUtils.currentActorRole();

        var cmd  = new EliminarCredencialesCommand(idCredencial, actor, rolActor);
        var resp = eliminarUc.ejecutar(cmd);

        // SUCCESS o IGNORED → 200 OK, FAILURE → 400 Bad Request
        HttpStatus status = !"FAILURE".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(resp);
    }
}

//Actualizar credenciales (PUT /api/credenciales/{idCliente})
//
//Recibe en la ruta el idCliente y en el cuerpo el username, currentPassword, newPassword y confirmPassword.
//
//Mapea a tu ActualizarCredencialesCommand y llama al UseCase.
//
//Devuelve 200 OK si el UseCase responde "SUCCESS", o 400 Bad Request en caso contrario.
//
//Eliminar credenciales (DELETE /api/credenciales/{idCredencial})
//
//Usa headers X-Actor-Id y X-Actor-Rol para pasar permisos al comando.
//
//Llama a EliminarCredencialesUseCase.
//
//Si la respuesta es "FAILURE", retorna 400, en cualquier otro caso ("SUCCESS" o "IGNORED") retorna 200.