package tracker.controllers;

import tracker.model.Task;
import java.util.List;

/**
 * Интерфейс для управления историей просмотров задач.
 * Сохраняет добавленные задачи и предоставляет доступ к списку истории.
 */
public interface HistoryManager {

    /**
     * Добавляет задачу в историю.
     *
     * @param task задача для добавления
     */
    void add(Task task);

    /**
     * Возвращает список задач из истории в порядке добавления.
     *
     * @return список задач в истории
     */
    List<Task> getHistory();
}
