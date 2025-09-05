package tracker.model;

/**
 * Класс задачи Task
 */
public class Task {

    private int id;
    private String name;
    private String description;
    private Status status;

    /**
     * Конструктор задачи
     *
     * @param name        название задачи
     * @param description описание задачи
     */
    public Task(String name, String description)
    {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    /**
     * Получить id задачи
     *
     * @return id
     */
    public int getId()
    {
        return id;
    }

    /**
     * Установить id задачи
     *
     * @param id идентификатор
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Получить название задачи
     *
     * @return название
     */
    public String getName()
    {
        return name;
    }

    /**
     * Установить название задачи
     *
     * @param name название
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Получить описание задачи
     *
     * @return описание
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Установить описание задачи
     *
     * @param description описание
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Получить статус задачи
     *
     * @return статус
     */
    public Status getStatus()
    {
        return status;
    }

    /**
     * Установить статус задачи
     *
     * @param status статус
     */
    public void setStatus(Status status)
    {
        this.status = status;
    }
