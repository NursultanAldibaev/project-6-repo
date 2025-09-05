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
 * Реализация TaskManager в памяти
 */
public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;

    @Override
    public int createTask(Task task)
    {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic)
    {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public int createSubtask(Subtask subtask)
    {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic);
        }

        return subtask.getId();
    }

    @Override
    public Task getTaskById(int id)
    {
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id)
    {
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id)
    {
        return subtasks.get(id);
    }

    @Override
    public List<Task> getAllTasks()
    {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics()
    {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks()
    {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void updateTask(Task task)
    {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic)
    {
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask)
    {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
        }
    }

    @Override
    public void deleteTask(int id)
    {
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id)
    {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subId : epic.getSubtaskIds()) {
                subtasks.remove(subId);
            }
        }
    }

    @Override
    public void deleteSubtask(int id)
    {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic);
            }
        }
    }

    private void updateEpicStatus(Epic epic)
    {
        List<Integer> subIds = epic.getSubtaskIds();
        if (subIds.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (int id : subIds) {
            Subtask s = subtasks.get(id);
            if (s != null) {
                if (s.getStatus() != Status.DONE) {
                    allDone = false;
                }
                if (s.getStatus() != Status.NEW) {
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
