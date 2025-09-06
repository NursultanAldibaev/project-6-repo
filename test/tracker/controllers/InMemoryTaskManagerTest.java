package tracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для InMemoryTaskManager.
 * Проверяется интеграция с InMemoryHistoryManager, создание, удаление и история задач.
 */
class InMemoryTaskManagerTest {

    private InMemoryTaskManager manager;

    @BeforeEach
    void setup() {
        manager = new InMemoryTaskManager();
        manager.resetIdCounter();
    }

    /**
     * Проверка добавления задачи в историю после получения по ID.
     */
    @Test
    void testCreateAndGetTaskAddsToHistory() {
        Task task = new Task("Task 1", "Description 1");
        int id = manager.createTask(task);

        manager.getTaskById(id);
        List<Task> history = manager.getHistory();
        assertEquals(1, history.size(), "История должна содержать одну задачу после getTaskById");
        assertEquals(task, history.get(0));
    }

    /**
     * Проверка добавления подзадачи в историю.
     */
    @Test
    void testCreateAndGetSubtaskAddsToHistory() {
        Epic epic = new Epic("Epic 1", "Epic description");
        int epicId = manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Subtask description", epicId);
        int subtaskId = manager.createSubtask(subtask);

        manager.getSubtaskById(subtaskId);
        List<Task> history = manager.getHistory();
        assertEquals(1, history.size(), "История должна содержать подзадачу");
        assertEquals(subtask, history.get(0));
    }

    /**
     * Проверка удаления задачи из менеджера и истории.
     */
    @Test
    void testDeleteTaskRemovesFromHistory() {
        Task task1 = new Task("Task 1", "Desc 1");
        Task task2 = new Task("Task 2", "Desc 2");
        int id1 = manager.createTask(task1);
        int id2 = manager.createTask(task2);

        manager.getTaskById(id1);
        manager.getTaskById(id2);

        manager.deleteTask(id1);
        List<Task> history = manager.getHistory();
        assertEquals(1, history.size(), "История должна содержать только Task 2 после удаления Task 1");
        assertEquals(task2, history.get(0));
    }

    /**
     * Проверка удаления эпика вместе с его подзадачами из истории.
     */
    @Test
    void testDeleteEpicRemovesEpicAndSubtasksFromHistory() {
        Epic epic = new Epic("Epic 1", "Desc Epic");
        int epicId = manager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc1", epicId);
        Subtask sub2 = new Subtask("Sub2", "Desc2", epicId);
        int sub1Id = manager.createSubtask(sub1);
        int sub2Id = manager.createSubtask(sub2);

        manager.getEpicById(epicId);
        manager.getSubtaskById(sub1Id);
        manager.getSubtaskById(sub2Id);

        manager.deleteEpic(epicId);
        List<Task> history = manager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой после удаления эпика и его подзадач");
    }

    /**
     * Проверка, что повторный доступ к задаче не увеличивает историю.
     */
    @Test
    void testDuplicateAccessMovesTaskToEnd() {
        Task task = new Task("Task 1", "Desc 1");
        int id = manager.createTask(task);

        manager.getTaskById(id);
        manager.getTaskById(id); // повтор
        List<Task> history = manager.getHistory();
        assertEquals(1, history.size(), "Дубликаты задачи не должны увеличивать историю");
        assertEquals(task, history.get(0));
    }
}
