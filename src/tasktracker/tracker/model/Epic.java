package tracker.model;

/**
 * Класс для задач-эпиков.
 * Эпик объединяет несколько подзадач (Subtask).
 */
public class Epic extends Task {
    /**
     * Конструктор для создания эпика.
     *
     * @param name        название эпика
     * @param description описание эпика
     */
    public Epic(String name, String description) {
        super(name, description);
    }
}
