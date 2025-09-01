import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Subtask> listSubtask;

    public Epic(String nameTask, String descriptionTask, int idTask, TypesOfStatuses typesOfStatuses) {
        super(nameTask, descriptionTask, idTask, typesOfStatuses);
        listSubtask = new ArrayList<>();
    }

    //Добавление подзадачи в список Epic задачи
    public void addInListSubtask(Subtask subtask) {
        listSubtask.add(subtask);
    }

    //Получение всех подзадач в списке Epic
    public ArrayList<Subtask> getListSubInEpic() {
        return listSubtask;
    }


}
