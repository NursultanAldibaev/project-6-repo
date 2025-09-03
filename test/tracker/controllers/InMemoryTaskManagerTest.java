package tracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Task;
import tracker.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void shouldAddNewTask() {
        Task task = new Task("Test task", "Test description", Status.NEW);
        int taskId = taskManager.createTask(task);

        Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task.getName(), savedTask.getName(), "Названия задач не совпадают.");
        assertEquals(task.getDescription(), savedTask.getDescription(), "Описания задач не совпадают.");
        assertEquals(task.getStatus(), savedTask.getStatus(), "Статусы задач не совпадают.");
    }

    @Test
    void shouldStoreTasksInHistory() {
        Task task = new Task("History task", "Tracked task", Status.NEW);
        int id = taskManager.createTask(task);

        // вызов метода getTask должен добавить в историю
        taskManager.getTask(id);
        List<Task> history = taskManager.getHistory();

        assertEquals(1, history.size(), "История должна содержать одну задачу.");
        assertEquals(task.getName(), history.get(0).getName(), "Неверная задача в истории.");
    }
}
