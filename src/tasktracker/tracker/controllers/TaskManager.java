package tracker.controllers;

import tracker.model.Task;
import tracker.model.Epic;
import tracker.model.Subtask;

import java.util.List;

/**
 * Интерфейс менеджера задач
 */
public interface TaskManager {

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubtask(Subtask subtask);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks(); // Исправлено: без аргументов

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

}
