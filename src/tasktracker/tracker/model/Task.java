package tracker.model;

import java.util.Objects;

/**
 * Класс, представляющий обычную задачу с полями: идентификатор, имя, описание и статус.
 * Может использоваться как базовый класс для {@link Epic} и {@link Subtask}.
 */
public class Task {
    /** Уникальный идентификатор задачи */
    protected int id;

    /** Имя задачи */
    protected String name;

    /** Описание задачи */
    protected String description;

    /** Статус задачи */
    protected Status status;

    /**
     * Создаёт новую задачу.
     *
     * @param name        имя задачи
     * @param description описание задачи
     * @param status      статус задачи
     */
    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    /**
     * Возвращает идентификатор задачи.
     *
     * @return id задачи
     */
    public int getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор задачи.
     *
     * @param id уникальный идентификатор
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Возвращает имя задачи.
     *
     * @return имя задачи
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает описание задачи.
     *
     * @return описание задачи
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает статус задачи.
     *
     * @return статус
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Устанавливает статус задачи.
     *
     * @param status новый статус
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Возвращает строковое представление задачи.
     *
     * @return строка с данными задачи
     */
    @Override
    public String toString() {
        return String.format("Task{id=%d, name='%s', description='%s', status=%s}",
                id, name, description, status);
    }

    /**
     * Проверяет равенство задач по их идентификатору.
     *
     * @param o объект для сравнения
     * @return {@code true}, если id совпадают
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    /**
     * Возвращает хеш-код задачи на основе её идентификатора.
     *
     * @return хеш-код
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
