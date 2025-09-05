package tracker.controllers;

import tracker.model.Task;
import tracker.model.Epic;
import tracker.model.Subtask;
import java.util.List;

/**
 * Интерфейс для управления задачами, эпиками и подзадачами.
 */
public interface TaskManager {

    /**
     * Создаёт новую задачу.
     *
     * @param task задача для создания
     * @return идентификатор созданной задачи
     */
    int createTask(Task task);

    /**
     * Создаёт новый эпик.
     *
     * @param epic эпик для создания
     * @return идентификатор созданного эпика
     */
    int createEpic(Epic epic);

    /**
     * Создаёт новую подзадачу.
     *
     * @param subtask подзадача для создания
     * @return идентификатор созданной подзадачи
     */
    int createSubtask(Subtask subtask);

    /**
     * Получает задачу по её идентификатору.
     *
     * @param id идентификатор задачи
     * @return задача
     */
    Task getTask(int id);

    /**
     * Получает эпик по его идентификатору.
     *
     * @param id идентификатор эпика
     * @return эпик
     */
    Epic getEpic(int id);

    /**
     * Получает подзадачу по её идентификатору.
     *
     * @param id идентификатор подзадачи
     * @return подзадача
     */
    Subtask getSubtask(int id);

    /**
     * Возвращает все подзадачи эпика.
     *
     * @param epicId идентификатор эпика
     * @return список подзадач
     */
    List<Subtask> getAllSubtasks(int epicId);

    /**
     * Возвращает историю просмотров задач.
     *
     * @return список задач в порядке просмотра
     */
    List<Task> getHistory();

    /**
     * Обновляет задачу.
     *
     * @param task задача для обновления
     */
    void updateTask(Task task);

    /**
     * Обновляет эпик.
     *
     * @param epic эпик для обновления
     */
    void updateEpic(Epic epic);

    /**
     * Обновляет подзадачу.
     *
     * @param subtask подзадача для обновления
     */
    void updateSubtask(Subtask subtask);

    /**
     * Удаляет задачу по идентификатору.
     *
     * @param id идентификатор задачи
     */
    void deleteTask(int id);

    /**
     * Удаляет эпик по идентификатору.
     *
     * @param id идентификатор эпика
     */
    void deleteEpic(int id);

    /**
     * Удаляет подзадачу по идентификатору.
     *
     * @param id идентификатор подзадачи
     */
    void deleteSubtask(int id);
}
