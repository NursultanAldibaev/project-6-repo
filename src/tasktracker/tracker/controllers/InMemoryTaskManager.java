package tracker.controllers;

import tracker.model.Task;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация TaskManager в памяти с интеграцией истории просмотров.
 * История просмотров реализована через InMemoryHistoryManager.
 */
public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    /** Менеджер истории просмотров задач */
    private final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    private int nextTaskId = 1;
    private int nextEpicId = 1;
    private int nextSubtaskId = 1;

    /**
     * Сбрасывает счетчики ID задач, эпиков и подзадач.
     */
    public void resetIdCounter() {
        nextTaskId = 1;
        nextEpicId = 1;
        nextSubtaskId = 1;
    }

    @Override
    public int createTask(final Task task) {
        task.setId(nextTaskId++);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int createEpic(final Epic epic) {
        epic.setId(nextEpicId++);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int createSubtask(final Subtask subtask) {
        subtask.setId(nextSubtaskId++);
        subtasks.put(subtask.getId(), subtask);

        final Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic);
        }

        return subtask.getId();
    }

    @Override
    public Task getTaskById(final int id) {
        final Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(final int id) {
        final Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(final int id) {
        final Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
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

    @Override
    public void updateTask(final Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(final Epic epic) {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    @Override
    public void updateSubtask(final Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        final Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteTask(final int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(final int id) {
        final Epic epic = epics.remove(id);
        if (epic != null) {
            for (final int subId : epic.getSubtaskIds()) {
                subtasks.remove(subId);
                historyManager.remove(subId);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtask(final int id) {
        final Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            final Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic);
            }
            historyManager.remove(id);
        }
    }

    /**
     * Возвращает текущую историю просмотров задач.
     *
     * @return список задач в порядке просмотра
     */
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /**
     * Обновляет статус эпика в зависимости от статусов его подзадач.
     *
     * @param epic эпик для обновления
     */
    private void updateEpicStatus(final Epic epic) {
        final List<Integer> subIds = epic.getSubtaskIds();
        if (subIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (final int id : subIds) {
            final Subtask subtask = subtasks.get(id);
            if (subtask != null) {
                if (subtask.getStatus() != Status.DONE) {
                    allDone = false;
                }
                if (subtask.getStatus() != Status.NEW) {
                    allNew = false;
                }
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
