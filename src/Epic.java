import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Subtask> listSubtask;

    public Epic(String nameTask, String descriptionTask, int idTask, TaskStatus typesOfStatuses) {
        super(nameTask, descriptionTask, idTask, typesOfStatuses);
        listSubtask = new ArrayList<>();
    }

    public Epic(String nameTask, String descriptionTask, int idTask, TaskStatus typesOfStatuses,
                Duration duration, LocalDateTime startTime) {
        super(nameTask, descriptionTask, idTask, typesOfStatuses, duration, startTime);
        listSubtask = new ArrayList<>();
    }

    @Override
    public LocalDateTime getEndTime() {
        if (listSubtask.isEmpty()) {
            return null;
        }
        return listSubtask.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public Duration getDuration() {
        if (listSubtask.isEmpty()) {
            return Duration.ZERO;
        }
        return listSubtask.stream()
                .map(Subtask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public LocalDateTime getStartTime() {
        if (listSubtask.isEmpty()) {
            return null;
        }
        return listSubtask.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    //Добавление подзадачи в список Epic задачи
    public void addInList(Subtask subtask) {
        listSubtask.add(subtask);
    }

    //Получение всех подзадач в списке Epic
    public ArrayList<Subtask> getSubtasks() {
        return listSubtask;
    }

    public void setSubtask(ArrayList<Subtask> listSubtask) {
        this.listSubtask = listSubtask;
    }
}
