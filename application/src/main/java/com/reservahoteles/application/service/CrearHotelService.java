package com.reservahoteles.application.service;


import com.reservahoteles.application.dto.CrearHotelCommand;
import com.reservahoteles.application.dto.CrearHotelResponse;
import com.reservahoteles.application.port.in.CrearHotelUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Hotel;
import com.reservahoteles.domain.port.out.HotelRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class CrearHotelService implements CrearHotelUseCase {

    private final HotelRepository hotelRepo;

    // Email muy básico
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    // Teléfono muy básico: dígitos, espacios, +, -, ()
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9+()\\-\\s]{4,20}$");

    public CrearHotelService(HotelRepository hotelRepo) {
        this.hotelRepo = hotelRepo;
    }

    @Override
    public CrearHotelResponse ejecutar(CrearHotelCommand cmd) {
        // 1) Solo ADMIN puede crear
        if (cmd.rolActor() != Rol.ADMINISTRADOR) {
            return new CrearHotelResponse(null, "FAILURE",
                    "Solo administradores pueden crear hoteles");
        }

        // 2) Validar campos obligatorios
        if (cmd.nombre() == null || cmd.nombre().isBlank()
                || cmd.direccion() == null || cmd.direccion().isBlank()
                || cmd.pais() == null || cmd.pais().isBlank()
                || cmd.ciudad() == null || cmd.ciudad().isBlank()
                || cmd.numeroEstrellas() == null
                || cmd.email() == null || cmd.email().isBlank()) {
            return new CrearHotelResponse(null, "FAILURE",
                    "Nombre, dirección, país, ciudad, estrellas y email son obligatorios");
        }

        // 3) Validar rango de estrellas
        int estrellas = cmd.numeroEstrellas();
        if (estrellas < 1 || estrellas > 5) {
            return new CrearHotelResponse(null, "FAILURE",
                    "Número de estrellas debe estar entre 1 y 5");
        }

        // 4) Validar email
        if (!EMAIL_PATTERN.matcher(cmd.email()).matches()) {
            return new CrearHotelResponse(null, "FAILURE",
                    "Formato de email inválido");
        }

        // 5) Validar teléfono si viene
        if (cmd.telefono() != null && !cmd.telefono().isBlank()
                && !PHONE_PATTERN.matcher(cmd.telefono()).matches()) {
            return new CrearHotelResponse(null, "FAILURE",
                    "Formato de teléfono inválido");
        }

        // 6) Validar latitud/longitud si vienen
        if (cmd.latitud() != null) {
            double lat = cmd.latitud();
            if (lat < -90.0 || lat > 90.0) {
                return new CrearHotelResponse(null, "FAILURE",
                        "Latitud debe estar entre -90 y 90");
            }
        }
        if (cmd.longitud() != null) {
            double lon = cmd.longitud();
            if (lon < -180.0 || lon > 180.0) {
                return new CrearHotelResponse(null, "FAILURE",
                        "Longitud debe estar entre -180 y 180");
            }
        }

        // 7) Unicidad de email
        List<Hotel> ya = hotelRepo.buscarPorCriteriosAvanzados(
                null, null, null, null,
                null, null, cmd.email()
        );
        if (!ya.isEmpty()) {
            return new CrearHotelResponse(null, "FAILURE",
                    "Ya existe un hotel con ese email");
        }

        // 8) Construir entidad de dominio
        Hotel hotel = Hotel.builder()
                .nombre(cmd.nombre().trim())
                .direccion(cmd.direccion().trim())
                .pais(cmd.pais().trim())
                .ciudad(cmd.ciudad().trim())
                .latitud(cmd.latitud())
                .longitud(cmd.longitud())
                .numeroEstrellas(estrellas)
                .telefono(cmd.telefono()!=null ? cmd.telefono().trim() : null)
                .email(cmd.email().trim().toLowerCase())
                .fechaRegistro(LocalDateTime.now())
                .build();

        // 9) Persistir
        Hotel guardado = hotelRepo.save(hotel);

        // 10) Responder
        return new CrearHotelResponse(
                guardado.getId(),
                "SUCCESS",
                "Hotel creado con id = " + guardado.getId()
        );
    }
}