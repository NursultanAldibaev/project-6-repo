package tasktracker.tracker.controllers;

import tasktracker.tracker.model.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> historyMap = new HashMap<>();
    private Node head;
    private Node tail;

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) { this.task = task; }
    }

    @Override
    public void add(Task task) {
        remove(task.getId());
        linkLast(task);
    }

    private void linkLast(Task task) {
        Node node = new Node(task);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        historyMap.put(task.getId(), node);
    }

    private void remove(int id) {
        Node node = historyMap.remove(id);
        if (node == null) return;
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;
        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }
}
