package com.example.lime.task;

import com.example.lime.task.dto.request.CreateTaskRequest;
import com.example.lime.task.dto.request.UpdateTaskRequest;
import com.example.lime.task.enums.FilterType;
import com.example.lime.task.enums.SortType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest request){
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(
            @RequestParam(required = false) FilterType filter,
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "PRIORITY") SortType sort) {
        return ResponseEntity.ok(taskService.getTasks(sort, filter, value));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,@Valid @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
