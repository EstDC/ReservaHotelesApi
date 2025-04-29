package com.reservahoteles.domain.entity;

import com.reservahoteles.common.enums.Rol;
import com.reservahoteles.common.enums.TipoIdentificacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String telefono;
    private String direccion;

    @Enumerated(EnumType.STRING)
    private TipoIdentificacion tipoIdentificacion;
    private String identificacion;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    @LastModifiedDate
    private LocalDateTime ultimaActualizacion;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Builder.Default
    private boolean activo = true;
}
