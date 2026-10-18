package com.example.todoapp;

import com.example.todoapp.model.TodoItem;
import com.example.todoapp.repository.TodoItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TodoappApplicationTests {

    @Autowired private MockMvc mockMvc;
    @Autowired private TodoItemRepository todoItemRepository;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void cleanDatabase() {
        todoItemRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void createTodoReturnsCreatedItem() throws Exception {
        Map<String, Object> request = Map.of(
                "title", "Test task",
                "description", "Created by integration test",
                "targetCompletionDate", "2026-12-31T23:59:59"
        );

        mockMvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title", is("Test task")))
                .andExpect(jsonPath("$.description", is("Created by integration test")))
                .andExpect(jsonPath("$.completed", is(false)));
    }

    @Test
    void createTodoRejectsBlankTitle() throws Exception {
        mockMvc.perform(post("/api/todos").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("title", "   "))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.title").exists());
    }

    @Test
    void getAllTodosReturnsSavedItems() throws Exception {
        todoItemRepository.save(new TodoItem("First task", "First description"));
        todoItemRepository.save(new TodoItem("Second task", "Second description"));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getTodoByIdReturnsItem() throws Exception {
        TodoItem saved = todoItemRepository.save(new TodoItem("Find me", "Test lookup"));

        mockMvc.perform(get("/api/todos/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Find me")));
    }

    @Test
    void getMissingTodoReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/todos/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTodoChangesItsFields() throws Exception {
        TodoItem saved = todoItemRepository.save(new TodoItem("Old title", "Old description"));
        Map<String, Object> request = Map.of(
                "title", "Updated title",
                "description", "Updated description",
                "targetCompletionDate", "2026-12-30T18:00:00"
        );

        mockMvc.perform(put("/api/todos/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated title")))
                .andExpect(jsonPath("$.description", is("Updated description")));
    }

    @Test
    void markTodoCompletedAndIncomplete() throws Exception {
        TodoItem saved = todoItemRepository.save(new TodoItem("Toggle task", "Status test"));

        mockMvc.perform(patch("/api/todos/{id}/complete", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed", is(true)))
                .andExpect(jsonPath("$.completedAt").isNotEmpty());

        mockMvc.perform(patch("/api/todos/{id}/incomplete", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed", is(false)))
                .andExpect(jsonPath("$.completedAt", nullValue()));
    }

    @Test
    void completedAndPendingEndpointsReturnCorrectItems() throws Exception {
        TodoItem completed = new TodoItem("Completed task", "Done");
        completed.setCompleted(true);
        todoItemRepository.save(completed);
        todoItemRepository.save(new TodoItem("Pending task", "Not done"));

        mockMvc.perform(get("/api/todos/completed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Completed task")));

        mockMvc.perform(get("/api/todos/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Pending task")));
    }

    @Test
    void deleteTodoRemovesIt() throws Exception {
        TodoItem saved = todoItemRepository.save(new TodoItem("Delete me", "Delete test"));

        mockMvc.perform(delete("/api/todos/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Todo item deleted successfully")));

        mockMvc.perform(get("/api/todos/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCompletedRemovesOnlyCompletedItems() throws Exception {
        TodoItem completed = new TodoItem("Remove completed", "Done");
        completed.setCompleted(true);
        todoItemRepository.save(completed);
        todoItemRepository.save(new TodoItem("Keep pending", "Still active"));

        mockMvc.perform(delete("/api/todos/completed"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Keep pending")));
    }
}
