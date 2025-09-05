package tracker.model;

// Класс обычной задачи
public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", name='" + name + '\'' +
               ", description='" + description + '\'' + ", status=" + status + '}';
    }
}
