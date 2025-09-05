package tracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {

    private InMemoryTaskManager manager;

    // <-- Вот здесь добавляем BeforeEach
    @BeforeEach
    void setup() {
        manager = new InMemoryTaskManager();
        manager.resetIdCounter(); // сброс ID перед каждым тестом
    }

    @Test
    void testCreateTask() {
        Task task = new Task("Task 1", "Description 1");
        int id = manager.createTask(task);
        assertEquals(1, id);
    }

    @Test
    void testCreateSubtask() {
        Epic epic = new Epic("Epic 1", "Epic description");
        int epicId = manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask description", epicId);
        int subtaskId = manager.createSubtask(subtask);

        assertEquals(1, subtaskId);
    }
}
