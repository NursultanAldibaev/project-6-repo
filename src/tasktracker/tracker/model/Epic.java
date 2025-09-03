package tracker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий эпик — задачу, которая объединяет в себе несколько подзадач.
 */
public class Epic extends Task {
    /** Список идентификаторов подзадач, входящих в эпик */
    private final List<Integer> subtaskIds = new ArrayList<>();

    /**
     * Создает новый эпик с указанными именем и описанием.
     * Статус по умолчанию — {@code Status.NEW}.
     *
     * @param name        имя эпика
     * @param description описание эпика
     */
    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    /**
     * Добавляет идентификатор подзадачи к списку подзадач эпика.
     *
     * @param id идентификатор подзадачи
     */
    public void addSubtaskId(int id) {
        subtaskIds.add(id);
    }

    /**
     * Очищает список подзадач эпика.
     */
    public void clearSubtasks() {
        subtaskIds.clear();
    }

    /**
     * Возвращает список идентификаторов подзадач, принадлежащих эпику.
     *
     * @return список идентификаторов подзадач
     */
    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    /**
     * Возвращает строковое представление эпика.
     *
     * @return строка с описанием эпика и его подзадач
     */
    @Override
    public String toString() {
        return String.format("Epic{id=%d, name='%s', description='%s', status=%s, subtasks=%s}",
                id, name, description, status, subtaskIds.toString());
    }
}