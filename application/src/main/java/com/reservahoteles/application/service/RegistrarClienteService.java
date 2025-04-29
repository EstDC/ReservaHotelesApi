package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.RegistrarClienteCommand;
import com.reservahoteles.application.dto.RegistrarClienteResponse;
import com.reservahoteles.application.port.in.RegistrarClienteUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Cliente;
import com.reservahoteles.domain.entity.Credencial;
import com.reservahoteles.domain.port.out.ClienteRepository;
import com.reservahoteles.domain.port.out.CredencialRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
public class RegistrarClienteService implements RegistrarClienteUseCase {

    private final ClienteRepository clienteRepo;
    private final CredencialRepository credRepo;
    private final PasswordEncoder passwordEncoder;

    // validaciones
    private static final Pattern EMAIL_RX = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern ID_RX    = Pattern.compile("^[A-Za-z0-9\\-]+$");
    private static final Pattern USER_RX  = Pattern.compile("^[A-Za-z0-9_\\.]{4,}$");
    private static final Pattern PASS_RX  = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W]).{8,}$"
    );

    public RegistrarClienteService(ClienteRepository clienteRepo,
                                   CredencialRepository credRepo,
                                   PasswordEncoder passwordEncoder) {
        this.clienteRepo     = clienteRepo;
        this.credRepo        = credRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegistrarClienteResponse ejecutar(RegistrarClienteCommand cmd) {
        // 1) campos obligatorios
        if (cmd.nombre() == null || cmd.nombre().isBlank()) {
            return fail("El nombre es obligatorio");
        }
        if (cmd.email() == null || !EMAIL_RX.matcher(cmd.email()).matches()) {
            return fail("Email inválido");
        }
        if (cmd.tipoIdentificacion() == null) {
            return fail("Tipo de identificación obligatorio");
        }
        if (cmd.identificacion() == null || !ID_RX.matcher(cmd.identificacion()).matches()) {
            return fail("Identificación inválida");
        }
        // username/password
        if (cmd.username() == null || !USER_RX.matcher(cmd.username()).matches()) {
            return fail("Username inválido (mínimo 4 caracteres alfanum/._)");
        }
        if (cmd.password() == null || !PASS_RX.matcher(cmd.password()).matches()) {
            return fail("Password débil (mín 8 caracteres, incluye mayúscula, minúscula, dígito y especial)");
        }

        // 2) unicidad en Cliente
        if (!clienteRepo.buscarPorCriterios(cmd.email(), null, null, null).isEmpty()) {
            return fail("Ya existe un cliente con ese email");
        }
        if (!clienteRepo.buscarPorCriterios(null, cmd.identificacion(), null, null).isEmpty()) {
            return fail("Ya existe un cliente con esa identificación");
        }
        // 3) unicidad en credenciales
        if (credRepo.findByUsername(cmd.username()).isPresent()) {
            return fail("El username ya está en uso");
        }

        // 4) crear dominio Cliente
        Cliente cl = Cliente.builder()
                .nombre(cmd.nombre().trim())
                .email(cmd.email().trim().toLowerCase())
                .telefono(cmd.telefono()!=null ? cmd.telefono().trim() : null)
                .direccion(cmd.direccion()!=null ? cmd.direccion().trim() : null)
                .tipoIdentificacion(cmd.tipoIdentificacion())
                .identificacion(cmd.identificacion().trim())
                .rol(Rol.CLIENTE)
                .activo(true)
                .fechaRegistro(LocalDateTime.now())
                .ultimaActualizacion(LocalDateTime.now())
                .build();

        Cliente guardado = clienteRepo.save(cl);

        // 5) crear Credencial
        Credencial cr = Credencial.builder()
                .idCliente(guardado.getId())
                .username(cmd.username().trim())
                .passwordHash(passwordEncoder.encode(cmd.password()))
                .fechaCreacion(LocalDateTime.now())
                .ultimaActualizacion(LocalDateTime.now())
                .build();
        credRepo.save(cr);

        return new RegistrarClienteResponse(
                guardado.getId(),
                "SUCCESS",
                "Cliente y credenciales creados correctamente"
        );
    }

    private RegistrarClienteResponse fail(String msg) {
        return new RegistrarClienteResponse(null, "FAILURE", msg);
    }
}

//Valida todos los campos del formulario (cliente + credenciales).
//
//Comprueba unicidad de email/identificación en Cliente y unicidad de username en Credencial.
//
//Persiste primero el Cliente, luego crea y guarda el Credencial (con el hash de bcrypt).
//
//Responde con status y mensaje claros.