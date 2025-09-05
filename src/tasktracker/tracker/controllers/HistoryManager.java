package tasktracker.tracker.controllers;

import tasktracker.tracker.model.Task;
import java.util.List;

public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
}
