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
     * Пересчитывает поля эпика (duration, startTime, endTime, status) на основе подзадач.
     *
     * @param subtasks мапа с подзадачами
     */
    public void updateFromSubtasks(java.util.Map<Integer, Subtask> subtasks) {
        if (subtaskIds.isEmpty()) {
            setStatus(Status.NEW);
            duration = null;
            startTime = null;
            endTime = null;
            return;
        }

        Duration total = Duration.ZERO;
        LocalDateTime minStart = null;
        LocalDateTime maxEnd = null;

        boolean hasNew = false;
        boolean hasDone = false;

        for (Integer id : subtaskIds) {
            Subtask s = subtasks.get(id);
            if (s == null) {
                continue;
            }

            // Статус эпика
            if (s.getStatus() == Status.NEW) {
                hasNew = true;
            } else if (s.getStatus() == Status.DONE) {
                hasDone = true;
            } else {
                hasNew = true;
                hasDone = true;
            }

            // Duration и время
            if (s.getDuration() != null) {
                total = total.plus(s.getDuration());
            }

            LocalDateTime sStart = s.getStartTime();
            LocalDateTime sEnd = s.getEndTime();

            if (sStart != null && (minStart == null || sStart.isBefore(minStart))) {
                minStart = sStart;
            }
            if (sEnd != null && (maxEnd == null || sEnd.isAfter(maxEnd))) {
                maxEnd = sEnd;
            }
        }

        // Обновляем статус эпика
        if (hasNew && hasDone) {
            setStatus(Status.IN_PROGRESS);
        } else if (hasDone && !hasNew) {
            setStatus(Status.DONE);
        } else {
            setStatus(Status.NEW);
        }

        duration = total.equals(Duration.ZERO) ? null : total;
        startTime = minStart;
        endTime = maxEnd;
    }

    @Override
    public String toCsvString() {
        long durationMinutes = (getDuration() == null) ? -1 : getDuration().toMinutes();
        String start = (getStartTime() == null) ? "" : getStartTime().toString();
        return String.format(
                "%d,%s,%s,%s,%s,%s,%d,%s",
                getId(),
                TaskType.EPIC,
                escapeCommas(getName()),
                getStatus(),
                escapeCommas(getDescription()),
                "",
                durationMinutes,
                start
        );
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskIds=" + subtaskIds +
                ", duration=" + ((duration == null) ? "null" : duration.toMinutes() + "m") +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
