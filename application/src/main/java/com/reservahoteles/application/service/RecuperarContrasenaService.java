package com.reservahoteles.application.service;
import com.reservahoteles.application.dto.RecuperarContrasenaCommand;
import com.reservahoteles.application.dto.RecuperarContrasenaResponse;
import com.reservahoteles.application.port.in.RecuperarContrasenaUseCase;
import com.reservahoteles.domain.entity.Cliente;
import com.reservahoteles.domain.entity.Credencial;
import com.reservahoteles.domain.port.out.ClienteRepository;
import com.reservahoteles.domain.port.out.CredencialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
public class RecuperarContrasenaService implements RecuperarContrasenaUseCase {

    private final ClienteRepository clienteRepo;
    private final CredencialRepository credRepo;
    private final PasswordEncoder passwordEncoder;

    // Validación básica de email
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    // Contraseña: ≥8 chars, mayúscula, minúscula, dígito y especial
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,}$");

    // Tokens en memoria: token ➝ clienteId
    private static final Map<String, Long> TOKEN_MAP       = new ConcurrentHashMap<>();
    private static final Map<String, LocalDateTime> EXPIRY = new ConcurrentHashMap<>();
    private static final long TOKEN_EXPIRY_MINUTES = 15;

    public RecuperarContrasenaService(ClienteRepository clienteRepo,
                                      CredencialRepository credRepo,
                                      PasswordEncoder passwordEncoder) {
        this.clienteRepo = clienteRepo;
        this.credRepo    = credRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RecuperarContrasenaResponse ejecutar(RecuperarContrasenaCommand cmd) {
        // 1) Validar email
        if (cmd.email() == null || !EMAIL_PATTERN.matcher(cmd.email()).matches()) {
            return new RecuperarContrasenaResponse(
                    "FAILURE", "Debes indicar un email válido"
            );
        }

        // 2) ¿Es fase de solicitud de token?
        if (cmd.token() == null) {
            // 2.a) Buscar cliente por email
            List<Cliente> clientes = clienteRepo.buscarPorCriterios(
                    cmd.email(), null, null, null);
            if (clientes.isEmpty()) {
                // No revelamos si existe o no
                return new RecuperarContrasenaResponse(
                        "SUCCESS",
                        "Si existe una cuenta asociada, recibirás un correo en breve"
                );
            }
            Cliente cli = clientes.get(0);

            // 2.b) Generar token y guardarlo con expiración
            String token = UUID.randomUUID().toString();
            TOKEN_MAP.put(token, cli.getId());
            EXPIRY.put(token, LocalDateTime.now().plus(TOKEN_EXPIRY_MINUTES, ChronoUnit.MINUTES));

            // 2.c) “Enviar” por email (simulado)
            // → aquí podrías llamar a un EmailService. Para la demo lo devolvemos en el mensaje:
            return new RecuperarContrasenaResponse(
                    "SUCCESS",
                    "Correo enviado. Tu token: " + token
            );
        }

        // 3) Fase de restablecer contraseña: token + nueva + confirmación
        String token = cmd.token();
        Long cliId   = TOKEN_MAP.get(token);
        LocalDateTime exp = EXPIRY.get(token);

        if (cliId == null || exp == null || LocalDateTime.now().isAfter(exp)) {
            return new RecuperarContrasenaResponse(
                    "FAILURE", "Token inválido o expirado"
            );
        }
        if (cmd.newPassword() == null || cmd.confirmPassword() == null) {
            return new RecuperarContrasenaResponse(
                    "FAILURE", "Debes indicar la nueva contraseña y confirmarla"
            );
        }
        if (!cmd.newPassword().equals(cmd.confirmPassword())) {
            return new RecuperarContrasenaResponse(
                    "FAILURE", "La confirmación no coincide"
            );
        }
        if (!PASSWORD_PATTERN.matcher(cmd.newPassword()).matches()) {
            return new RecuperarContrasenaResponse(
                    "FAILURE",
                    "La contraseña debe tener ≥8 caracteres, mayúscula, minúscula, dígito y carácter especial"
            );
        }

        // 4) Recuperar credencial y actualizar hash
        Credencial cred = credRepo.findByClienteId(cliId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró credencial para cliente " + cliId
                ));
        cred.setPasswordHash(passwordEncoder.encode(cmd.newPassword()));
        cred.setUltimaActualizacion(LocalDateTime.now());
        credRepo.save(cred);

        // 5) Limpiar token
        TOKEN_MAP.remove(token);
        EXPIRY.remove(token);

        return new RecuperarContrasenaResponse(
                "SUCCESS",
                "Contraseña actualizada correctamente"
        );
    }
}

//RecuperarContrasenaCommand lleva cuatro campos:
//
//email (siempre obligatorio),
//
//token (nulo en la primera fase),
//
//newPassword y confirmPassword (sólo en la segunda fase).
//
//Fase 1 (token == null):
//
//Validamos el formato del email,
//
//Buscamos el cliente por email (si no existe, devolvemos igualmente éxito para no permitir enumerar cuentas),
//
//Generamos un token de un solo uso, lo guardamos en memoria con TTL de 15 min,
//
//Devolvemos un mensaje con el token (simulando el envío de correo).
//
//Fase 2 (token != null):
//
//Comprobamos que el token existe y no haya expirado,
//
//Validamos que newPassword y confirmPassword estén presentes, coincidan y cumplan la política de fuerza,
//
//Obtenemos la Credencial asociada al cliente, hasheamos y guardamos la nueva contraseña,
//
//Limpiamos el token de nuestra memoria.
//
//De esta forma tu front podrá:
//
//Mostrar un formulario para introducir email y, tras pulsar enviar, mostrar un alert “correo enviado” con el token (en producción el token iría en el correo).
//
//Luego mostrar un segundo formulario para introducir token, nueva contraseña y confirmación, y al enviarlo ejecutar la fase 2.