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
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public void addHistory(Task task) {

        if (taskMap.containsKey(task.getId())) {
            moveToHead(taskMap.get(task.getId()));
            return;
        }

        // Создаем новый узел
        Node newNode = new Node(task.getId());
        newNode.task = task;
        taskMap.put(task.getId(), newNode);

        addToHead(newNode);

    }

    public void printViewHistory() {
        if (head.next != null) {
            Node current = head.next;
            historyTask = new ArrayList<>();

            while (current != tail) {
                historyTask.add(current.task);
                current = current.next;
            }
        }
    }

    //Перепревязка Node
    private void addToHead(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
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
        head.next = null;
    }
}
