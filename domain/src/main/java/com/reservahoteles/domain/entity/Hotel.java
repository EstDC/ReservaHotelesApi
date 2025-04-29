package com.reservahoteles.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    private Long id;
    private String nombre;
    private String direccion;
    private String pais;
    private String ciudad;
    private Double latitud;
    private Double longitud;
    private Integer numeroEstrellas;
    private String telefono;
    private String email;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimaActualizacion;

    // Para inicializar las listas en el builder, usa @Builder.Default
    @Builder.Default
    private List<Habitacion> habitaciones = new ArrayList<>();

    @Builder.Default
    private List<Servicio> servicios = new ArrayList<>();
}
