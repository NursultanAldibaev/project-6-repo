package tasktracker;

import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.TaskManager;
import tracker.model.Task;
import tracker.model.Epic;
import tracker.model.Subtask;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Починить баг", "Ошибка в отчёте");
        Task task2 = new Task("Проверить отчёт", "После починки");

        int task1Id = manager.createTask(task1);
        int task2Id = manager.createTask(task2);

        Epic epic1 = new Epic("Рефакторинг TaskManager", "Сделать интерфейсом");
        int epic1Id = manager.createEpic(epic1);

        Subtask sub1 = new Subtask("Переделать TaskManager", "Сделать интерфейсом", epic1Id);
        Subtask sub2 = new Subtask("Добавить историю", "Через отдельный класс", epic1Id);

        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        System.out.println("Все задачи:");
        System.out.println(manager.getTask(task1Id));
        System.out.println(manager.getTask(task2Id));

        System.out.println("\nЭпик и его подзадачи:");
        System.out.println(manager.getEpic(epic1Id));
        for (Subtask subtask : manager.getAllSubtasks()) {
            if (subtask.getEpicId() == epic1Id) {
                System.out.println(subtask);
            }
        }
    }
}
