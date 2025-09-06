package tasktracker;

import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.TaskManager;
import tracker.model.Subtask;
import tracker.model.Task;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        manager.createTask(task1);
        manager.createTask(task2);

        Subtask sub1 = new Subtask("Subtask 1", "Subdesc 1", 0);
        Subtask sub2 = new Subtask("Subtask 2", "Subdesc 2", 0);
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getSubtaskById(sub1.getId());
        manager.getTaskById(task1.getId()); // повтор, перемещается в конец

        System.out.println("История просмотров:");
        for (Task t : manager.getHistory()) {
            System.out.println(t);
        }

        manager.deleteTask(task2.getId());

        System.out.println("\nИстория после удаления Task 2:");
        for (Task t : manager.getHistory()) {
            System.out.println(t);
        }
    }
}
