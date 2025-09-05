package tasktracker;

import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.TaskManager;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Починить баг", "Ошибка в отчёте");
        Task task2 = new Task("Проверить отчёт", "После починки");

        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("Большая задача", "Нужно сделать много всего");
        int epic1Id = manager.createEpic(epic1);

        // ✅ убрали Status из конструктора Subtask
        Subtask sub1 = new Subtask("Переделать TaskManager", "Сделать интерфейсом", epic1Id);
        Subtask sub2 = new Subtask("Добавить историю", "Через отдельный класс", epic1Id);

        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        System.out.println("Задачи:");
        System.out.println(manager.getTask(task1.getId()));
        System.out.println(manager.getTask(task2.getId()));

        System.out.println("Эпики:");
        System.out.println(manager.getEpic(epic1Id));

        System.out.println("Подзадачи эпика:");
        for (Subtask s : manager.getEpicSubtasks(epic1Id)) {
            System.out.println(s);
        }

        System.out.println("История:");
        System.out.println(manager.getHistory());
    }
}
