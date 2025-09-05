package tracker.model;

// Класс Epic наследуется от Task и содержит список подзадач
public class Epic extends Task {
    private java.util.List<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasks = new java.util.ArrayList<>();
    }

    public java.util.List<Subtask> getSubtasks() {
        return subtasks;
    }
}
