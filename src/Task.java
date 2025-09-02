public class Task {
    private final String nameTask;  //название
    private String descriptionTask; // описание
    private final int id; // id который не меняется
    private TaskStatus typesOfStatuses; //статус выполнения

    public Task(String nameTask, String descriptionTask, int id, TaskStatus typesOfStatuses) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.id = id;
        this.typesOfStatuses = typesOfStatuses;
    }

    public int getId() { //получение id
        return id;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public TaskStatus getStatus() {
        return typesOfStatuses;
    }
    public void setStatus(TaskStatus typesOfStatuses){
        this.typesOfStatuses = typesOfStatuses;
    }

    public String getNameTask() {
        return nameTask;
    }

}
