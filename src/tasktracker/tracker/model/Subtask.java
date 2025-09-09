package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Класс подзадачи, связанной с эпиком
 */
public class Subtask extends Task {

    private final int epicId;

    /**
     * Конструктор подзадачи
     *
     * @param name        название подзадачи
     * @param description описание подзадачи
     * @param epicId      идентификатор эпика
     */
    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    // Дополнительный конструктор с временем/длительностью (если нужно)
    public Subtask(String name, String description, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    // 🔽 Новый метод — сохранение в CSV
    @Override
    public String toCsvString() {
        long durationMinutes = getDuration() == null ? -1 : getDuration().toMinutes();
        String start = getStartTime() == null ? "" : getStartTime().toString();
        return String.format("%d,%s,%s,%s,%s,%d,%d,%s",
                getId(), TaskType.SUBTASK, escapeCommas(getName()), getStatus(), escapeCommas(getDescription()),
                epicId, durationMinutes, start);
    }

    @Override
    public String toString() {
        return "Subtask{" +
               "id=" + getId() +
               ", name='" + getName() + '\'' +
               ", description='" + getDescription() + '\'' +
               ", status=" + getStatus() +
               ", epicId=" + epicId +
               ", duration=" + (getDuration() == null ? "null" : getDuration().toMinutes() + "m") +
               ", startTime=" + getStartTime() +
               '}';
    }
}
