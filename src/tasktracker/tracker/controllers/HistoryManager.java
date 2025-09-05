package tracker.controllers;

import tracker.model.Task;
import java.util.List;

/**
 * Интерфейс для работы с историей просмотров задач.
 */
public interface HistoryManager {

    /**
     * Добавляет задачу в историю.
     *
     * @param task задача для добавления
     */
    void add(Task task);

    /**
     * Возвращает историю просмотров задач.
     *
     * @return список задач в порядке просмотра
     */
    List<Task> getHistory();
}
