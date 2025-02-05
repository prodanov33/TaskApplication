package com.example.lime.task;

import com.example.lime.task.enums.Priority;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByIsCompleted(boolean isCompleted);

    List<Task> findAllByPriority(Priority priority);
}
