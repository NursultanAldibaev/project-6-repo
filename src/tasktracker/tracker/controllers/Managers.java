package tracker.controllers;

/**
 * Утилитарный класс для получения стандартных реализаций менеджеров задач и истории.
 */
public class Managers {

    /**
     * Возвращает стандартную реализацию менеджера задач.
     *
     * @return {@link TaskManager} — реализация менеджера задач, хранящего данные в памяти
     */
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    /**
     * Возвращает стандартную реализацию менеджера истории.
     *
     * @return {@link HistoryManager} — реализация менеджера истории, хранящего данные в памяти
     */
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}