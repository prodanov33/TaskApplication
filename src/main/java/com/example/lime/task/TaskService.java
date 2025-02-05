package com.example.lime.task;

import com.example.lime.task.dto.request.CreateTaskRequest;
import com.example.lime.task.dto.request.UpdateTaskRequest;
import com.example.lime.task.enums.FilterType;
import com.example.lime.task.enums.Priority;
import com.example.lime.task.enums.SortType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    Task createTask(CreateTaskRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .isCompleted(false)
                .dueDate(request.getDueDate())
                .priority(calculatePriority(request.isCritical(), false,request.getDueDate()))
                .build();

        return taskRepository.save(task);
    }
    public List<Task> getTasks(SortType sortBy, FilterType filter, String value) {

        if (filter != null && value != null) {
            return switch (filter) {
                case IS_COMPLETED -> taskRepository.findAllByIsCompleted(Boolean.parseBoolean(value));
                case PRIORITY -> taskRepository.findAllByPriority(Priority.valueOf(value));
            };
        }
        Sort sort = Sort.by(sortBy.getStringValue());
        return taskRepository.findAll(sort);
    }
    public Task getTask(Long id) {
       return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task with ID " + id + " not found"));
    }
    public void deleteTask(Long id) {
        taskRepository.delete(taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with ID " + id + " not found")));
    }
    public Task updateTask(Long id, UpdateTaskRequest request) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(request.getTitle());
                    task.setDescription(request.getDescription());
                    task.setDueDate(request.getDueDate());
                    task.setCompleted(request.isCompleted());
                    task.setPriority(calculatePriority(request.isCritical(),request.isCompleted(),request.getDueDate()));
                    return taskRepository.save(task);
                }).orElseThrow(() -> new EntityNotFoundException("Task with ID " + id + " not found"));
    }
    protected Priority calculatePriority(boolean isCritical, boolean isCompleted, LocalDate dueDate) {
        if (isCompleted) return Priority.LOW;
        if (isCritical) return Priority.HIGH;

        long daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
        return daysUntilDue <= 0 ? Priority.HIGH :
                daysUntilDue <= 7 ? Priority.MEDIUM : Priority.LOW;
    }

}
