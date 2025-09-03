package tracker.controllers;

import tracker.model.*;
import java.util.List;

/**
 * Интерфейс для управления задачами, эпиками и подзадачами.
 * Делаем методы для создания, обновления, удаления и получения объектов,
 * а также получения истории просмотров.
 */
public interface TaskManager {

    /**
     * Создаёт новую задачу.
     *
     * @param task задача для создания
     * @return уникальный идентификатор созданной задачи
     */
    int createTask(Task task);

    /**
     * Создаёт новый эпик.
     *
     * @param epic эпик для создания
     * @return уникальный идентификатор созданного эпика
     */
    int createEpic(Epic epic);

    /**
     * Создаёт новую подзадачу.
     *
     * @param subtask подзадача для создания
     * @return уникальный идентификатор созданной подзадачи
     */
    int createSubtask(Subtask subtask);

    /**
     * Обновляет существующую задачу.
     *
     * @param task обновлённая задача
     */
    void updateTask(Task task);

    /**
     * Обновляет существующий эпик.
     *
     * @param epic обновлённый эпик
     */
    void updateEpic(Epic epic);

    /**
     * Обновляет существующую подзадачу.
     *
     * @param subtask обновлённая подзадача
     */
    void updateSubtask(Subtask subtask);

    /**
     * Удаляет задачу по её идентификатору.
     *
     * @param id идентификатор задачи
     */
    void deleteTaskById(int id);

    /**
     * Удаляет эпик по его идентификатору.
     *
     * @param id идентификатор эпика
     */
    void deleteEpicById(int id);

    /**
     * Удаляет подзадачу по её идентификатору.
     *
     * @param id идентификатор подзадачи
     */
    void deleteSubtaskById(int id);

    /**
     * Удаляет все задачи.
     */
    void deleteTasks();

    /**
     * Удаляет все подзадачи.
     */
    void deleteSubtasks();

    /**
     * Удаляет все эпики и связанные с ними подзадачи.
     */
    void deleteEpics();

    /**
     * Возвращает список всех задач.
     *
     * @return список задач
     */
    List<Task> getAllTasks();

    /**
     * Возвращает список всех эпиков.
     *
     * @return список эпиков
     */
    List<Epic> getAllEpics();

    /**
     * Возвращает список всех подзадач.
     *
     * @return список подзадач
     */
    List<Subtask> getAllSubtasks();

    /**
     * Возвращает задачу по её идентификатору.
     *
     * @param id идентификатор задачи
     * @return задача или {@code null}, если не найдена
     */
    Task getTask(int id);

    /**
     * Возвращает эпик по его идентификатору.
     *
     * @param id идентификатор эпика
     * @return эпик или {@code null}, если не найден
     */
    Epic getEpic(int id);

    /**
     * Возвращает подзадачу по её идентификатору.
     *
     * @param id идентификатор подзадачи
     * @return подзадача или {@code null}, если не найдена
     */
    Subtask getSubtask(int id);

    /**
     * Возвращает список подзадач, относящихся к указанному эпику.
     *
     * @param epicId идентификатор эпика
     * @return список подзадач
     */
    List<Subtask> getEpicSubtasks(int epicId);

    /**
     * Возвращает историю просмотров задач.
     *
     * @return список просмотренных задач в порядке обращения
     */
    List<Task> getHistory();
}
