package tasktracker;

import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.TaskManager;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.model.Epic;
import tracker.model.Status;

public class Main {

    public static void main(String[] args)
    {
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        manager.createTask(task1);
        manager.createTask(task2);

        Epic epic = new Epic("Epic 1", "Epic Description");
        int epicId = manager.createEpic(epic);

        Subtask sub1 = new Subtask("Subtask 1", "Subdesc 1", epicId);
        Subtask sub2 = new Subtask("Subtask 2", "Subdesc 2", epicId);
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        System.out.println("All tasks:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("All epics:");
        for (Epic e : manager.getAllEpics()) {
            System.out.println(e);
        }

        System.out.println("All subtasks:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }
    }

}
