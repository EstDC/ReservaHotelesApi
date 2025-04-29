package com.reservahoteles.application.service;


import com.reservahoteles.application.dto.ActualizarCredencialesCommand;
import com.reservahoteles.application.dto.ActualizarCredencialesResponse;
import com.reservahoteles.application.port.in.ActualizarCredencialesUseCase;
import com.reservahoteles.domain.entity.Credencial;
import com.reservahoteles.domain.port.out.CredencialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@Service
public class ActualizarCredencialesService implements ActualizarCredencialesUseCase {

    private final CredencialRepository credRepo;
    private final PasswordEncoder passwordEncoder;

    // Al menos 8 caracteres, una mayúscula, una minúscula, un dígito y un caracter especial
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,}$"
    );

    public ActualizarCredencialesService(CredencialRepository credRepo,
                                         PasswordEncoder passwordEncoder) {
        this.credRepo = credRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ActualizarCredencialesResponse ejecutar(ActualizarCredencialesCommand cmd) {
        // 1. Obtener credencial por cliente
        Credencial cred = credRepo.findByClienteId(cmd.idCliente())
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontraron credenciales para cliente id=" + cmd.idCliente()
                ));

        boolean cambiado = false;

        // 2. Cambiar username si se pide y es distinto
        if (cmd.username() != null && !cmd.username().equals(cred.getUsername())) {
            // 2.1 Verificar unicidad
            if (credRepo.findByUsername(cmd.username()).isPresent()) {
                return new ActualizarCredencialesResponse(
                        cmd.idCliente(), "FAILURE", "El nombre de usuario ya está en uso"
                );
            }
            cred.setUsername(cmd.username());
            cambiado = true;
        }

        // 3. Cambiar contraseña si nos pasan newPassword
        if (cmd.newPassword() != null) {
            // Debe enviar también currentPassword y confirmPassword
            if (cmd.currentPassword() == null || cmd.confirmPassword() == null) {
                return new ActualizarCredencialesResponse(
                        cmd.idCliente(), "FAILURE",
                        "Para cambiar la contraseña debe indicar la actual y confirmarla"
                );
            }
            // 3.1 Verificar que currentPassword coincide
            if (!passwordEncoder.matches(cmd.currentPassword(), cred.getPasswordHash())) {
                return new ActualizarCredencialesResponse(
                        cmd.idCliente(), "FAILURE", "Contraseña actual incorrecta"
                );
            }
            // 3.2 Verificar fuerza de la nueva contraseña
            if (!PASSWORD_PATTERN.matcher(cmd.newPassword()).matches()) {
                return new ActualizarCredencialesResponse(
                        cmd.idCliente(), "FAILURE",
                        "La nueva contraseña debe tener ≥8 caracteres, " +
                                "al menos una mayúscula, una minúscula, un dígito y un caracter especial"
                );
            }
            // 3.3 Verificar confirmación
            if (!cmd.newPassword().equals(cmd.confirmPassword())) {
                return new ActualizarCredencialesResponse(
                        cmd.idCliente(), "FAILURE", "La confirmación de la nueva contraseña no coincide"
                );
            }
            // 3.4 Aplicar hashing y asignar
            cred.setPasswordHash(passwordEncoder.encode(cmd.newPassword()));
            cambiado = true;
        }

        // 4. Si no se pidió ningún cambio
        if (!cambiado) {
            return new ActualizarCredencialesResponse(
                    cmd.idCliente(), "FAILURE", "No se indicó ningún dato para actualizar"
            );
        }

        // 5. Actualizar fecha y guardar
        cred.setUltimaActualizacion(LocalDateTime.now());
        credRepo.save(cred);

        return new ActualizarCredencialesResponse(
                cmd.idCliente(), "SUCCESS", "Credenciales actualizadas correctamente"
        );
    }
}

//1. Buscar credencial por ID de cliente
//
//Credencial cred = credRepo.findByClienteId(cmd.idCliente())
//        .orElseThrow(() -> new NoSuchElementException(
//                "No se encontraron credenciales para cliente id=" + cmd.idCliente()
//        ));
//
//Se obtiene el objeto Credencial asociado al idCliente proporcionado en el comando.
//
//Si no se encuentra, lanza excepción → probablemente se transforma en un error HTTP 404 en la API.
//
//🔄 2. Actualizar username (si se proporciona)
//
//if (cmd.username() != null && !cmd.username().equals(cred.getUsername())) {
//
//Si el nuevo username es distinto del actual, se intenta actualizar.
//
//        Pero primero, se valida que no esté en uso por otro usuario:
//
//        if (credRepo.findByUsername(cmd.username()).isPresent()) {
//        return ... "El nombre de usuario ya está en uso"
//        }
//
//Si está libre, se actualiza el campo.
//
//        🔒 3. Actualizar contraseña (si se proporciona newPassword)
//3.1 Verifica que vengan también:
//
//La contraseña actual (currentPassword)
//
//La confirmación (confirmPassword)
//
//Si falta alguna → ❌ error inmediato.
//        3.2 Comprueba que la contraseña actual sea válida
//
//if (!passwordEncoder.matches(cmd.currentPassword(), cred.getPasswordHash()))
//
//Usa PasswordEncoder para comparar la actual con la que está hasheada en base de datos.
//
//Si no coinciden → error de autenticación.
//
//3.3 Valida la fortaleza de la nueva contraseña
//
//"^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,}$"
//
//Significa:
//
//Al menos 8 caracteres
//
//Al menos una minúscula
//
//Al menos una mayúscula
//
//Al menos un número
//
//Al menos un caracter especial (como @#$%!, etc.)
//
//3.4 Verifica que la confirmación coincida
//
//if (!cmd.newPassword().equals(cmd.confirmPassword()))
//
//        3.5 Si todo está bien → actualiza el hash de la contraseña
//
//cred.setPasswordHash(passwordEncoder.encode(cmd.newPassword()));
//
//        ⛔ 4. Si no se pidió cambiar ni username ni password
//
//Devuelve error: "No se indicó ningún dato para actualizar"
//        💾 5. Guarda cambios en base de datos
//
//Actualiza el campo ultimaActualizacion
//
//Persiste con credRepo.save(...)
//
//✅ Resultado final
//
//Se devuelve una respuesta que indica:
//
//ID del cliente
//
//    "SUCCESS" o "FAILURE"
//
//Mensaje explicativo
//
//🧠 ¿Quién usaría este caso de uso?
//
//El cliente desde su perfil (frontend) si quiere:
//
//Cambiar nombre de usuario
//
//Cambiar contraseña
//
//💡 El frontend debería:
//
//Mostrar el formulario precargado
//
//Validar mínimamente los campos antes de enviar
//
//Mostrar los mensajes de respuesta devueltos desde este servicio