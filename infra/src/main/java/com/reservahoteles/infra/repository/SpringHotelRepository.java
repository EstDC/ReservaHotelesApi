package com.reservahoteles.infra.repository;

import com.reservahoteles.infra.entity.HotelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpringHotelRepository extends JpaRepository<HotelEntity, Long> {

    // Métodos derivados básicos
    List<HotelEntity> findByPaisIgnoreCase(String pais);

    List<HotelEntity> findByCiudadIgnoreCase(String ciudad);

    List<HotelEntity> findByNumeroEstrellas(Integer numeroEstrellas);

    List<HotelEntity> findByNombreContainingIgnoreCase(String nombre);

    // Consulta personalizada para combinar varios filtros (parámetros opcionales)
    @Query("SELECT h FROM HotelEntity h " +
            "WHERE (:pais IS NULL OR LOWER(h.pais) LIKE LOWER(CONCAT('%', :pais, '%'))) " +
            "AND (:ciudad IS NULL OR LOWER(h.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))) " +
            "AND (:numeroEstrellas IS NULL OR h.numeroEstrellas = :numeroEstrellas) " +
            "AND (:nombre IS NULL OR LOWER(h.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))")
    List<HotelEntity> buscarPorCriterios(@Param("pais") String pais,
                                                  @Param("ciudad") String ciudad,
                                                  @Param("numeroEstrellas") Integer numeroEstrellas,
                                                  @Param("nombre") String nombre);


    @Query("SELECT h FROM HotelEntity h " +
            "WHERE (:pais IS NULL OR LOWER(h.pais) LIKE LOWER(CONCAT('%', :pais, '%'))) " +
            "AND (:ciudad IS NULL OR LOWER(h.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))) " +
            "AND (:numeroEstrellas IS NULL OR h.numeroEstrellas = :numeroEstrellas) " +
            "AND (:nombre IS NULL OR LOWER(h.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
            "AND (:direccion IS NULL OR LOWER(h.direccion) LIKE LOWER(CONCAT('%', :direccion, '%'))) " +
            "AND (:telefono IS NULL OR h.telefono = :telefono) " +
            "AND (:email IS NULL OR LOWER(h.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    List<HotelEntity> buscarPorCriteriosAvanzados(@Param("pais") String pais,
                                                  @Param("ciudad") String ciudad,
                                                  @Param("numeroEstrellas") Integer numeroEstrellas,
                                                  @Param("nombre") String nombre,
                                                  @Param("direccion") String direccion,
                                                  @Param("telefono") String telefono,
                                                  @Param("email") String email);

    @Query("SELECT h FROM HotelEntity h " +
            "WHERE (:pais IS NULL OR LOWER(h.pais) LIKE LOWER(CONCAT('%', :pais, '%'))) " +
            "  AND (:ciudad IS NULL OR LOWER(h.ciudad) LIKE LOWER(CONCAT('%', :ciudad, '%'))) " +
            "  AND (:numeroEstrellas IS NULL OR h.numeroEstrellas = :numeroEstrellas) " +
            "  AND (:nombre IS NULL OR LOWER(h.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
            "  AND (:direccion IS NULL OR LOWER(h.direccion) LIKE LOWER(CONCAT('%', :direccion, '%'))) " +
            "  AND (:telefono IS NULL OR h.telefono = :telefono) " +
            "  AND (:email IS NULL OR LOWER(h.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    Page<HotelEntity> buscarPorCriteriosAvanzados(
            @Param("pais") String pais,
            @Param("ciudad") String ciudad,
            @Param("numeroEstrellas") Integer numeroEstrellas,
            @Param("nombre") String nombre,
            @Param("direccion") String direccion,
            @Param("telefono") String telefono,
            @Param("email") String email,
            Pageable pageable
    );
}