package tracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для InMemoryHistoryManager.
 * Проверяется добавление, удаление и корректная обработка дубликатов в истории.
 */
class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    /**
     * Проверка добавления задач в историю.
     */
    @Test
    void testAddTaskToHistory() {
        final Task task1 = new Task("Task 1", "Desc 1");
        final Task task2 = new Task("Task 2", "Desc 2");

        historyManager.add(task1);
        historyManager.add(task2);

        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать 2 задачи");
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    /**
     * Проверка того, что дубликаты перемещаются в конец истории.
     */
    @Test
    void testAddDuplicateMovesToEnd() {
        final Task task1 = new Task("Task 1", "Desc 1");
        final Task task2 = new Task("Task 2", "Desc 2");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1); // повтор

        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "Дубликаты не должны увеличивать размер истории");
        assertEquals(task2, history.get(0), "Task2 должен быть первым");
        assertEquals(task1, history.get(1), "Task1 должен переместиться в конец");
    }

    /**
     * Проверка удаления задачи из истории.
     */
    @Test
    void testRemoveTaskFromHistory() {
        final Task task1 = new Task("Task 1", "Desc 1");
        final Task task2 = new Task("Task 2", "Desc 2");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());

        final List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История после удаления должна содержать 1 задачу");
        assertEquals(task2, history.get(0));
    }

    /**
     * Проверка удаления несуществующей задачи не изменяет историю.
     */
    @Test
    void testRemoveNonExistentTask() {
        final Task task1 = new Task("Task 1", "Desc 1");

        historyManager.add(task1);
        historyManager.remove(999); // несуществующий ID

        final List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История должна остаться без изменений");
        assertEquals(task1, history.get(0));
    }

    /**
     * Проверка пустой истории.
     */
    @Test
    void testEmptyHistory() {
        final List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "Пустая история должна возвращать пустой список");
    }
}
