package com.reservahoteles.infra.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomRevisionListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity rev = (CustomRevisionEntity) revisionEntity;
        // obtiene el usuario “actual” de Spring Security
        String user = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        rev.setUsername(user);
    }
}