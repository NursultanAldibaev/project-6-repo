package service;

import model.Task;
import java.util.*;

/**
 * Реализация интерфейса HistoryManager.
 * Хранит историю просмотров задач в памяти с помощью двусвязного списка и HashMap.
 *
 * - Двусвязный список нужен для быстрого обхода истории (от head до tail).
 * - HashMap<id, Node> позволяет находить и удалять задачи из истории за O(1).
 */
public class InMemoryHistoryManager implements HistoryManager {

    /** Хранение ссылок на узлы списка по id задачи (для быстрого доступа). */
    private final Map<Integer, Node> nodeMap = new HashMap<>();

    /** Начало (head) и конец (tail) двусвязного списка. */
    private Node head;
    private Node tail;

    /**
     * Добавляет задачу в историю.
     * Если задача уже есть — удаляется старая запись и создаётся новая в конце списка.
     *
     * @param task задача для добавления в историю
     */
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        // если уже есть в истории — удаляем старую запись
        if (nodeMap.containsKey(task.getId())) {
            remove(task.getId());
        }

        // добавляем в конец списка
        linkLast(task);
    }

    /**
     * Удаляет задачу из истории по её id.
     * Используется при удалении задачи из основного менеджера.
     *
     * @param id идентификатор задачи
     */
    @Override
    public void remove(int id) {
        Node node = nodeMap.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    /**
     * Возвращает историю просмотров задач.
     * История возвращается в порядке от самой старой к самой новой.
     *
     * @return список просмотренных задач
     */
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

    // ======================= внутренние методы =======================

    /**
     * Добавляет задачу в конец двусвязного списка.
     *
     * @param task задача для добавления
     */
    private void linkLast(Task task) {
        Node newNode = new Node(task);

        if (tail == null) {
            // список пуст — новый элемент становится головой
            head = newNode;
        } else {
            // привязать новый узел к текущему хвосту
            tail.next = newNode;
            newNode.prev = tail;
        }

        // обновить хвост списка
        tail = newNode;

        // сохранить ссылку на узел в HashMap
        nodeMap.put(task.getId(), newNode);
    }

    /**
     * Удаляет узел из двусвязного списка.
     *
     * @param node узел для удаления
     */
    private void removeNode(Node node) {
        Node prev = node.prev;
        Node next = node.next;

        if (prev != null) {
            prev.next = next;
        } else {
            // если удаляется голова
            head = next;
        }

        if (next != null) {
            next.prev = prev;
        } else {
            // если удаляется хвост
            tail = prev;
        }
    }

    /**
     * Узел двусвязного списка.
     * Хранит ссылку на задачу и ссылки на предыдущий/следующий узлы.
     */
    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }
}
