public class Node {
    private int taskId;
    private Node prev;
    private Node next;
    private Task task;

    public Node(int taskId) {
        this.taskId = taskId;

    }

    public int getTaskId() {
        return taskId;
    }

    public Node getPrev() {
        return prev;
    }

    public Node getNext() {
        return next;
    }

    public Task getTask() {
        return task;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
