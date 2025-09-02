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
    public ArrayList<Subtask> getList() {
        return listSubtask;
    }

    public void setList(ArrayList<Subtask> listSubtask) {
        this.listSubtask = listSubtask;
    }
}
