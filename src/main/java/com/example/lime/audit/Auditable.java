package com.example.lime.audit;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class Auditable {

    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;
}
