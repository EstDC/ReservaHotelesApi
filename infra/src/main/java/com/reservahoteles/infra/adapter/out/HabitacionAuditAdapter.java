package com.reservahoteles.infra.adapter.out;

import com.reservahoteles.application.dto.RevisionHabitacionDTO;
import com.reservahoteles.application.port.out.HabitacionAuditPort;
import com.reservahoteles.infra.entity.HabitacionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HabitacionAuditAdapter implements HabitacionAuditPort {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<RevisionHabitacionDTO> findRevisions(Long idHabitacion) {
        AuditReader reader = AuditReaderFactory.get(em);
        List<Number> revs = reader.getRevisions(HabitacionEntity.class, idHabitacion);

        return revs.stream()
                .map(rev -> {
                    HabitacionEntity hist = reader.find(HabitacionEntity.class, idHabitacion, rev);
                    Date revDate = reader.getRevisionDate(rev);
                    return new RevisionHabitacionDTO(
                            rev.longValue(),
                            LocalDateTime.ofInstant(revDate.toInstant(), ZoneId.systemDefault()),
                            hist.getEstado(),
                            hist.getTipo(),
                            hist.getPrecioPorNoche()
                    );
                })
                .collect(Collectors.toList());
    }
}