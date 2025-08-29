import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    int counterId = 0;
    HashMap<Integer, Task> taskHashMap;
    HashMap<Integer, Subtask> subtaskHashMap;
    HashMap<Integer, Epic> epicHashMap;
    ArrayList<Task> arrayListTask;


    //Создание Task задачи
    public void addTask(String nameTask, String descriptionTask, TypesOfStatuses typesOfStatuses) {
        counterId++; //Получаем новый id путем прибавления 1
        Task task = new Task(nameTask, descriptionTask, counterId, typesOfStatuses); //создаем новую задачу
        arrayListTask.add(task); //добавляем задачу в список общих задач
        taskHashMap.put(counterId,task); // добавляем задачу в список
    }

    //Создание Epic задачи
    public void addEpic(String nameTask, String descriptionTask, TypesOfStatuses typesOfStatuses) {
        counterId++; //Получаем новый id путем прибавления 1
        Epic epic = new Epic(nameTask, descriptionTask, counterId, typesOfStatuses); //создаем новую задачу
        arrayListTask.add(epic); //добавляем задачу в список общих задач
        epicHashMap.put(counterId,epic); // добавляем задачу в список
    }

    //Создание Subtask задачи
    public void addSubtask(String nameTask, String descriptionTask, TypesOfStatuses typesOfStatuses,Epic epic) {
        counterId++; //Получаем новый id путем прибавления 1
        Subtask subtask = new Subtask(nameTask, descriptionTask, counterId, typesOfStatuses,epic); //создаем новую задачу
        arrayListTask.add(subtask); //добавляем задачу в список общих задач
        subtaskHashMap.put(counterId,subtask); // добавляем задачу в список
    }

    // получение всех задач
    public ArrayList<Task> getListAllTask() {
        return arrayListTask;
    }

    // получение задач Task
    public ArrayList<Task> getListTask() {
        return new ArrayList<>(taskHashMap.values()); // лист всех Task
    }

    // получение задач Epic
    public ArrayList<Epic> getListTEpic() {
        return new ArrayList<>(epicHashMap.values()); // лист всех Task
    }

    // получение задач Subtask
    public ArrayList<Subtask> getListSubtask() {
        return new ArrayList<>(subtaskHashMap.values());
    }

    // Отчистка ArrayList
    public void dellAllTask() {
        arrayListTask.clear();
        taskHashMap.clear();
        epicHashMap.clear();
        subtaskHashMap.clear();
        System.gc();// для JVM
    }

    //получение по идентификатору
    public Object getId(int idTask) {
        for(Task task : arrayListTask){
            if(idTask == task.getId()){
                return task;
            }
        }
        return null;
    }

    //обновление данных
    public void upDateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void upDateEpic(Epic epic) {
        taskHashMap.put(epic.getId(), epic);
    }

    public void upDateSubtask(Subtask subtask) {
        taskHashMap.put(subtask.getId(), subtask);
    }




    //удаление по id
    public void dellById(int idTask) {
        arrayListTask.remove(idTask);
    }

    //получение списка всех подзадач Epic
    public ArrayList<Epic> getEpicList() {


        return
    }


}
