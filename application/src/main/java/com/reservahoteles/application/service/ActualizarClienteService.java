package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.ActualizarClienteCommand;
import com.reservahoteles.application.dto.ActualizarClienteResponse;
import com.reservahoteles.application.port.in.ActualizarClienteUseCase;
import com.reservahoteles.domain.entity.Cliente;
import com.reservahoteles.domain.port.out.ClienteRepository;
import com.reservahoteles.common.enums.TipoIdentificacion;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@Service
public class ActualizarClienteService implements ActualizarClienteUseCase {

    private final ClienteRepository clienteRepo;

    // Regex muy simple para validar emails
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public ActualizarClienteService(ClienteRepository clienteRepo) {
        this.clienteRepo = clienteRepo;
    }

    @Override
    public ActualizarClienteResponse ejecutar(ActualizarClienteCommand cmd) {
        // 1. Comprobar que el cliente existe
        Cliente existente = clienteRepo.findById(cmd.idCliente())
                .orElseThrow(() ->
                        new NoSuchElementException("No se encontró el cliente con id = " + cmd.idCliente())
                );

        // 2. Validar y actualizar email si viene en el comando
        if (cmd.email() != null && !cmd.email().equalsIgnoreCase(existente.getEmail())) {
            if (!EMAIL_PATTERN.matcher(cmd.email()).matches()) {
                return new ActualizarClienteResponse(
                        cmd.idCliente(), "FAILURE", "Formato de email inválido"
                );
            }
            List<Cliente> porEmail = clienteRepo.buscarPorCriterios(cmd.email(), null, null, null);
            boolean emailEnUso = porEmail.stream()
                    .anyMatch(c -> !c.getId().equals(cmd.idCliente()));
            if (emailEnUso) {
                return new ActualizarClienteResponse(
                        cmd.idCliente(), "FAILURE", "Email ya registrado para otro cliente"
                );
            }
            existente.setEmail(cmd.email());
        }

        // 3. Validar y actualizar identificación si viene
        if (cmd.identificacion() != null && !cmd.identificacion().equals(existente.getIdentificacion())) {
            List<Cliente> porIden = clienteRepo.buscarPorCriterios(null, cmd.identificacion(), null, null);
            boolean idenEnUso = porIden.stream()
                    .anyMatch(c -> !c.getId().equals(cmd.idCliente()));
            if (idenEnUso) {
                return new ActualizarClienteResponse(
                        cmd.idCliente(), "FAILURE", "La identificación ya está en uso"
                );
            }
            existente.setIdentificacion(cmd.identificacion());
        }

        // 4. Actualizar el resto de campos si vienen
        if (cmd.nombre() != null) {
            existente.setNombre(cmd.nombre());
        }
        if (cmd.telefono() != null) {
            existente.setTelefono(cmd.telefono());
        }
        if (cmd.direccion() != null) {
            existente.setDireccion(cmd.direccion());
        }
        if (cmd.tipoIdentificacion() != null) {
            existente.setTipoIdentificacion(cmd.tipoIdentificacion());
        }

        // 5. Actualizar la fecha de última modificación
        existente.setUltimaActualizacion(LocalDateTime.now());

        // 6. Persistir los cambios
        clienteRepo.save(existente);

        // 7. Devolver respuesta
        return new ActualizarClienteResponse(
                cmd.idCliente(),
                "SUCCESS",
                "Cliente actualizado correctamente"
        );
    }
}

//Recibe el comando
//La UI muestra un formulario con los campos del cliente (nombre, email, teléfono, dirección, identificación, etc.). Cuando el usuario (cliente o admin) lo envía, en el backend cae un ActualizarClienteCommand con todos esos valores (algunos pueden llegar como null si el formulario los deja opcionales).
//
//Comprueba que el cliente existe
//Se llama a clienteRepo.findById(idCliente); si no hay ninguna fila con ese ID, lanza NoSuchElementException y la UI recibe un error “Cliente no encontrado”.
//
//Valída y actualiza el email
//
//Por qué el email: en la mayoría de aplicaciones web el email es el identificador de login y suele ser único, por eso comprobamos antes de guardar que:
//
//Formato: que tenga forma usuario@dominio.xxx.
//
//        Unicidad: que no haya otro cliente con ese mismo email (llamamos a buscarPorCriterios(email, …) y nos aseguramos de que, si devuelve resultados, ninguno tenga un ID distinto al actual).
//Si alguno de estos chequeos falla, devolvemos una respuesta con status = “FAILURE” y un mensaje claro.
//
//Valída y actualiza la identificación
//De forma muy parecida comprobamos que la nueva cédula/DNI/Pasaporte no esté ya asignada a otro cliente.
//
//Actualiza el resto de campos
//Para cada campo opcional (nombre, telefono, direccion, tipoIdentificacion), si no viene null en el comando, lo sobreescribimos en el objeto Cliente recuperado.
//
//Marca la fecha de modificación
//Ponemos ultimaActualizacion = LocalDateTime.now() para que quede registro de cuándo se hizo el cambio.
//
//Persiste los cambios
//Llamamos a clienteRepo.save(...), que en infra hará el UPDATE correspondiente en la base de datos.
//
//Devuelve un DTO de respuesta
//Con status = "SUCCESS" y un mensaje. La UI lo recibe y puede, por ejemplo, mostrar “Tus datos se han actualizado correctamente.”