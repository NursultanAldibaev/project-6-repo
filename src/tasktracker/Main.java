package tasktracker;

import tasktracker.tracker.controllers.InMemoryTaskManager;
import tasktracker.tracker.model.Task;
import tasktracker.tracker.model.Epic;
import tasktracker.tracker.model.Subtask;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Починить баг", "Ошибка в отчёте");
        Task task2 = new Task("Проверить отчёт", "После починки");
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic1 = new Epic("Большая задача", "Нужно сделать много всего");
        int epic1Id = manager.createEpic(epic1);

        Subtask sub1 = new Subtask("Переделать TaskManager", "Сделать интерфейсом", epic1Id);
        Subtask sub2 = new Subtask("Добавить историю", "Через отдельный класс", epic1Id);
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());
        System.out.println("Все подзадачи: " + manager.getAllSubtasks());
    }
}
