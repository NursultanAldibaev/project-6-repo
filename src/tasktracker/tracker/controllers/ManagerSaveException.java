package tracker.controllers;

/**
 * Исключение для ошибок сохранения/загрузки задач в файл.
 * Наследуется от RuntimeException, чтобы не заставлять обрабатывать через try-catch.
 */
public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}

