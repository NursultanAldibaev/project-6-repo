package tracker.controllers;

import tracker.model.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация истории просмотров задач в памяти.
 * <p>
 * Поддерживает:
 * <ul>
 *     <li>Неограниченную историю</li>
 *     <li>Удаление задач по ID</li>
 *     <li>Уникальность задач в истории (повторный просмотр перемещает задачу в конец)</li>
 * </ul>
 */
public class InMemoryHistoryManager implements HistoryManager {

    /**
     * Узел двусвязного списка, хранящий задачу.
     */
    private static class Node {
        final Task task;
        Node prev;
        Node next;

        Node(final Task task) {
            this.task = task;
        }
    }

    /** Словарь для быстрого доступа к узлам по ID задачи. */
    private final Map<Integer, Node> nodes = new HashMap<>();

    /** Голова двусвязного списка. */
    private Node head;

    /** Хвост двусвязного списка. */
    private Node tail;

    @Override
    public void add(final Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId());
        linkLast(task);
    }

    @Override
    public void remove(final int id) {
        final Node node = nodes.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {
        final List<Task> history = new ArrayList<>();
        Node current = head;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    /**
     * Добавляет задачу в конец двусвязного списка.
     *
     * @param task задача для добавления
     */
    private void linkLast(final Task task) {
        final Node newNode = new Node(task);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        nodes.put(task.getId(), newNode);
    }

    /**
     * Удаляет узел из двусвязного списка.
     *
     * @param node узел для удаления
     */
    private void removeNode(final Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        // Очистка ссылок для предотвращения утечек памяти
        node.prev = null;
        node.next = null;
    }
}
