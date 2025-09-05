package tracker.model;

import java.util.ArrayList;
import java.util.List;

// Класс эпика, содержит список подзадач
public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public void addSubtask(int id) { subtaskIds.add(id); }
    public void removeSubtask(int id) { subtaskIds.remove((Integer) id); }

    public List<Integer> getSubtaskIds() { return subtaskIds; }

    @Override
    public String toString() {
        return "Epic{" + "id=" + getId() + ", name='" + getName() + '\'' +
               ", description='" + getDescription() + '\'' +
               ", status=" + getStatus() + ", subtaskIds=" + subtaskIds + '}';
    }
}
