public class Subtask extends Task {
    private final Epic epic;

    public Subtask(String nameTask, String descriptionTask, int idTask, TaskStatus typesOfStatuses, Epic epic) {
        super(nameTask, descriptionTask, idTask, typesOfStatuses);
        this.epic = epic;
    }

    public Epic getEpicSub() { // получение Epic к которому пренадлежит
        return epic;
    }

}
