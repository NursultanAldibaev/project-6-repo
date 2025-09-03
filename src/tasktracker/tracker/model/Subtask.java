package tracker.model;

/**
 * Класс, представляющий подзадачу, которая относится к определённому эпику.
 * Наследуется от {@link Task}.
 */
public class Subtask extends Task {
    /** Идентификатор эпика, к которому относится подзадача */
    private int epicId;

    /**
     * Создаёт новую подзадачу.
     *
     * @param name        имя подзадачи
     * @param description описание подзадачи
     * @param status      статус подзадачи
     * @param epicId      идентификатор эпика, к которому она относится
     */
    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    /**
     * Возвращает идентификатор эпика, к которому относится подзадача.
     *
     * @return идентификатор эпика
     */
    public int getEpicId() {
        return epicId;
    }

    /**
     * Устанавливает идентификатор эпика, к которому относится подзадача.
     *
     * @param epicId идентификатор эпика
     */
    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    /**
     * Возвращает строковое представление подзадачи.
     *
     * @return строка с информацией о подзадаче
     */
    @Override
    public String toString() {
        return String.format("Subtask{id=%d, name='%s', description='%s', status=%s, epicId=%d}",
                id, name, description, status, epicId);
    }
}
