package com.example.lime.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class AuditListener {

    @PrePersist
    public void setCreatedOn(Object entity) {
        if (entity instanceof Auditable auditable) {
            auditable.setCreatedDate(LocalDateTime.now());
            auditable.setCreatedBy(getCurrentUser());
        }
    }

    @PreUpdate
    public void setUpdatedOn(Object entity) {
        if (entity instanceof Auditable auditable) {
            auditable.setLastModifiedDate(LocalDateTime.now());
            auditable.setLastModifiedBy(getCurrentUser());
        }
    }

    private String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
