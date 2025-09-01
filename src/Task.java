public class Task {
    final String nameTask;  //название
    String descriptionTask; // описание
    private final int id; // id который не меняется
    TypesOfStatuses typesOfStatuses; //статус выполнения

    public Task(String nameTask, String descriptionTask, int id, TypesOfStatuses typesOfStatuses) {
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

    public TypesOfStatuses getTypesOfStatuses() {
        return typesOfStatuses;
    }

    public String getNameTask() {
        return nameTask;
    }

}
