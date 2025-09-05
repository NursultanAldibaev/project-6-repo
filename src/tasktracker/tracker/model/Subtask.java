package tracker.model;

/**
 * Подзадача, относящаяся к конкретному эпику.
 */
public class Subtask extends Task {
    /** Идентификатор эпика, к которому принадлежит подзадача. */
    private int epicId;

    /**
     * Конструктор для создания подзадачи.
     *
     * @param name        название подзадачи
     * @param description описание подзадачи
     * @param epicId      id связанного эпика
     */
    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
