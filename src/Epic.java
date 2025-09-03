import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> listSubtask;

    public Epic(String nameTask, String descriptionTask, int idTask, TaskStatus typesOfStatuses) {
        super(nameTask, descriptionTask, idTask, typesOfStatuses);
        listSubtask = new ArrayList<>();
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
