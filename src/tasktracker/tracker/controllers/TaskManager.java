package tracker.controllers;

import tracker.model.Task;
import tracker.model.Epic;
import tracker.model.Subtask;
import java.util.List;

// Интерфейс менеджера задач
public interface TaskManager {
    int createTask(Task task);
    int createEpic(Epic epic);
    int createSubtask(Subtask subtask);

    Task getTask(int id);
    Epic getEpic(int id);
    Subtask getSubtask(int id);

    List<Task> getAllTasks();
    List<Subtask> getAllSubtasks();
    List<Epic> getAllEpics();
}
