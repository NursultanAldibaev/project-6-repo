package tracker.model;

/**
 * Базовый класс для всех задач.
 * Содержит общие поля: id, название, описание и статус.
 */
public class Task {
    /** Уникальный идентификатор задачи. */
    private int id;

    /** Название задачи. */
    private String name;

    /** Описание задачи. */
    private String description;

    /** Статус задачи. */
    private Status status;

    /**
     * Конструктор для создания задачи.
     *
     * @param name        название задачи
     * @param description описание задачи
     */
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
