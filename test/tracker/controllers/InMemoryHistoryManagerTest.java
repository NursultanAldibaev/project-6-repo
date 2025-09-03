package tracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Task;
import tracker.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void shouldAddTaskToHistory() {
        Task task = new Task("History Test", "Testing history add", Status.NEW);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не должна быть null.");
        assertEquals(1, history.size(), "История должна содержать одну задачу.");
        assertEquals(task.getName(), history.get(0).getName(), "Неверная задача в истории.");
    }

    @Test
    void shouldNotExceedHistoryLimit() {
        for (int i = 1; i <= 15; i++) {
            Task task = new Task("Task " + i, "Description " + i, Status.NEW);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();

        assertEquals(10, history.size(), "История должна содержать не более 10 задач.");
        assertEquals("Task 6", history.get(0).getName(), "Неверный первый элемент после переполнения.");
        assertEquals("Task 15", history.get(9).getName(), "Неверный последний элемент после переполнения.");
    }
}
