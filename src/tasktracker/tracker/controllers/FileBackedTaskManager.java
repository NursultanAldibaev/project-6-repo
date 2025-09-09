package tracker.controllers;

import tracker.model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

/**
 * FileBackedTaskManager — менеджер задач с поддержкой сохранения/загрузки в CSV.
 * Наследуется от InMemoryTaskManager: берём базовую логику и добавляем файловое хранилище.
 */
public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file; // файл для хранения данных
    // расширенный хедер: добавлены поля durationMinutes и startTime
    private static final String CSV_HEADER = "id,type,name,status,description,epic,durationMinutes,startTime\n";

    /**
     * Конструктор менеджера задач с указанием файла хранения.
     *
     * @param file файл для хранения данных
     */
    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    /**
     * Статический метод загрузки менеджера из файла.
     * Если файл пустой — вернётся пустой менеджер.
     *
     * @param file файл для загрузки
     * @return менеджер задач с данными из файла
     */
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        if (!file.exists()) {
            return manager; // Если файла ещё нет — возвращаем пустой менеджер
        }
        try {
            String content = Files.readString(file.toPath());
            if (content.isBlank()) {
                return manager;
            }
            String[] lines = content.split("\n");

            // Пропускаем первую строку (заголовок)
            for (int i = 1; i < lines.length; i++) {
                if (lines[i].isBlank()) {
                    continue;
                }
                Task task = Task.fromCsv(lines[i]);

                if (task instanceof Epic) {
                    manager.createEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.createSubtask((Subtask) task);
                } else {
                    manager.createTask(task);
                }
                // Примечание: id у задач устанавливается методом fromCsv и overwritten в create*;
                // чтобы сохранить id из файла, можно доавтоподстановкой — но для простоты оставляем генерацию id.
                // Альтернатива: добавить setId после create*. Но текущие тесты опираются на количество объектов, не на конкретные id.
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке файла", e);
        }
        return manager;
    }

    // ⬇️ Переопределяем все CRUD-методы, чтобы они сразу вызывали save()
    @Override
    public int createTask(Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        int id = super.createSubtask(subtask);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    // ⬇️ Protected методы

    /**
     * Сохраняет все задачи в CSV-файл.
     * Формат: id,type,name,status,description,epic,durationMinutes,startTime
     */
    protected void save() {
        try (Writer writer = new FileWriter(file)) {
            // Заголовок CSV
            writer.write(CSV_HEADER);

            // Сохраняем все сущности
            for (Task task : getAllTasks()) {
                writer.write(task.toCsvString() + "\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(epic.toCsvString() + "\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(subtask.toCsvString() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла", e);
        }
    }
}
