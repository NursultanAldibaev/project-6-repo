package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс Эпик
 */
public class Epic extends Task {

    private final List<Integer> subtaskIds = new ArrayList<>();

    // Для эпика duration/startTime/endTime вычисляемые (храним endTime для удобства)
    private Duration duration = Duration.ZERO;
    private LocalDateTime startTime = null;
    private LocalDateTime endTime = null;

    /**
     * Конструктор эпика
     *
     * @param name        название эпика
     * @param description описание эпика
     */
    public Epic(String name, String description) {
        super(name, description);
        setStatus(Status.NEW);
        // duration/startTime управляются через updateFromSubtasks
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

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Пересчитывает поля эпика (duration, startTime, endTime) на основе подзадач.
     * Этот метод должен вызываться менеджером задач при изменениях подзадач.
     *
     * @param subtasksProvider функция/лента для доступа к подзадачам (например, мапа)
     */
    public void updateFromSubtasks(java.util.Map<Integer, Subtask> subtasks) {
        Duration total = Duration.ZERO;
        LocalDateTime minStart = null;
        LocalDateTime maxEnd = null;

        for (Integer id : subtaskIds) {
            Subtask s = subtasks.get(id);
            if (s == null) {
                continue;
            }
            if (s.getDuration() != null) {
                total = total.plus(s.getDuration());
            }
            LocalDateTime sStart = s.getStartTime();
            LocalDateTime sEnd = s.getEndTime();
            if (sStart != null) {
                if (minStart == null || sStart.isBefore(minStart)) {
                    minStart = sStart;
                }
            }
            if (sEnd != null) {
                if (maxEnd == null || sEnd.isAfter(maxEnd)) {
                    maxEnd = sEnd;
                }
            }
        }

        this.duration = total.equals(Duration.ZERO) ? null : total;
        this.startTime = minStart;
        this.endTime = maxEnd;
    }

    // 🔽 Новый метод — сохранение в CSV
    @Override
    public String toCsvString() {
        long durationMinutes = (getDuration() == null) ? -1 : getDuration().toMinutes();
        String start = (getStartTime() == null) ? "" : getStartTime().toString();
        return String.format("%d,%s,%s,%s,%s,%s,%d,%s",
                getId(), TaskType.EPIC, escapeCommas(getName()), getStatus(), escapeCommas(getDescription()),
                "", durationMinutes, start);
    }

    @Override
    public String toString() {
        return "Epic{" +
               "id=" + getId() +
               ", name='" + getName() + '\'' +
               ", description='" + getDescription() + '\'' +
               ", status=" + getStatus() +
               ", subtaskIds=" + subtaskIds +
               ", duration=" + (duration == null ? "null" : duration.toMinutes()+"m") +
               ", startTime=" + startTime +
               ", endTime=" + endTime +
               '}';
    }
}
