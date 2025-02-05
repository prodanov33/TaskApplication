package com.example.lime.task.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdateTaskRequest {

    @Size(min = 3, message = "Title must be at least 3 characters long")
    @NotNull
    private String title;

    @Size(min = 3, message = "Description must be at least 3 characters long")
    @NotNull
    private String description;

    @NotNull
    private LocalDate dueDate;

    private boolean isCompleted;

    private boolean isCritical;
}
