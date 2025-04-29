package com.reservahoteles.application.service;
import com.reservahoteles.application.dto.ConfirmarRecuperarContrasenaCommand;
import com.reservahoteles.application.dto.ConfirmarRecuperarContrasenaResponse;
import com.reservahoteles.application.port.in.ConfirmarRecuperarContrasenaUseCase;
import com.reservahoteles.domain.entity.Credencial;
import com.reservahoteles.domain.port.out.CredencialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@Service
public class ConfirmarRecuperarContrasenaService implements ConfirmarRecuperarContrasenaUseCase {

    private final CredencialRepository credRepo;
    private final PasswordEncoder passwordEncoder;

    // mismo patrón que para cambiar pwd
    private static final Pattern PASS_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,}$");

    public ConfirmarRecuperarContrasenaService(CredencialRepository credRepo,
                                               PasswordEncoder passwordEncoder) {
        this.credRepo = credRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ConfirmarRecuperarContrasenaResponse ejecutar(ConfirmarRecuperarContrasenaCommand cmd) {
        // 1) Validar token
        if (cmd.token() == null || cmd.token().isBlank()) {
            return new ConfirmarRecuperarContrasenaResponse(
                    null, "FAILURE", "Token inválido"
            );
        }
        // 2) Buscar credenciales por token
        Credencial cred = credRepo.findByResetToken(cmd.token())
                .orElseThrow(() -> new NoSuchElementException("Token de recuperación no válido"));

        // 3) Validar expiración (si lo has modelado)
        if (cred.getResetTokenExpiry() != null
                && cred.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return new ConfirmarRecuperarContrasenaResponse(
                    null, "FAILURE", "El token ha expirado"
            );
        }

        // 4) Validar nueva contraseña y confirmación
        if (cmd.newPassword() == null || !PASS_PATTERN.matcher(cmd.newPassword()).matches()) {
            return new ConfirmarRecuperarContrasenaResponse(
                    null, "FAILURE",
                    "La contraseña debe tener ≥8 caracteres, " +
                            "al menos una mayúscula, una minúscula, un dígito y un caracter especial"
            );
        }
        if (!cmd.newPassword().equals(cmd.confirmPassword())) {
            return new ConfirmarRecuperarContrasenaResponse(
                    null, "FAILURE", "La confirmación no coincide"
            );
        }

        // 5) Aplicar el cambio
        cred.setPasswordHash(passwordEncoder.encode(cmd.newPassword()));
        cred.setUltimaActualizacion(LocalDateTime.now());
        // Limpiar el token para que no se reutilice
        cred.setResetToken(null);
        cred.setResetTokenExpiry(null);

        credRepo.save(cred);

        return new ConfirmarRecuperarContrasenaResponse(
                cred.getIdCliente(),
                "SUCCESS",
                "Contraseña actualizada correctamente"
        );
    }
}