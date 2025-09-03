package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.*;

/**
 * Реализация интерфейса TaskManager.
 * Хранит все задачи, эпики и подзадачи в памяти с использованием HashMap.
 * 
 * - Каждой сущности присваивается уникальный id.
 * - Для эпиков автоматически поддерживается список связанных подзадач.
 * - Интеграция с HistoryManager: при получении и удалении задач обновляется история.
 */
public class InMemoryTaskManager implements TaskManager {

    /** Хранилище обычных задач по id */
    private final Map<Integer, Task> tasks = new HashMap<>();

    /** Хранилище эпиков по id */
    private final Map<Integer, Epic> epics = new HashMap<>();

    /** Хранилище подзадач по id */
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    /** Счётчик для генерации уникальных id */
    private int idCounter = 0;

    /** Менеджер истории просмотров */
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    /**
     * Создание обычной задачи.
     * Присваивается новый id и сохраняется в HashMap.
     */
    @Override
    public void createTask(Task task) {
        task.setId(++idCounter);
        tasks.put(task.getId(), task);
    }

    /**
     * Создание эпика.
     * У эпика также будет свой список подзадач.
     */
    @Override
    public void createEpic(Epic epic) {
        epic.setId(++idCounter);
        epics.put(epic.getId(), epic);
    }

    /**
     * Создание подзадачи.
     * Подзадача связывается с эпиком по epicId.
     */
    @Override
    public void createSubtask(Subtask subtask) {
        subtask.setId(++idCounter);
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.getSubtasks().add(subtask);
        }
    }

    /**
     * Получение задачи по id.
     * При этом добавляется в историю просмотров.
     */
    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    /**
     * Получение эпика по id.
     * При этом добавляется в историю просмотров.
     */
    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    /**
     * Получение подзадачи по id.
     * При этом добавляется в историю просмотров.
     */
    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    /**
     * Обновление обычной задачи (перезапись по id).
     */
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    /**
     * Обновление эпика (перезапись по id).
     */
    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    /**
     * Обновление подзадачи (перезапись по id).
     */
    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    /**
     * Удаление задачи по id.
     * Также очищается из истории.
     */
    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id); // удаляем из истории
    }

    /**
     * Удаление эпика по id.
     * При этом также удаляются все связанные подзадачи
     * и очищаются из истории.
     */
    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
                historyManager.remove(subtask.getId()); // удаляем подзадачи из истории
            }
        }
        historyManager.remove(id); // удаляем сам эпик из истории
    }

    /**
     * Удаление подзадачи по id.
     * Также обновляется список подзадач у эпика.
     */
    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtasks().remove(subtask);
            }
        }
        historyManager.remove(id); // удаляем подзадачу из истории
    }

    /**
     * Получение истории просмотров задач.
     */
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
