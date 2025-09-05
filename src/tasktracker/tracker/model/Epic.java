package tracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс Эпик
 */
public class Epic extends Task {

    private final List<Integer> subtaskIds = new ArrayList<>();

    /**
     * Конструктор эпика
     *
     * @param name        название эпика
     * @param description описание эпика
     */
    public Epic(String name, String description) {
        super(name, description);
        setStatus(Status.NEW);
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(int id) {
        subtaskIds.add(id);
    }

    public void removeSubtaskId(int id) {
        subtaskIds.remove(Integer.valueOf(id));
    }

    @Override
    public String toString() {
        return "Epic{" +
               "id=" + getId() +
               ", name='" + getName() + '\'' +
               ", description='" + getDescription() + '\'' +
               ", status=" + getStatus() +
               ", subtaskIds=" + subtaskIds +
               '}';
    }

}
