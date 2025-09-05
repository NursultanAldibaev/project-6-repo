package tasktracker.tracker.controllers;

import tasktracker.tracker.model.Task;
import tasktracker.tracker.model.Epic;
import tasktracker.tracker.model.Subtask;

import java.util.List;

public interface TaskManager {
    int createTask(Task task);
    int createEpic(Epic epic);
    int createSubtask(Subtask subtask);

    Task getTask(int id);
    Epic getEpic(int id);
    Subtask getSubtask(int id);

    List<Task> getAllTasks();
    List<Epic> getAllEpics();
    List<Subtask> getAllSubtasks();

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    void deleteTask(int id);
    void deleteEpic(int id);
    void deleteSubtask(int id);

    List<Task> getHistory();
}
