import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    int counterId = 0;
    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    HashMap<Integer, Epic> epicHashMap = new HashMap<>();


    //Создание Task задачи
    public void addTask(String nameTask, String descriptionTask, TypesOfStatuses typesOfStatuses) {
        counterId++; //Получаем новый id путем прибавления 1
        Task task = new Task(nameTask, descriptionTask, counterId, typesOfStatuses); //создаем новую задачу
        taskHashMap.put(counterId, task); // добавляем задачу в список
    }

    //Создание Epic задачи
    public void addEpic(String nameTask, String descriptionTask, TypesOfStatuses typesOfStatuses) {
        counterId++; //Получаем новый id путем прибавления 1
        Epic epic = new Epic(nameTask, descriptionTask, counterId, typesOfStatuses); //создаем новую задачу
        epicHashMap.put(counterId, epic); // добавляем задачу в список
    }

    //Создание Subtask задачи
    public void addSubtask(String nameTask, String descriptionTask, TypesOfStatuses typesOfStatuses, Epic epic) {
        counterId++; //Получаем новый id путем прибавления 1
        Subtask subtask = new Subtask(nameTask, descriptionTask, counterId, typesOfStatuses, epic); //создаем новую задачу
        subtaskHashMap.put(counterId, subtask); // добавляем задачу в список
        epic.addInListSubtask(subtask); //добавляется в лист эпика
    }

    // получение всех задач

    public ArrayList<String> getListAllTask() {
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
        ArrayList<String> arrayListAll = new ArrayList<>();
        for (Task task : taskHashMap.values()) {
            arrayListAll.add(task.getNameTask());
        }
        return arrayListAll;
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
    public void dellAllTask() {
        taskHashMap.clear();
        epicHashMap.clear();
        subtaskHashMap.clear();
        System.gc();// для JVM
    }


    //Общее получение пр идентификатору
    /*public Task getAllId(int idTask) {
        for (Task task : arrayListTask) {
            if (idTask == task.getId()) {
                return task;
            }
        }
        return null;
    }*/

    //получение по идентификатору
    public Task getIdTask(int idTask) {
        for (Task task : taskHashMap.values()) {
            if (idTask == task.getId()) {
                return task;
            }
        }
        return null;
    }

    //получение по идентификатору
    public Epic getIdEpic(int idTask) {
        for (Epic epic : epicHashMap.values()) {
            if (idTask == epic.getId()) {
                return epic;
            }
        }
        return null;
    }

    public Subtask getIdSubtask(int idTask) {
        for (Subtask subtask : subtaskHashMap.values()) {
            if (idTask == subtask.getId()) {
                return subtask;
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
        typesOfStatuses(subtask.getEpicSub()); //обновит статус Epic
    }

    //удаление по id
    public void dellByIdTask(int idTask) {
        taskHashMap.remove(idTask);
    }

    public void dellByIdEpic(int idEpic) {
        for (Subtask subtask : epicHashMap.get(idEpic).listSubtask) {
            subtaskHashMap.remove(subtask.getId());
        }
        epicHashMap.remove(idEpic);
    }

    public void dellByIdSubtask(int idSub) {
        //Epic epic = subtaskHashMap.get(idSub).getEpicSub();
        //epicHashMap.get(subtaskHashMap.get(idSub).getEpicSub()).listSubtask.remove(subtaskHashMap.get(idSub));
        int idEcpiSub = subtaskHashMap.get(idSub).getEpicSub().getId();
        epicHashMap.get(idEcpiSub).listSubtask.remove(subtaskHashMap.get(idSub));

        subtaskHashMap.remove(idSub);
        //epic.listSubtask.remove(subtaskHashMap.get(idSub));

        typesOfStatuses(epicHashMap.get(idEcpiSub));
    }


    //получение списка всех подзадач Epic
    public ArrayList<Subtask> getEpicList(int idEpic) {
        if (epicHashMap.containsKey(idEpic)) {
            return epicHashMap.get(idEpic).getListSubInEpic();
        } else {
            System.out.println("Это не эпик задач");
        }
        return null;
    }

    // изменение статуса
    public void setStatusTask(int idTask, TypesOfStatuses typesOfStatuses) {
        getIdTask(idTask).typesOfStatuses = typesOfStatuses; //ищем по id и меняем статус
    }

    public void setStatusSubtask(int idSubtask, TypesOfStatuses typesOfStatuses) {
        getIdSubtask(idSubtask).typesOfStatuses = typesOfStatuses; //ищем по id и меняем статус
        typesOfStatuses(getIdSubtask(idSubtask).getEpicSub()); // проверка и изменения статуса для Epic
    }


    // проверка статуса для Epic
    public void typesOfStatuses(Epic epic) {
        epic.typesOfStatuses = TypesOfStatuses.IN_PROGRESS;
        if (epic.getListSubInEpic().isEmpty()) { // проверка на пустосту
            epic.typesOfStatuses = TypesOfStatuses.NEW;
        }

        TypesOfStatuses saveStatus = epic.typesOfStatuses;

        for (Subtask subtask : epic.getListSubInEpic()) { //проверка на NEW
            if (subtask.getTypesOfStatuses() == TypesOfStatuses.NEW) {
                epic.typesOfStatuses = TypesOfStatuses.NEW;
            } else {
                epic.typesOfStatuses = saveStatus;
            }
        }
        for (Subtask subtask : epic.getListSubInEpic()) { //проверка на DONE
            if (subtask.getTypesOfStatuses() == TypesOfStatuses.DONE) {
                epic.typesOfStatuses = TypesOfStatuses.DONE;
            } else {
                epic.typesOfStatuses = saveStatus;
            }
        }
    }
}
