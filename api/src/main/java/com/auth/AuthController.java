package com.auth;

import com.reservahoteles.application.dto.*;
import com.reservahoteles.application.port.in.AutenticarClienteUseCase;
import com.reservahoteles.application.port.in.ConfirmarRecuperarContrasenaUseCase;
import com.reservahoteles.application.port.in.RecuperarContrasenaUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Operaciones de autenticación y recuperación de contraseña")
@RequiredArgsConstructor
public class AuthController {

    private final AutenticarClienteUseCase loginUseCase;
    private final RecuperarContrasenaUseCase recuperarUseCase;
    private final ConfirmarRecuperarContrasenaUseCase confirmarUseCase;

    /**  POST /api/auth/login  → autenticación */
    @Operation(
            summary = "Autenticar cliente",
            description = "Recibe correo y contraseña, valida credenciales y devuelve token o error."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Autenticación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AutenticarClienteResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping("/login")
    public ResponseEntity<AutenticarClienteResponse> login(
            @Valid @RequestBody AutenticarClienteCommand cmd
    ) {
        var resp = loginUseCase.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(resp);
    }

    /**  POST /api/auth/recuperar  → envía “correo” de recuperación */
    @Operation(
            summary = "Iniciar recuperación de contraseña",
            description = "Envía un correo con un token de recuperación al email indicado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Correo de recuperación enviado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecuperarContrasenaResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Email no válido o no registrado")
    })
    @PostMapping("/recuperar")
    public ResponseEntity<RecuperarContrasenaResponse> recuperar(
            @Valid @RequestBody RecuperarContrasenaCommand cmd
    ) {
        var resp = recuperarUseCase.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }

    /**  POST /api/auth/recuperar/confirmar  → confirma token y cambia password */
    @Operation(
            summary = "Confirmar recuperación de contraseña",
            description = "Valida el token enviado por email y actualiza la contraseña."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Contraseña actualizada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConfirmarRecuperarContrasenaResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Token inválido o expirado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping("/recuperar/confirmar")
    public ResponseEntity<ConfirmarRecuperarContrasenaResponse> confirmar(
            @Valid @RequestBody ConfirmarRecuperarContrasenaCommand cmd
    ) {
        var resp = confirmarUseCase.ejecutar(cmd);
        HttpStatus status = "SUCCESS".equals(resp.status())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(resp);
    }
}