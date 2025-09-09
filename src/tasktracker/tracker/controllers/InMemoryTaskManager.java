package tracker.controllers;

import tracker.model.Task;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Status;

import java.time.LocalDateTime;
import java.util.*;

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
     * TreeSet для приоритетов (по startTime). Задачи без startTime не попадают в это множество.
     * Компаратор: сначала по startTime, затем по id.
     */
    private final NavigableSet<Task> prioritized = new TreeSet<>((a, b) -> {
        LocalDateTime aStart = a.getStartTime();
        LocalDateTime bStart = b.getStartTime();
        if (aStart == null && bStart == null) {
            return Integer.compare(a.getId(), b.getId());
        }
        if (aStart == null) return 1; // nulls last
        if (bStart == null) return -1;
        int cmp = aStart.compareTo(bStart);
        if (cmp != 0) return cmp;
        return Integer.compare(a.getId(), b.getId());
    });

    /**
     * Сбрасывает счетчики ID задач, эпиков и подзадач.
     */
    public void resetIdCounter() {
        nextTaskId = 1;
        nextEpicId = 1;
        nextSubtaskId = 1;
        tasks.clear();
        epics.clear();
        subtasks.clear();
        prioritized.clear();
    }

    @Override
    public int createTask(final Task task) {
        // проверка пересечений
        if (hasIntersection(task)) {
            throw new IllegalStateException("Задача пересекается по времени с существующей задачей");
        }

        task.setId(nextTaskId++);
        tasks.put(task.getId(), task);
        addToPrioritized(task);
        return task.getId();
    }

    @Override
    public int createEpic(final Epic epic) {
        epic.setId(nextEpicId++);
        epics.put(epic.getId(), epic);
        // у эпика поля рассчитываются когда создаются подзадачи
        return epic.getId();
    }

    @Override
    public int createSubtask(final Subtask subtask) {
        // убедимся, что эпик существует
        final Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            throw new IllegalArgumentException("Эпик для Subtask не найден (id=" + subtask.getEpicId() + ")");
        }

        if (hasIntersection(subtask)) {
            throw new IllegalStateException("Подзадача пересекается по времени с существующей задачей");
        }

        subtask.setId(nextSubtaskId++);
        subtasks.put(subtask.getId(), subtask);

        epic.addSubtaskId(subtask.getId());
        // пересчитать эпик: используем map subtasks
        epic.updateFromSubtasks(subtasks);

        addToPrioritized(subtask);
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
        // проверка пересечений: перед изменением удаляем старую версию временно для корректной проверки
        Task existing = tasks.get(task.getId());
        if (existing != null) {
            removeFromPrioritized(existing);
        }
        if (hasIntersection(task)) {
            // вернём старую версию в prioritized (если была) и кинем исключение
            if (existing != null) addToPrioritized(existing);
            throw new IllegalStateException("Обновление задачи приводит к пересечению по времени");
        }
        tasks.put(task.getId(), task);
        addToPrioritized(task);
    }

    @Override
    public void updateEpic(final Epic epic) {
        epics.put(epic.getId(), epic);
        // при обновлении эпика вручную можно пересчитать поля
        epic.updateFromSubtasks(subtasks);
    }

    @Override
    public void updateSubtask(final Subtask subtask) {
        Subtask existing = subtasks.get(subtask.getId());
        if (existing != null) {
            removeFromPrioritized(existing);
        }
        if (hasIntersection(subtask)) {
            if (existing != null) addToPrioritized(existing);
            throw new IllegalStateException("Обновление подзадачи приводит к пересечению по времени");
        }

        subtasks.put(subtask.getId(), subtask);
        final Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.updateFromSubtasks(subtasks);
        }
        addToPrioritized(subtask);
    }

    @Override
    public void deleteTask(final int id) {
        Task removed = tasks.remove(id);
        if (removed != null) {
            removeFromPrioritized(removed);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpic(final int id) {
        final Epic epic = epics.remove(id);
        if (epic != null) {
            for (final int subId : new ArrayList<>(epic.getSubtaskIds())) {
                Subtask s = subtasks.remove(subId);
                if (s != null) {
                    removeFromPrioritized(s);
                    historyManager.remove(subId);
                }
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
                epic.updateFromSubtasks(subtasks);
            }
            removeFromPrioritized(subtask);
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
     * Возвращает список задач и подзадач, отсортированных по startTime (приоритет).
     * Задачи без startTime не включаются в список.
     *
     * @return упорядоченный список задач
     */
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritized);
    }

    /**
     * Добавляет задачу в prioritized если у неё задан startTime.
     */
    private void addToPrioritized(Task task) {
        if (task.getStartTime() != null) {
            prioritized.add(task);
        }
    }

    /**
     * Удаляет задачу из prioritized (если была).
     */
    private void removeFromPrioritized(Task task) {
        prioritized.remove(task);
    }

    /**
     * Проверяет пересечение двух задач (отрезки [start, end)).
     * Возвращает true если пересекаются. Если у одной или обеих задач нет startTime/duration, возвращает false.
     */
    private boolean isIntersect(Task a, Task b) {
        if (a == null || b == null) return false;
        LocalDateTime aStart = a.getStartTime();
        LocalDateTime bStart = b.getStartTime();
        LocalDateTime aEnd = a.getEndTime();
        LocalDateTime bEnd = b.getEndTime();
        if (aStart == null || bStart == null || aEnd == null || bEnd == null) {
            return false;
        }
        // пересечение отрезков: aStart < bEnd && bStart < aEnd
        return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
    }

    /**
     * Проверяет, пересекается ли task с любым из уже существующих задач — O(n) при использовании prioritized
     */
    private boolean hasIntersection(Task task) {
        if (task == null || task.getStartTime() == null || task.getDuration() == null) {
            return false;
        }
        // Оптимизация: так как prioritized отсортирован по startTime, можно искать потенциальных кандидатов.
        // Но для простоты и надёжности пройдёмся по prioritized и по tasks (включая задачи, которые не в prioritized)
        // Используем Stream API
        // Проверим только те задачи, у которых задан startTime и duration:
        return prioritized.stream().anyMatch(existing -> {
            // пропустим саму задачу (если обновление)
            if (existing.getId() == task.getId()) return false;
            return isIntersect(task, existing);
        }) ||
        // также проверим задачи, которые не в prioritized (в т.ч. те, у которых startTime==null — они не пересекаются)
        tasks.values().stream()
                .filter(t -> t.getStartTime() != null && t.getDuration() != null && t.getId() != task.getId())
                .anyMatch(t -> isIntersect(task, t)) ||
        epics.values().stream()
                .filter(e -> e.getStartTime() != null && e.getEndTime() != null)
                .anyMatch(e -> isIntersect(task, e)) ||
        subtasks.values().stream()
                .filter(s -> s.getStartTime() != null && s.getDuration() != null && s.getId() != task.getId())
                .anyMatch(s -> isIntersect(task, s));
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
