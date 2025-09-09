package tracker.controllers;

import org.junit.jupiter.api.Test;
import tracker.model.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для FileBackedTaskManager.
 * Проверяем сохранение и загрузку задач.
 */
class FileBackedTaskManagerTest {

    @Test
    void testSaveAndLoadEmptyFile() throws IOException {
        // Создаём временный файл
        File file = File.createTempFile("test", ".csv");

        // Менеджер без задач
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        // Загружаем из пустого файла
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(file);

        // Проверяем, что задачи пустые
        assertTrue(loaded.getAllTasks().isEmpty());
        assertTrue(loaded.getAllEpics().isEmpty());
        assertTrue(loaded.getAllSubtasks().isEmpty());
    }

    @Test
    void testSaveAndLoadTasks() throws IOException {
        File file = File.createTempFile("test", ".csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        // Создаём задачу, эпик и подзадачу
        Task task = new Task("Task1", "Description1");
        Epic epic = new Epic("Epic1", "EpicDesc");
        int epicId = manager.createEpic(epic);
        Subtask subtask = new Subtask("Sub1", "SubDesc", epicId);

        manager.createTask(task);
        manager.createSubtask(subtask);

        // Загружаем из файла в новый менеджер
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(file);

        // Проверяем, что всё сохранилось
        assertEquals(1, loaded.getAllTasks().size());
        assertEquals(1, loaded.getAllEpics().size());
        assertEquals(1, loaded.getAllSubtasks().size());
    }
}
