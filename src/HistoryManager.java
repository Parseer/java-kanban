import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    void addHistory(Task task);

    List<Task> getHistory();

    void remove(int id);

    void clear();
}
