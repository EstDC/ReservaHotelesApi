package com.reservahoteles.application.service;

import com.reservahoteles.application.dto.ActualizarHotelCommand;
import com.reservahoteles.application.dto.ActualizarHotelResponse;
import com.reservahoteles.application.port.in.ActualizarHotelUseCase;
import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.domain.entity.Hotel;
import com.reservahoteles.domain.entity.Servicio;
import com.reservahoteles.domain.port.out.HotelRepository;
import com.reservahoteles.domain.port.out.ServicioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class ActualizarHotelService implements ActualizarHotelUseCase {

    private final HotelRepository     hotelRepo;
    private final ServicioRepository  servicioRepo;

    // expresiones regulares simples
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE_REGEX =
            Pattern.compile("^[+\\d][\\d\\s\\-]{6,}$");

    public ActualizarHotelService(HotelRepository hotelRepo,
                                  ServicioRepository servicioRepo) {
        this.hotelRepo     = hotelRepo;
        this.servicioRepo  = servicioRepo;
    }

    @Override
    public ActualizarHotelResponse ejecutar(ActualizarHotelCommand cmd) {

        /* 0 ── Solo ADMIN puede ejecutar */
        if (cmd.rolSolicitante() != Rol.ADMINISTRADOR) {
            return fail(cmd, "No tiene permisos para actualizar hoteles");
        }

        /* 1 ── Cargar hotel existente */
        Hotel hotel = hotelRepo.findById(cmd.idHotel())
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe el hotel con id = " + cmd.idHotel())
                );

        boolean cambiado = false;

        /* 2 ── Nombre */
        if (cmd.nombre() != null && !cmd.nombre().equalsIgnoreCase(hotel.getNombre())) {
            // unicidad por nombre + ciudad
            boolean nombreDuplicado = hotelRepo
                    .buscarPorCriteriosAvanzados(null, hotel.getCiudad(), null,
                            cmd.nombre(), null, null, null)
                    .stream()
                    .anyMatch(h -> !h.getId().equals(hotel.getId()));
            if (nombreDuplicado) {
                return fail(cmd, "Ya existe un hotel con ese nombre en la misma ciudad");
            }
            hotel.setNombre(cmd.nombre());
            cambiado = true;
        }

        /* 3 ── Dirección / ciudad / país */
        if (cmd.direccion() != null)   { hotel.setDireccion(cmd.direccion());   cambiado = true; }
        if (cmd.ciudad() != null)      { hotel.setCiudad(cmd.ciudad());         cambiado = true; }
        if (cmd.pais() != null)        { hotel.setPais(cmd.pais());             cambiado = true; }

        /* 4 ── Estrellas */
        if (cmd.numeroEstrellas() != null &&
                !Objects.equals(cmd.numeroEstrellas(), hotel.getNumeroEstrellas())) {

            int est = cmd.numeroEstrellas();
            if (est < 1 || est > 5) return fail(cmd, "El número de estrellas debe estar entre 1 y 5");
            hotel.setNumeroEstrellas(est);
            cambiado = true;
        }

        /* 5 ── Teléfono */
        if (cmd.telefono() != null && !cmd.telefono().equals(hotel.getTelefono())) {
            if (!PHONE_REGEX.matcher(cmd.telefono()).matches()) {
                return fail(cmd, "Formato de teléfono inválido");
            }
            hotel.setTelefono(cmd.telefono());
            cambiado = true;
        }

        /* 6 ── E‑mail (formato + unicidad) */
        if (cmd.email() != null && !cmd.email().equalsIgnoreCase(hotel.getEmail())) {
            if (!EMAIL_REGEX.matcher(cmd.email()).matches()) {
                return fail(cmd, "Formato de email inválido");
            }
            boolean emailDuplicado = hotelRepo
                    .buscarPorCriteriosAvanzados(null, null, null, null,
                            null, null, cmd.email())
                    .stream()
                    .anyMatch(h -> !h.getId().equals(hotel.getId()));
            if (emailDuplicado) {
                return fail(cmd, "El email ya está asignado a otro hotel");
            }
            hotel.setEmail(cmd.email());
            cambiado = true;
        }

        /* 7 ── Coordenadas */
        if (cmd.latitud() != null) {
            if (cmd.latitud() < -90 || cmd.latitud() > 90)
                return fail(cmd, "Latitud fuera de rango (-90..90)");
            hotel.setLatitud(cmd.latitud());
            cambiado = true;
        }
        if (cmd.longitud() != null) {
            if (cmd.longitud() < -180 || cmd.longitud() > 180)
                return fail(cmd, "Longitud fuera de rango (-180..180)");
            hotel.setLongitud(cmd.longitud());
            cambiado = true;
        }

        /* 8 ── Servicios (reemplazo completo) */
        if (cmd.idsServicios() != null) {
            List<Servicio> nuevosServicios = servicioRepo.findByIds(cmd.idsServicios());

            if (nuevosServicios.size() != cmd.idsServicios().size()) {
                return fail(cmd, "Alguno de los servicios indicados no existe");
            }
            hotel.setServicios(nuevosServicios);
            cambiado = true;
        }

        /* 9 ── Nada cambió */
        if (!cambiado) {
            return fail(cmd, "No se indicó ningún dato para actualizar");
        }

        /* 10 ── Guardar */
        hotel.setFechaRegistro(
                hotel.getFechaRegistro() == null ? LocalDateTime.now() : hotel.getFechaRegistro());
        hotelRepo.save(hotel);

        return new ActualizarHotelResponse(
                hotel.getId(), "SUCCESS", "Hotel actualizado correctamente");
    }

    // utilidades -------------------------------------------------------------

    private ActualizarHotelResponse fail(ActualizarHotelCommand cmd, String msg) {
        return new ActualizarHotelResponse(cmd.idHotel(), "FAILURE", msg);
    }
}