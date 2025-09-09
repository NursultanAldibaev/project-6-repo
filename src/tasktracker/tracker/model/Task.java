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

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // 🔽 Новый метод — сохранение в CSV
    public String toCsvString() {
        return String.format("%d,%s,%s,%s,%s,", id, TaskType.TASK, name, status, description);
    }

    // 🔽 Новый метод — восстановление из CSV
    public static Task fromCsv(String line) {
        String[] fields = line.split(",");
        int id = Integer.parseInt(fields[0]);
        String type = fields[1];
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];

        Task task;
        switch (type) {
            case "EPIC" -> task = new Epic(name, description);
            case "SUBTASK" -> {
                int epicId = Integer.parseInt(fields[5]);
                task = new Subtask(name, description, epicId);
            }
            default -> task = new Task(name, description);
        }

        task.setId(id);
        task.setStatus(status);
        return task;
    }

    @Override
    public String toString() {
        return "Task{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", status=" + status +
               '}';
    }
}
