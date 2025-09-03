package TaskTracker;

import tracker.model.*;
import tracker.model.Status;
import tracker.controllers.Managers;
import tracker.controllers.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        // Создание задач
        Task task1 = new Task("Починить баг", "Ошибка в отчёте", Status.NEW);
        Task task2 = new Task("Проверить отчёт", "После починки", Status.NEW);
        Epic epic1 = new Epic("Рефакторинг", "Улучшить структуру кода");
        Subtask sub1 = new Subtask("Переделать TaskManager", "Сделать интерфейсом", Status.NEW, 0);
        Subtask sub2 = new Subtask("Добавить историю", "Через отдельный класс", Status.NEW, 0);

        int task1Id = manager.createTask(task1);
        int task2Id = manager.createTask(task2);
        int epic1Id = manager.createEpic(epic1);

        // Связываем подзадачи с эпиком
        sub1.setEpicId(epic1Id);
        sub2.setEpicId(epic1Id);

        int sub1Id = manager.createSubtask(sub1);
        int sub2Id = manager.createSubtask(sub2);

        // Получаем задачи (добавляются в историю)
        manager.getTask(task1Id);
        manager.getEpic(epic1Id);
        manager.getSubtask(sub1Id);
        manager.getSubtask(sub2Id);
        manager.getTask(task2Id);
        manager.getEpic(epic1Id); // повторно

        printAll(manager);
    }

    private static void printAll(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("\nЭпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
            for (Subtask subtask : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("  --> " + subtask);
            }
        }

        System.out.println("\nПодзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("\nИстория:");
        for (Task t : manager.getHistory()) {
            System.out.println(t);
        }
    }
}
