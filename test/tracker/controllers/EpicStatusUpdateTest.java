package tracker.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.Status;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicStatusUpdateTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void epicShouldBeNewIfAllSubtasksAreNew() {
        Epic epic = new Epic("Epic 1", "Description");
        int epicId = taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc1", Status.NEW, epicId);
        Subtask sub2 = new Subtask("Sub2", "Desc2", Status.NEW, epicId);

        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        Epic savedEpic = taskManager.getEpic(epicId);
        assertEquals(Status.NEW, savedEpic.getStatus(), "Эпик должен быть NEW при всех подзадачах NEW");
    }

    @Test
    void epicShouldBeDoneIfAllSubtasksAreDone() {
        Epic epic = new Epic("Epic 2", "Description");
        int epicId = taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc1", Status.DONE, epicId);
        Subtask sub2 = new Subtask("Sub2", "Desc2", Status.DONE, epicId);

        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        Epic savedEpic = taskManager.getEpic(epicId);
        assertEquals(Status.DONE, savedEpic.getStatus(), "Эпик должен быть DONE при всех подзадачах DONE");
    }

    @Test
    void epicShouldBeInProgressIfMixedStatuses() {
        Epic epic = new Epic("Epic 3", "Description");
        int epicId = taskManager.createEpic(epic);

        Subtask sub1 = new Subtask("Sub1", "Desc1", Status.NEW, epicId);
        Subtask sub2 = new Subtask("Sub2", "Desc2", Status.DONE, epicId);

        taskManager.createSubtask(sub1);
        taskManager.createSubtask(sub2);

        Epic savedEpic = taskManager.getEpic(epicId);
        assertEquals(Status.IN_PROGRESS, savedEpic.getStatus(), "Эпик должен быть IN_PROGRESS при смешанных статусах");
    }

    @Test
    void epicShouldBeNewIfNoSubtasks() {
        Epic epic = new Epic("Epic 4", "No subtasks");
        int epicId = taskManager.createEpic(epic);

        Epic savedEpic = taskManager.getEpic(epicId);
        assertEquals(Status.NEW, savedEpic.getStatus(), "Эпик без подзадач должен быть NEW");
    }
}
