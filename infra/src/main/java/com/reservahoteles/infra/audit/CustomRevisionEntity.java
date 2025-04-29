package com.reservahoteles.infra.audit;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@Table(name = "REVINFO")
@RevisionEntity(CustomRevisionListener.class)
@Data
public class CustomRevisionEntity {
    @Id @GeneratedValue
    @RevisionNumber
    private int id;

    @RevisionTimestamp
    private long timestamp;

    // campo extra para guardar, p. ej., el username del que hizo la revisión
    private String username;
}