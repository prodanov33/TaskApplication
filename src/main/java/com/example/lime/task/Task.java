package com.example.lime.task;

import com.example.lime.audit.Auditable;
import com.example.lime.audit.AuditListener;
import com.example.lime.task.enums.Priority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "task")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditListener.class)
public class Task extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalDate dueDate;

    private Priority priority;

    private boolean isCompleted;
}
