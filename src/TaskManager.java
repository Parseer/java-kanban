import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int counterId = 0;
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();


    //Создание Task задачи
    public void addTask(String nameTask, String descriptionTask, TaskStatus typesOfStatuses) {
        counterId++; //Получаем новый id путем прибавления 1
        Task task = new Task(nameTask, descriptionTask, counterId, typesOfStatuses); //создаем новую задачу
        taskHashMap.put(counterId, task); // добавляем задачу в список
    }

    //Создание Epic задачи
    public void addEpic(String nameTask, String descriptionTask, TaskStatus typesOfStatuses) {
        counterId++; //Получаем новый id путем прибавления 1
        Epic epic = new Epic(nameTask, descriptionTask, counterId, typesOfStatuses); //создаем новую задачу
        epicHashMap.put(counterId, epic); // добавляем задачу в список
    }

    //Создание Subtask задачи
    public void addSubtask(String nameTask, String descriptionTask, TaskStatus typesOfStatuses, Epic epic) {
        counterId++; //Получаем новый id путем прибавления 1
        Subtask subtask = new Subtask(nameTask, descriptionTask, counterId, typesOfStatuses, epic); //создаем новую задачу
        subtaskHashMap.put(counterId, subtask); // добавляем задачу в список
        epic.addInList(subtask); //добавляется в лист эпика
    }

    // получение всех задач

    public ArrayList<String> getTasks() {
        ArrayList<String> arrayListAll = new ArrayList<>();

        for (Task task : taskHashMap.values()) {
            arrayListAll.add(task.getNameTask());
        }
        for (Epic epic : epicHashMap.values()) {
            arrayListAll.add(epic.getNameTask());
        }
        for (Subtask subtask : subtaskHashMap.values()) {
            arrayListAll.add(subtask.getNameTask());
        }
        return arrayListAll;
    }

    // получение задач Task
    public ArrayList<String> getListTask() {
        ArrayList<String> tasks = new ArrayList<>();
        for (Task task : taskHashMap.values()) {
            tasks.add(task.getNameTask());
        }
        return tasks;
    }

    // получение задач Epic
    public ArrayList<String> getListTEpic() {
        ArrayList<String> arrayListAll = new ArrayList<>();
        for (Epic epic : epicHashMap.values()) {
            arrayListAll.add(epic.getNameTask());
        }
        return arrayListAll; // лист всех Epic
    }

    // получение задач Subtask
    public ArrayList<String> getListSubtask() {
        ArrayList<String> arrayListAll = new ArrayList<>();
        for (Subtask subtask : subtaskHashMap.values()) {
            arrayListAll.add(subtask.getNameTask());
        }
        return arrayListAll; // лист всех Sub
    }


    // Отчистка ArrayList
    public void deleteAllTask() {
        taskHashMap.clear();
        epicHashMap.clear();
        subtaskHashMap.clear();
        System.gc();// для JVM
    }


    //получение по идентификатору
    public Task getTask(int idTask) {
        for (Task task : taskHashMap.values()) {
            if (idTask == task.getId()) {
                return task;
            }
        }
        return null;
    }

    //получение по идентификатору
    public Epic getEpic(int idTask) {
        for (Epic epic : epicHashMap.values()) {
            if (idTask == epic.getId()) {
                return epic;
            }
        }
        return null;
    }

    public Subtask getSubtask(int idTask) {
        for (Subtask subtask : subtaskHashMap.values()) {
            if (idTask == subtask.getId()) {
                return subtask;
            }
        }
        return null;
    }

    //обновление данных
    public void updateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        taskHashMap.put(epic.getId(), epic);
    }

    public void updateSubtask(Subtask subtask) {
        taskHashMap.put(subtask.getId(), subtask);
        typesOfStatuses(subtask.getEpicSub()); //обновит статус Epic
    }

    //удаление по id
    public void deleteTask(int idTask) {
        taskHashMap.remove(idTask);
    }

    public void deleteEpic(int idEpic) {
        for (Subtask subtask : epicHashMap.get(idEpic).getList()) {
            subtaskHashMap.remove(subtask.getId());
        }
        epicHashMap.remove(idEpic);
    }

    public void deleteSubtask(int idSub) {
        int idEpicSub;
        if(subtaskHashMap.containsKey(idSub)){
            idEpicSub = subtaskHashMap.get(idSub).getEpicSub().getId();
            epicHashMap.get(idEpicSub).getList().remove(subtaskHashMap.get(idSub));
            subtaskHashMap.remove(idSub);
            typesOfStatuses(epicHashMap.get(idEpicSub));
        }
    }


    //получение списка всех подзадач Epic
    public ArrayList<Subtask> getEpicList(int idEpic) {
        if (epicHashMap.containsKey(idEpic)) {
            return epicHashMap.get(idEpic).getList();
        }
        return null;
    }

    // изменение статуса
    public void setStatusTask(int idTask, TaskStatus typesOfStatuses) {
        getTask(idTask).setStatus(typesOfStatuses);  //ищем по id и меняем статус
    }

    public void setStatusSubtask(int idSubtask, TaskStatus typesOfStatuses) {
        getSubtask(idSubtask).setStatus(typesOfStatuses); //ищем по id и меняем статус
        typesOfStatuses(getSubtask(idSubtask).getEpicSub()); // проверка и изменения статуса для Epic
    }


    // проверка статуса для Epic
    public void typesOfStatuses(Epic epic) {
        epic.setStatus(TaskStatus.IN_PROGRESS);
        if (epic.getList().isEmpty()) { // проверка на пустосту
            epic.setStatus(TaskStatus.NEW);
        }

        TaskStatus saveStatus = epic.getStatus();

        for (Subtask subtask : epic.getList()) { //проверка на NEW
            if (subtask.getStatus() == TaskStatus.NEW) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                epic.setStatus(saveStatus);
            }
        }
        for (Subtask subtask : epic.getList()) { //проверка на DONE
            if (subtask.getStatus() == TaskStatus.DONE) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(saveStatus);
            }
        }
    }
}
