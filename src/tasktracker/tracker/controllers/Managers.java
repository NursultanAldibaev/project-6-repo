package tasktracker.tracker.controllers;

/**
 * Утилитарный класс для получения реализаций менеджеров.
 */
public final class Managers {

    private Managers() {
        // запрет на создание экземпляра
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

