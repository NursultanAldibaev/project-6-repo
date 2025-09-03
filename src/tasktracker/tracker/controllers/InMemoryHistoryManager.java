package tracker.controllers;

import tracker.model.Task;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация {@link HistoryManager}, и она хранит историю задач в памяти.
 * Сохраняются последние 10 задач. Если же задач будет больше 10, то самая старая задача удаляется.
 */
public class InMemoryHistoryManager implements HistoryManager {

    /**
     * Список задач, представляющий историю.
     */
    private final List<Task> history = new ArrayList<>();

    /**
     * Добавляет задачу в историю. Если история уже содержит 10 задач,
     * удаляет самую старую перед добавлением новой.
     *
     * @param task задача для добавления
     */
    @Override
    public void add(Task task) {
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(task);
    }

    /**
     * Возвращает копию списка задач из истории.
     *
     * @return список задач в порядке добавления
     */
    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
