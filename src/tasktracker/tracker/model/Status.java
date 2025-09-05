package tracker.model;

/**
 * Перечисление возможных статусов задачи.
 */
public enum Status {
    /** Новая задача. */
    NEW,

    /** Задача в процессе выполнения. */
    IN_PROGRESS,

    /** Задача завершена. */
    DONE
}
