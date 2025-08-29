public class Task {
    final String nameTask;  //название
    String descriptionTask; // описание
    private final int idTask; // id который не меняется
    TypesOfStatuses typesOfStatuses; //статус выполнения

    public Task(String nameTask, String descriptionTask, int idTask, TypesOfStatuses typesOfStatuses) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.idTask = idTask;
        this.typesOfStatuses = typesOfStatuses;
    }

    public int getId(){ //получение id
        return idTask;
    }

    public void setDescriptionTask(String descriptionTask){
        this.descriptionTask = descriptionTask;
    }





}
