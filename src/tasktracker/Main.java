package tasktracker;

import tracker.controllers.InMemoryTaskManager;
import tracker.controllers.TaskManager;
import tracker.model.Subtask;
import tracker.model.Task;

public class Main { // скобка на той же строке

    public static void main(String[] args) { // скобка на той же строке
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        manager.createTask(task1);
        manager.createTask(task2);

        Subtask sub1 = new Subtask("Subtask 1", "Subdesc 1", 0);
        Subtask sub2 = new Subtask("Subtask 2", "Subdesc 2", 0);
        manager.createSubtask(sub1);
        manager.createSubtask(sub2);

        System.out.println("All tasks:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }

        System.out.println("All subtasks:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }
    }

}
