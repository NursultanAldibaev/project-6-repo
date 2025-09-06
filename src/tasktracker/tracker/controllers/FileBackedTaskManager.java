package tracker.controllers;

import tracker.model.*;

import java.io.*;
import java.nio.file.Files;

/**
 * FileBackedTaskManager — менеджер задач с поддержкой сохранения/загрузки в CSV.
 * Наследуется от InMemoryTaskManager: берём базовую логику и добавляем файловое хранилище.
 */
public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file; // файл для хранения данных

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    /**
     * Сохраняет все задачи в CSV-файл.
     * Формат: id,type,name,status,description,epic
     */
    protected void save() {
        try (Writer writer = new FileWriter(file)) {
            // Заголовок CSV
            writer.write("id,type,name,status,description,epic\n");

            // Сохраняем все сущности
            for (Task task : getAllTasks()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(toString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении файла", e);
        }
    }

    /**
     * Преобразует задачу в строку CSV.
     * Для подзадачи добавляется ID её эпика.
     */
    private String toString(Task task) {
        TaskType type;
        if (task instanceof Epic) {
            type = TaskType.EPIC;
        } else if (task instanceof Subtask) {
            type = TaskType.SUBTASK;
        } else {
            type = TaskType.TASK;
        }

        // Для Subtask указываем номер эпика
        String epicField = "";
        if (task instanceof Subtask) {
            epicField = String.valueOf(((Subtask) task).getEpicId());
        }

        return String.format("%d,%s,%s,%s,%s,%s",
                task.getId(),
                type,
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                epicField
        );
    }

    /**
     * Восстанавливает задачу из строки CSV.
     */
    private Task fromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String type = fields[1];
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];

        Task task;
        switch (type) {
            case "EPIC":
                task = new Epic(name, description);
                break;
            case "SUBTASK":
                int epicId = Integer.parseInt(fields[5]);
                task = new Subtask(name, description, epicId);
                break;
            default:
                task = new Task(name, description);
        }

        task.setId(id);
        task.setStatus(status);
        return task;
    }

    /**
     * Статический метод загрузки менеджера из файла.
     * Если файл пустой — вернётся пустой менеджер.
     */
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        if (!file.exists()) {
            return manager; // Если файла ещё нет — возвращаем пустой менеджер
        }

        try {
            String content = Files.readString(file.toPath());
            String[] lines = content.split("\n");

            // Пропускаем первую строку (заголовок)
            for (int i = 1; i < lines.length; i++) {
                if (lines[i].isBlank()) continue;

                Task task = manager.fromString(lines[i]);

                // Разбираем, что за тип задачи
                if (task instanceof Epic) {
                    manager.createEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.createSubtask((Subtask) task);
                } else {
                    manager.createTask(task);
                }
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
}
