public class Subtask extends Task{
    final Epic epic;

    public Subtask(String nameTask, String descriptionTask, int idTask, TypesOfStatuses typesOfStatuses,Epic epic) {
        super(nameTask, descriptionTask, idTask, typesOfStatuses);
        this.epic = epic;
    }

    public Epic getEpicSub(){
        return epic;
    }




}

