package tracker.controllers;

import tracker.model.Task;
import tracker.model.Epic;
import tracker.model.Subtask;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;

    // Создание задачи
    @Override
    public int createTask(Task task)
    {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    // Создание эпика
    @Override
    public int createEpic(Epic epic)
    {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    // Создание подзадачи
    @Override
    public int createSubtask(Subtask subtask)
    {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null)
        {
            epic.addSubtask(subtask.getId());
        }
        return subtask.getId();
    }

    @Override
    public Task getTask(int id)
    {
        Task task = tasks.get(id);
        if (task != null)
        {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpic(int id)
    {
        Epic epic = epics.get(id);
        if (epic != null)
        {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtask(int id)
    {
        Subtask subtask = subtasks.get(id);
        if (subtask != null)
        {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public List<Task> getHistory()
    {
        return historyManager.getHistory();
    }

    @Override
    public List<Subtask> getAllSubtasks()
    {
        return new ArrayList<>(subtasks.values());
    }
}
