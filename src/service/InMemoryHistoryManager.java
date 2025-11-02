package service;

import model.Task;
import util.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private ArrayList<Task> historyTask;

    private HashMap<Integer, Node> taskMap = new HashMap<>();
    private final Node head;
    private final Node tail;


    public InMemoryHistoryManager() {
        taskMap = new HashMap<>();
        head = new Node(-1); // фиктивная голова
        tail = new Node(-1); // фиктивный хвост
        head.setNext(tail);
        tail.setPrev(head);
    }

    @Override
    public void addHistory(Task task) {

        if (taskMap.containsKey(task.getId())) {
            moveToHead(taskMap.get(task.getId()));
            return;
        }

        // Создаем новый узел
        Node newNode = new Node(task.getId());
        newNode.setTask(task);
        taskMap.put(task.getId(), newNode);
        addToHead(newNode);

    }

    public void printViewHistory() {
        if (head.getNext() != null) {
            Node current = head.getNext();
            historyTask = new ArrayList<>();

            while (current != tail) {
                historyTask.add(current.getTask());
                current = current.getNext();
            }
        }
    }

    //Перепревязка util.Node
    private void addToHead(Node node) {
        node.setPrev(head);
        node.setNext(head.getNext());
        head.getNext().setPrev(node);
        head.setNext(node);
    }

    private void removeNode(Node node) {
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
    }

    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }

    @Override
    public ArrayList<Task> getHistory() {
        printViewHistory();
        return historyTask;
    }

    @Override
    public void remove(int id) {
        if (!taskMap.containsKey(id)) {
            return; // Задача не найдена
        }

        Node nodeToRemove = taskMap.get(id);

        // Удаляем из списка
        removeNode(nodeToRemove);

        // Удаляем из HashMap
        taskMap.remove(id);
    }

    //Полная отчистка
    public void clear() {
        taskMap.clear();
        historyTask.clear();
        head.setNext(null);
    }
}
