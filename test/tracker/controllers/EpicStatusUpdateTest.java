package tracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.Subtask;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicStatusUpdateTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testEpicAllSubtasksNew() {
        Epic epic = new Epic("Epic 1", "Description Epic 1");
        int epicId = taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc1", epicId);
        Subtask sub2 = new Subtask("Sub2", "Desc2", epicId);
        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        Epic savedEpic = taskManager.getEpicById(epicId);
        assertEquals(Status.NEW, savedEpic.getStatus(), "Эпик должен быть NEW при всех подзадачах NEW");
    }

    @Test
    void testEpicAllSubtasksDone() {
        Epic epic = new Epic("Epic 2", "Description Epic 2");
        int epicId = taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc1", epicId);
        sub1.setStatus(Status.DONE);
        Subtask sub2 = new Subtask("Sub2", "Desc2", epicId);
        sub2.setStatus(Status.DONE);
        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        Epic savedEpic = taskManager.getEpicById(epicId);
        assertEquals(Status.DONE, savedEpic.getStatus(), "Эпик должен быть DONE при всех подзадачах DONE");
    }

    @Test
    void testEpicMixedSubtasks() {
        Epic epic = new Epic("Epic 3", "Description Epic 3");
        int epicId = taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc1", epicId);
        Subtask sub2 = new Subtask("Sub2", "Desc2", epicId);
        sub2.setStatus(Status.DONE);
        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        Epic savedEpic = taskManager.getEpicById(epicId);
        assertEquals(Status.IN_PROGRESS, savedEpic.getStatus(), "Эпик должен быть IN_PROGRESS при смешанных статусах");
    }

    @Test
    void testEpicWithoutSubtasks() {
        Epic epic = new Epic("Epic 4", "Description Epic 4");
        int epicId = taskManager.createEpic(epic);

        Epic savedEpic = taskManager.getEpicById(epicId);
        assertEquals(Status.NEW, savedEpic.getStatus(), "Эпик без подзадач должен быть NEW");
    }
}
