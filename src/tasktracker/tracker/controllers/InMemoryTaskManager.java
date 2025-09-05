package tasktracker.tracker.controllers;

import tasktracker.tracker.model.Task;
import tasktracker.tracker.model.Epic;
import tasktracker.tracker.model.Subtask;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;

    @Override
    public int createTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int createSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask.getId());
        }
        return subtask.getId();
    }

    @Override
    public Task getTask(int id) {
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }
}
