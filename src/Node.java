public class Node {
    int taskId;
    Node prev;
    Node next;
    Task task;

    public Node(int taskId) {
        this.taskId = taskId;

    }
}
