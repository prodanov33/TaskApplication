package com.example.lime.task;

import com.example.lime.task.dto.request.CreateTaskRequest;
import com.example.lime.task.dto.request.UpdateTaskRequest;
import com.example.lime.task.enums.FilterType;
import com.example.lime.task.enums.Priority;
import com.example.lime.task.enums.SortType;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private CreateTaskRequest createTaskRequest;
    private UpdateTaskRequest updateTaskRequest;
    private Task highPriorityTask;
    private Task lowPriorityTask;
    private Task dueTodayTask;
    private Task dueLaterTask;

    @BeforeEach
    void setUp() {
        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .dueDate(LocalDate.now().plusDays(5))
                .isCompleted(false)
                .priority(Priority.MEDIUM)
                .build();

        createTaskRequest = new CreateTaskRequest("Test Task", "Test Description", LocalDate.now().plusDays(5), false);
        updateTaskRequest = new UpdateTaskRequest("Updated Task", "Updated Description", LocalDate.now().plusDays(3), true, false);

        highPriorityTask = Task.builder()
                .id(1L)
                .title("High Priority Task")
                .description("Urgent task")
                .dueDate(LocalDate.now().plusDays(2))
                .isCompleted(false)
                .priority(Priority.HIGH)
                .build();

        lowPriorityTask = Task.builder()
                .id(2L)
                .title("Low Priority Task")
                .description("Not urgent")
                .dueDate(LocalDate.now().plusDays(10))
                .isCompleted(false)
                .priority(Priority.LOW)
                .build();

        dueTodayTask = Task.builder()
                .id(3L)
                .title("Task Due Today")
                .description("Needs to be done today")
                .dueDate(LocalDate.now())
                .isCompleted(false)
                .priority(Priority.MEDIUM)
                .build();

        dueLaterTask = Task.builder()
                .id(4L)
                .title("Task Due Later")
                .description("Can be done later")
                .dueDate(LocalDate.now().plusDays(5))
                .isCompleted(false)
                .priority(Priority.LOW)
                .build();
    }

    @Test
    void createTask_ShouldReturnSavedTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(createTaskRequest);

        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getTitle()).isEqualTo(createTaskRequest.getTitle());
        assertThat(createdTask.getDescription()).isEqualTo(createTaskRequest.getDescription());
        assertThat(createdTask.getPriority()).isEqualTo(Priority.MEDIUM);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getTasks_ShouldReturnFilteredTasks() {
        when(taskRepository.findAll(any(Sort.class))).thenReturn(List.of(task));

        List<Task> tasks = taskService.getTasks(SortType.PRIORITY, null, null);

        assertThat(tasks).isNotEmpty();
        assertThat(tasks.size()).isEqualTo(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo(task.getTitle());
        verify(taskRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void getTask_ShouldReturnTask_WhenExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTask(1L);

        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getId()).isEqualTo(1L);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTask_ShouldThrowException_WhenNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> taskService.getTask(1L));

        assertThat(exception.getMessage()).isEqualTo("Task with ID 1 not found");
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask_WhenExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTask(1L, updateTaskRequest);

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getTitle()).isEqualTo(updateTaskRequest.getTitle());
        assertThat(updatedTask.getDescription()).isEqualTo(updateTaskRequest.getDescription());
        assertThat(updatedTask.getPriority()).isEqualTo(Priority.LOW);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void updateTask_ShouldThrowException_WhenNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> taskService.updateTask(1L, updateTaskRequest));

        assertThat(exception.getMessage()).isEqualTo("Task with ID 1 not found");
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void deleteTask_ShouldDeleteTask_WhenExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void deleteTask_ShouldThrowException_WhenNotFound() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> taskService.deleteTask(1L));

        assertThat(exception.getMessage()).isEqualTo("Task with ID 1 not found");
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void calculatePriority_ShouldReturnCorrectPriority() {
        LocalDate today = LocalDate.now();
        assertThat(taskService.calculatePriority(false, false, today.minusDays(1))).isEqualTo(Priority.HIGH);
        assertThat(taskService.calculatePriority(false, false, today.plusDays(3))).isEqualTo(Priority.MEDIUM);
        assertThat(taskService.calculatePriority(false, false, today.plusDays(10))).isEqualTo(Priority.LOW);
        assertThat(taskService.calculatePriority(true, false, today.plusDays(5))).isEqualTo(Priority.HIGH);
        assertThat(taskService.calculatePriority(false, true, today.plusDays(5))).isEqualTo(Priority.LOW);
    }

    @Test
    void getTasks_ShouldReturnTasksSortedByPriority() {
        when(taskRepository.findAll(any(Sort.class)))
                .thenReturn(List.of(highPriorityTask, dueTodayTask, dueLaterTask, lowPriorityTask));

        List<Task> tasks = taskService.getTasks(SortType.PRIORITY, null, null);

        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(4);
        assertThat(tasks.get(0).getPriority()).isEqualTo(Priority.HIGH);
        assertThat(tasks.get(3).getPriority()).isEqualTo(Priority.LOW);

        verify(taskRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void getTasks_ShouldReturnTasksSortedByDueDate() {
        when(taskRepository.findAll(any(Sort.class)))
                .thenReturn(List.of(dueTodayTask, dueLaterTask, highPriorityTask, lowPriorityTask));

        List<Task> tasks = taskService.getTasks(SortType.DUE_DATE, null, null);

        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(4);
        assertThat(tasks.get(0).getDueDate()).isEqualTo(LocalDate.now());
        assertThat(tasks.get(3).getDueDate()).isEqualTo(LocalDate.now().plusDays(10));

        verify(taskRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void getTasks_ShouldFilterByCompletionStatus() {
        when(taskRepository.findAllByIsCompleted(eq(false)))
                .thenReturn(List.of(dueTodayTask));

        List<Task> tasks = taskService.getTasks(SortType.PRIORITY, FilterType.IS_COMPLETED, "false");

        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).isCompleted()).isFalse();

        verify(taskRepository, times(1)).findAllByIsCompleted(eq(false));
    }

    @Test
    void getTasks_ShouldFilterByPriority() {
        when(taskRepository.findAllByPriority(eq(Priority.HIGH)))
                .thenReturn(List.of(highPriorityTask));

        List<Task> tasks = taskService.getTasks(SortType.PRIORITY, FilterType.PRIORITY, "HIGH");

        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getPriority()).isEqualTo(Priority.HIGH);

        verify(taskRepository, times(1)).findAllByPriority(eq(Priority.HIGH));
    }
}
