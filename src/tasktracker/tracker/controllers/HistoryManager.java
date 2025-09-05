package tracker.controllers;

import tracker.model.Task;
import java.util.List;

// Интерфейс менеджера истории просмотров
public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
}
