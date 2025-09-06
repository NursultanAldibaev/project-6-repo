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
     * Удаляет задачу из истории по её идентификатору.
     *
     * @param id идентификатор задачи для удаления
     */
    void remove(int id);

    /**
     * Возвращает историю просмотров задач.
     *
     * @return список задач в порядке просмотра
     */
    List<Task> getHistory();
}
