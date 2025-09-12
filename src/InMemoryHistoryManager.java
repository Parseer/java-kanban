import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{

    private final ArrayList<Task> historyTask = new ArrayList<>();
    private static final int MAX_SIZE = 10;


    @Override
    public void addHistory(Task task) {  //можно сделать через LinkedList, т.к он быстрее удаляет первый элемент, 3-2 в пользу Array по О-большому
        if (historyTask.size() >= MAX_SIZE) {
            historyTask.remove(0);
        }
        historyTask.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyTask;
    }
}
