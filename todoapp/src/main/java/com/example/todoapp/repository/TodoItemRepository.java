package com.example.todoapp.repository;

import com.example.todoapp.model.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    List<TodoItem> findByCompletedTrue();
    List<TodoItem> findByCompletedFalse();

    @Query("SELECT t FROM TodoItem t ORDER BY " +
            "CASE WHEN t.completed = true THEN 1 ELSE 0 END, " +
            "CASE WHEN t.targetCompletionDate IS NULL THEN 1 ELSE 0 END, " +
            "t.targetCompletionDate ASC, t.createdAt ASC")
    List<TodoItem> findAllOrderedByCompletionAndDate();
}
