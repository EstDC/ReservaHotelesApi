package com.reservahoteles.infra.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"habitaciones", "servicios"})
@Table(name = "hoteles")
public class HotelEntity {

    /* ──────────── PK ──────────── */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hotel")
    private Long id;

    /* ───────── Datos básicos ───── */
    @Column(name = "nombre",    nullable = false, length = 100)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Column(name = "pais",      nullable = false, length = 50)
    private String pais;

    @Column(name = "ciudad",    nullable = false, length = 50)
    private String ciudad;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "numero_estrellas", nullable = false)
    private Integer numeroEstrellas;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /* ──────── Timestamps ───────── */
    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @UpdateTimestamp
    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    /* ─────── Relaciones ────────── */

    /** 1‑N habitaciones (lado padre) */
    @OneToMany(mappedBy     = "hotelEntity",
            cascade      = CascadeType.ALL,
            orphanRemoval= true,
            fetch        = FetchType.LAZY)
    @Builder.Default
    private List<HabitacionEntity> habitaciones = new ArrayList<>();

    /** N‑M servicios ofertados */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name               = "hotel_servicios",
            joinColumns        = @JoinColumn(name = "id_hotel"),
            inverseJoinColumns = @JoinColumn(name = "id_servicio"))
    @Builder.Default
    private List<ServicioEntity> servicios = new ArrayList<>();

    /* ── Convenience methods (sin cambiar nombres) ── */
    public void addServicio(ServicioEntity srv) {
        if (srv == null || servicios.contains(srv)) return;
        servicios.add(srv);
        srv.getHoteles().add(this);          // sincroniza lado inverso
    }

    public void removeServicio(ServicioEntity srv) {
        if (srv == null) return;
        if (servicios.remove(srv)) {
            srv.getHoteles().remove(this);   // sincroniza lado inverso
        }
    }
}