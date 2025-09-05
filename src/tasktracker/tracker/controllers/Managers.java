package tracker.controllers;

public class Managers {

    private Managers() {
        // приватный конструктор, чтобы запретить создание экземпляров
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
