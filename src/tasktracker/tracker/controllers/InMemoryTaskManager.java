package tasktracker.tracker.controllers;

import tasktracker.tracker.model.Task;
import tasktracker.tracker.model.Epic;
import tasktracker.tracker.model.Subtask;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

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
        if (epic != null) epic.addSubtask(subtask.getId());
        return subtask.getId();
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Task> getAllTasks() { return new ArrayList<>(tasks.values()); }

    @Override
    public List<Epic> getAllEpics() { return new ArrayList<>(epics.values()); }

    @Override
    public List<Subtask> getAllSubtasks() { return new ArrayList<>(subtasks.values()); }

    @Override
    public void updateTask(Task task) { tasks.put(task.getId(), task); }

    @Override
    public void updateEpic(Epic epic) { epics.put(epic.getId(), epic); }

    @Override
    public void updateSubtask(Subtask subtask) { subtasks.put(subtask.getId(), subtask); }

    @Override
    public void deleteTask(int id) { tasks.remove(id); }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskIds()) subtasks.remove(subtaskId);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) epic.removeSubtask(id);
        }
    }

    @Override
    public List<Task> getHistory() { return historyManager.getHistory(); }
}
