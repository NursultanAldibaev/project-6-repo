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
        final Task task = new Task("Task 1", "Description 1");
        final int id = manager.createTask(task);

        manager.getTaskById(id);
        final List<Task> history = manager.getHistory();
        assertEquals(1, history.size(),
                "История должна содержать одну задачу после getTaskById");
        assertEquals(task, history.get(0));
    }

    /**
     * Проверка добавления подзадачи в историю.
     */
    @Test
    void testCreateAndGetSubtaskAddsToHistory() {
        final Epic epic = new Epic("Epic 1", "Epic description");
        final int epicId = manager.createEpic(epic);

        final Subtask subtask = new Subtask("Subtask 1", "Subtask description", epicId);
        final int subtaskId = manager.createSubtask(subtask);

        manager.getSubtaskById(subtaskId);
        final List<Task> history = manager.getHistory();
        assertEquals(1, history.size(),
                "История должна содержать подзадачу после getSubtaskById");
        assertEquals(subtask, history.get(0));
    }

    /**
     * Проверка удаления задачи из менеджера и истории.
     */
    @Test
    void testDeleteTaskRemovesFromHistory() {
        final Task task1 = new Task("Task 1", "Desc 1");
        final Task task2 = new Task("Task 2", "Desc 2");
        final int id1 = manager.createTask(task1);
        final int id2 = manager.createTask(task2);

        manager.getTaskById(id1);
        manager.getTaskById(id2);

        manager.deleteTask(id1);
        final List<Task> history = manager.getHistory();
        assertEquals(1, history.size(),
                "История должна содержать только Task 2 после удаления Task 1");
        assertEquals(task2, history.get(0));
    }

    /**
     * Проверка удаления эпика вместе с его подзадачами из истории.
     */
    @Test
    void testDeleteEpicRemovesEpicAndSubtasksFromHistory() {
        final Epic epic = new Epic("Epic 1", "Desc Epic");
        final int epicId = manager.createEpic(epic);

        final Subtask sub1 = new Subtask("Sub1", "Desc1", epicId);
        final Subtask sub2 = new Subtask("Sub2", "Desc2", epicId);
        final int sub1Id = manager.createSubtask(sub1);
        final int sub2Id = manager.createSubtask(sub2);

        manager.getEpicById(epicId);
        manager.getSubtaskById(sub1Id);
        manager.getSubtaskById(sub2Id);

        manager.deleteEpic(epicId);
        final List<Task> history = manager.getHistory();
        assertTrue(history.isEmpty(),
                "История должна быть пустой после удаления эпика и его подзадач");
    }

    /**
     * Проверка, что повторный доступ к задаче не увеличивает историю.
     */
    @Test
    void testDuplicateAccessMovesTaskToEnd() {
        final Task task = new Task("Task 1", "Desc 1");
        final int id = manager.createTask(task);

        manager.getTaskById(id);
        manager.getTaskById(id); // повторный доступ
        final List<Task> history = manager.getHistory();
        assertEquals(1, history.size(),
                "Дубликаты задачи не должны увеличивать историю");
        assertEquals(task, history.get(0));
    }
}
