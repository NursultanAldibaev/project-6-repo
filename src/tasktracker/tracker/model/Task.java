package tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Класс задачи Task
 */
public class Task {

    private int id;
    private String name;
    private String description;
    private Status status;

    // Новые поля
    private Duration duration; // может быть null
    private LocalDateTime startTime; // может быть null

    // Формат для сериализации/десериализации времени
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Конструктор задачи (совместим с прежними тестами).
     *
     * @param name        название задачи
     * @param description описание задачи
     */
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = null;
        this.startTime = null;
    }

    // Дополнительный конструктор (если нужно задавать время/длительность при создании)
    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
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

    // Новые геттеры/сеттеры
    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Возвращает рассчитанное время окончания задачи.
     * Если startTime или duration == null, возвращает null.
     */
    public LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return null;
        }
        return startTime.plus(duration);
    }

    // 🔽 Новый метод — сохранение в CSV
    public String toCsvString() {
        // Формат: id,type,name,status,description,epic,durationMinutes,startTime
        long durationMinutes = duration == null ? -1 : duration.toMinutes();
        String start = startTime == null ? "" : startTime.format(DATE_TIME_FORMATTER);
        return String.format("%d,%s,%s,%s,%s,%s,%d,%s",
                id, TaskType.TASK, escapeCommas(name), status, escapeCommas(description), "", durationMinutes, start);
    }

    // Вспомогательный метод: экранируем запятые в полях (простая мера)
    protected String escapeCommas(String s) {
        return s == null ? "" : s.replace(",", " ");
    }

    // 🔽 Новый метод — восстановление из CSV
    public static Task fromCsv(String line) {
        // Разбиваем на 8 полей (но описание/имя могут иметь пробелы)
        // Полагаемся на формат: id,type,name,status,description,epic,durationMinutes,startTime
        String[] fields = line.split(",", -1);
        // безопасная обработка
        int id = Integer.parseInt(fields[0]);
        String type = fields[1];
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];
        String epicField = fields.length > 5 ? fields[5] : "";
        long durationMinutes = -1;
        if (fields.length > 6 && !fields[6].isEmpty()) {
            try {
                durationMinutes = Long.parseLong(fields[6]);
            } catch (NumberFormatException ignored) {
            }
        }
        String startField = fields.length > 7 ? fields[7] : "";

        Task task;
        switch (type) {
            case "EPIC" -> {
                task = new Epic(name, description);
            }
            case "SUBTASK" -> {
                int epicId = -1;
                if (!epicField.isEmpty()) {
                    epicId = Integer.parseInt(epicField);
                }
                task = new Subtask(name, description, epicId);
            }
            default -> task = new Task(name, description);
        }

        task.setId(id);
        task.setStatus(status);

        if (durationMinutes >= 0) {
            task.setDuration(Duration.ofMinutes(durationMinutes));
        } else {
            task.setDuration(null);
        }

        if (!startField.isBlank()) {
            task.setStartTime(LocalDateTime.parse(startField, DATE_TIME_FORMATTER));
        } else {
            task.setStartTime(null);
        }

        return task;
    }

    @Override
    public String toString() {
        return "Task{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", status=" + status +
               ", duration=" + (duration == null ? "null" : duration.toMinutes() + "m") +
               ", startTime=" + startTime +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return id == task.id &&
               Objects.equals(name, task.name) &&
               Objects.equals(description, task.description) &&
               status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }
}
