package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.AutenticarClienteCommand;
import com.reservahoteles.application.dto.AutenticarClienteResponse;
import com.reservahoteles.application.port.in.AutenticarClienteUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Credencial;
import com.reservahoteles.domain.entity.Cliente;
import com.reservahoteles.domain.port.out.CredencialRepository;
import com.reservahoteles.domain.port.out.ClienteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class AutenticarClienteService implements AutenticarClienteUseCase {

    private final CredencialRepository credRepo;
    private final ClienteRepository  clienteRepo;
    private final PasswordEncoder    passwordEncoder;

    /** Número máximo de intentos antes de bloquear cuenta */
    private static final int MAX_FAILED_ATTEMPTS = 5;
    /** Tiempo de bloqueo (p. ej. 15 minutos) */
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    public AutenticarClienteService(CredencialRepository credRepo,
                                    ClienteRepository clienteRepo,
                                    PasswordEncoder passwordEncoder) {
        this.credRepo        = credRepo;
        this.clienteRepo     = clienteRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AutenticarClienteResponse ejecutar(AutenticarClienteCommand cmd) {
        LocalDateTime now = LocalDateTime.now();

        // 1. Validaciones básicas
        if (cmd.username() == null || cmd.username().isBlank()
                || cmd.password() == null || cmd.password().isBlank()) {
            return new AutenticarClienteResponse(
                    null, null, null,
                    "FAILURE",
                    "Username y password son obligatorios"
            );
        }

        // 2. Recuperar credencial
        Credencial cred = credRepo.findByUsername(cmd.username())
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe usuario '" + cmd.username() + "'"
                ));

        // 3. Comprobar estado de bloqueo
        if (!cred.isAccountNonLocked()) {
            // si ha pasado el tiempo de bloqueo, desbloqueamos
            if (cred.getLockTime() != null
                    && cred.getLockTime().plus(LOCK_DURATION).isBefore(now)) {
                cred.setAccountNonLocked(true);
                cred.setFailedAttempts(0);
                cred.setLockTime(null);
            } else {
                LocalDateTime unlockAt = cred.getLockTime().plus(LOCK_DURATION);
                return new AutenticarClienteResponse(
                        null, null, null,
                        "FAILURE",
                        "Cuenta bloqueada. Inténtalo de nuevo después de " + unlockAt
                );
            }
        }

        // 4. Verificar contraseña
        if (!passwordEncoder.matches(cmd.password(), cred.getPasswordHash())) {
            // incremento de intentos fallidos
            int errores = cred.getFailedAttempts() + 1;
            cred.setFailedAttempts(errores);
            // si llega al límite, bloquea
            if (errores >= MAX_FAILED_ATTEMPTS) {
                cred.setAccountNonLocked(false);
                cred.setLockTime(now);
                credRepo.save(cred);
                return new AutenticarClienteResponse(
                        null, null, null,
                        "FAILURE",
                        "Has superado el número máximo de intentos. Cuenta bloqueada por "
                                + (LOCK_DURATION.toMinutes()) + " minutos."
                );
            } else {
                credRepo.save(cred);
                return new AutenticarClienteResponse(
                        null, null, null,
                        "FAILURE",
                        "Credenciales incorrectas. Te quedan "
                                + (MAX_FAILED_ATTEMPTS - errores) + " intentos."
                );
            }
        }

        // 5. Login exitoso → resetear contador y desbloquear
        if (cred.getFailedAttempts() > 0 || !cred.isAccountNonLocked()) {
            cred.setFailedAttempts(0);
            cred.setAccountNonLocked(true);
            cred.setLockTime(null);
        }

        // 6. Recuperar datos del cliente
        Cliente cliente = clienteRepo.findById(cred.getIdCliente())
                .orElseThrow(() -> new IllegalStateException(
                        "No se encontró el cliente para credencial id=" + cred.getId()
                ));

        // 7. Generar token (simulado o JWT real)
        String token = UUID.randomUUID().toString();

        // 8. Actualizar timestamps y persistir
        cred.setUltimaActualizacion(now);
        credRepo.save(cred);

        // 9. Devolver respuesta
        return new AutenticarClienteResponse(
                cliente.getId(),
                token,
                cliente.getRol().name(),
                "SUCCESS",
                "Autenticación correcta"
        );
    }
}