package tracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс Epic — это особый вид задачи, который может содержать подзадачи (Subtask)
 */
public class Epic extends Task {

    private final List<Integer> subtaskIds;

    public Epic(String name, String description) {
        super(name, description);
        this.subtaskIds = new ArrayList<>();
    }

    /**
     * Добавляет идентификатор подзадачи к эпику
     */
    public void addSubtask(int id) {
        subtaskIds.add(id);
    }

    /**
     * Удаляет идентификатор подзадачи из эпика
     */
    public void removeSubtask(int id) {
        subtaskIds.remove(Integer.valueOf(id));
    }

    /**
     * Возвращает список идентификаторов всех подзадач эпика
     */
    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }
}
