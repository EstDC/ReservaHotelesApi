package com.reservahoteles.domain.port.out;
import com.reservahoteles.domain.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


/**
 * Contrato para la persistencia y consulta de hoteles.
 * Nota: Los métodos que modifican el estado del sistema (crear, actualizar,
 * asignar servicios, eliminar/desactivar) deben ser invocados solo por administradores.
 */
public interface HotelRepository {

    /**
     * Persiste un hotel (crea o actualiza).
     * Este método debe usarse únicamente por administradores.
     *
     * @param hotel entidad hotel.
     * @return el hotel persistido.
     */
    Hotel save(Hotel hotel);

    /**
     * Busca un hotel por su identificador.
     *
     * @param id identificador del hotel.
     * @return un Optional con el hotel si se encontró.
     */
    Optional<Hotel> findById(Long id);

    /**
     * Devuelve la lista de todos los hoteles registrados.
     *
     * @return lista de hoteles.
     */
    List<Hotel> findAll();

    /**
     * Busca hoteles aplicando filtros básicos opcionales.
     * Parámetros:
     * - pais: Filtra por país (opcional).
     * - ciudad: Filtra por ciudad (opcional).
     * - numeroEstrellas: Filtra por número de estrellas (opcional).
     * - nombre: Filtra por un nombre parcial (opcional).
     *
     * @param pais            el país del hotel (opcional).
     * @param ciudad          la ciudad del hotel (opcional).
     * @param numeroEstrellas el número de estrellas (opcional).
     * @param nombre          el nombre (o parte del nombre) del hotel (opcional).
     * @return lista de hoteles que cumplan con los criterios.
     */
    List<Hotel> buscarPorCriterios(String pais, String ciudad, Integer numeroEstrellas, String nombre);

    /**
     * Busca hoteles aplicando un conjunto ampliado de criterios.
     * Esto puede ser útil en consultas avanzadas para la administración.
     * Parámetros:
     * - pais: Filtra por país (opcional).
     * - ciudad: Filtra por ciudad (opcional).
     * - numeroEstrellas: Filtra por número de estrellas (opcional).
     * - nombre: Filtra por nombre (opcional).
     * - direccion: Filtra por dirección (opcional).
     * - telefono: Filtra por teléfono (opcional).
     * - email: Filtra por email (opcional).
     *
     * @param pais            el país del hotel (opcional).
     * @param ciudad          la ciudad del hotel (opcional).
     * @param numeroEstrellas el número de estrellas (opcional).
     * @param nombre          el nombre (o parte del nombre) del hotel (opcional).
     * @param direccion       la dirección del hotel (opcional).
     * @param telefono        el teléfono del hotel (opcional).
     * @param email           el email del hotel (opcional).
     * @return lista de hoteles que cumplen con todos los criterios.
     */
    List<Hotel> buscarPorCriteriosAvanzados(String pais, String ciudad, Integer numeroEstrellas,
                                            String nombre, String direccion, String telefono, String email);

    Page<Hotel> buscarPorCriteriosAvanzados(
            String pais,
            String ciudad,
            Integer numeroEstrellas,
            String nombre,
            String direccion,
            String telefono,
            String email,
            Pageable pageable
    );


    /**
     * Elimina o desactiva un hotel.
     * Este método debe usarse únicamente por administradores.
     *
     * @param id identificador del hotel.
     */
    void eliminar(Long id);

    /**
     * Asigna o actualiza los servicios asociados a un hotel.
     * Se espera recibir una lista de identificadores de servicios.
     * Este método debe usarse únicamente por administradores.
     *
     * @param idHotel     identificador del hotel.
     * @param idsServicios lista de identificadores de servicios a asignar.
     */
    void asignarServicios(Long idHotel, List<Long> idsServicios);
}