package tracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testCreateTask() {
        Task task = new Task("Task 1", "Desc 1");
        int id = taskManager.createTask(task);
        assertEquals(1, id);
        assertEquals(Status.NEW, taskManager.getTaskById(id).getStatus());
    }

    @Test
    void testCreateSubtask() {
        Task task = new Task("Epic Task", "Desc Epic");
        int epicId = taskManager.createTask(task);

        Subtask sub = new Subtask("Subtask 1", "Subdesc", epicId);
        int subId = taskManager.createSubtask(sub);
        assertEquals(1, subId);
        assertEquals(Status.NEW, taskManager.getSubtaskById(subId).getStatus());
    }
}
