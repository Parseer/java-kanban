import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int counterId = 0;
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    //Создание Task задачи
    @Override
    public void addTask(String nameTask, String descriptionTask, TaskStatus typesOfStatuses) {
        counterId++; //Получаем новый id путем прибавления 1
        Task task = new Task(nameTask, descriptionTask, counterId, typesOfStatuses); //создаем новую задачу
        taskHashMap.put(counterId, task); // добавляем задачу в список
    }

    //Создание Epic задачи
    @Override
    public void addEpic(String nameTask, String descriptionTask, TaskStatus typesOfStatuses) {
        counterId++; //Получаем новый id путем прибавления 1
        Epic epic = new Epic(nameTask, descriptionTask, counterId, typesOfStatuses); //создаем новую задачу
        epicHashMap.put(counterId, epic); // добавляем задачу в список
    }

    //Создание Subtask задачи
    @Override
    public void addSubtask(String nameTask, String descriptionTask, TaskStatus typesOfStatuses, Epic epic) {
        counterId++; //Получаем новый id путем прибавления 1
        Subtask subtask = new Subtask(nameTask, descriptionTask, counterId, typesOfStatuses, epic); //создаем новую задачу
        subtaskHashMap.put(counterId, subtask); // добавляем задачу в список
        epic.addInList(subtask); //добавляется в лист эпика
    }

    // получение всех задач
    @Override
    public ArrayList<String> getAllTasks() {
        ArrayList<String> arrayListAll = new ArrayList<>();

        for (Task task : taskHashMap.values()) {
            arrayListAll.add(task.getName());
        }
        for (Epic epic : epicHashMap.values()) {
            arrayListAll.add(epic.getName());
        }
        for (Subtask subtask : subtaskHashMap.values()) {
            arrayListAll.add(subtask.getName());
        }
        return arrayListAll;
    }

    // получение задач Task
    @Override
    public ArrayList<String> getTasks() {
        if (!(taskHashMap.isEmpty())) {
            ArrayList<String> tasks = new ArrayList<>();
            for (Task task : taskHashMap.values()) {
                tasks.add(task.getName());
            }
            return tasks;
        } else return null;
    }

    // получение задач Epic
    @Override
    public ArrayList<String> getEpics() {
        if (!(epicHashMap.isEmpty())) {
            ArrayList<String> arrayListAll = new ArrayList<>();
            for (Epic epic : epicHashMap.values()) {
                arrayListAll.add(epic.getName());
            }
            return arrayListAll; // лист всех Epic
        } else return null;
    }

    // получение задач Subtask
    @Override
    public ArrayList<String> getSubtasks() {
        ArrayList<String> arrayListAll = new ArrayList<>();
        if (!(subtaskHashMap.isEmpty())) {
            for (Subtask subtask : subtaskHashMap.values()) {
                arrayListAll.add(subtask.getName());
            }
            return arrayListAll; // лист всех Sub
        } else return null;
    }

    // Отчистка ArrayList
    @Override
    public void deleteAllTask() {
        taskHashMap.clear();
        epicHashMap.clear();
        subtaskHashMap.clear();
        inMemoryHistoryManager.clear();
        System.gc();// для JVM
    }

    //получение по идентификатору
    @Override
    public Task getTask(int idTask) {
        for (Task task : taskHashMap.values()) {
            if (idTask == task.getId()) {
                inMemoryHistoryManager.addHistory(task);//Добавление в списсок истории
                return task;
            }
        }
        return null;
    }

    //получение по идентификатору
    @Override
    public Epic getEpic(int idTask) {
        for (Epic epic : epicHashMap.values()) {
            if (idTask == epic.getId()) {
                inMemoryHistoryManager.addHistory(epic); //Добавление в списсок истории
                return epic;
            }
        }
        return null;
    }

    @Override
    public Subtask getSubtask(int idTask) {
        for (Subtask subtask : subtaskHashMap.values()) {
            if (idTask == subtask.getId()) {
                inMemoryHistoryManager.addHistory(subtask); //Добавление в списсок истории
                return subtask;
            }
        }
        return null;
    }

    //обновление данных
    @Override
    public void updateTask(Task task) {
        taskHashMap.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        taskHashMap.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        taskHashMap.put(subtask.getId(), subtask);
        checkStatus(subtask.getEpicSub()); //обновит статус Epic
    }

    //удаление по id
    @Override
    public void deleteTask(int idTask) {
        if (taskHashMap.containsKey(idTask)) {
            taskHashMap.remove(idTask);
            inMemoryHistoryManager.remove(idTask);
        }
    }

    @Override
    public void deleteEpic(int idEpic) {
        if (epicHashMap.containsKey(idEpic)) {
            for (Subtask subtask : epicHashMap.get(idEpic).getSubtasks()) {
                subtaskHashMap.remove(subtask.getId());
                inMemoryHistoryManager.remove(subtask.getId());
            }
            epicHashMap.remove(idEpic);
            inMemoryHistoryManager.remove(idEpic);
        }
    }

    @Override
    public void deleteSubtask(int idSub) {
        int idEpicSub;
        if (subtaskHashMap.containsKey(idSub)) {
            idEpicSub = subtaskHashMap.get(idSub).getEpicSub().getId();
            epicHashMap.get(idEpicSub).getSubtasks().remove(subtaskHashMap.get(idSub));
            inMemoryHistoryManager.remove(idSub);
            subtaskHashMap.remove(idSub);
            checkStatus(epicHashMap.get(idEpicSub));
        }
    }

    //получение списка всех подзадач Epic
    @Override
    public ArrayList<Subtask> getEpicList(int idEpic) {
        if (epicHashMap.containsKey(idEpic)) {
            return epicHashMap.get(idEpic).getSubtasks();
        }
        return null;
    }

    // изменение статуса
    @Override
    public void setStatusTask(int idTask, TaskStatus typesOfStatuses) {
        if (taskHashMap.containsKey(idTask)) {
            getTask(idTask).setStatus(typesOfStatuses);  //ищем по id и меняем статус
        }
    }

    @Override
    public void setStatusSubtask(int idSubtask, TaskStatus typesOfStatuses) {
        if (subtaskHashMap.containsKey(idSubtask)) {
            getSubtask(idSubtask).setStatus(typesOfStatuses); //ищем по id и меняем статус
            checkStatus(getSubtask(idSubtask).getEpicSub()); // проверка и изменения статуса для Epic
        }
    }

    // проверка статуса для Epic
    @Override
    public void checkStatus(Epic epic) {
        epic.setStatus(TaskStatus.IN_PROGRESS);
        if (epic.getSubtasks().isEmpty()) { // проверка на пустосту
            epic.setStatus(TaskStatus.NEW);
        }

        TaskStatus saveStatus = epic.getStatus();

        for (Subtask subtask : epic.getSubtasks()) { //проверка на NEW
            if (subtask.getStatus() == TaskStatus.NEW) {
                epic.setStatus(TaskStatus.NEW);
            } else {
                epic.setStatus(saveStatus);
            }
        }
        for (Subtask subtask : epic.getSubtasks()) { //проверка на DONE
            if (subtask.getStatus() == TaskStatus.DONE) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(saveStatus);
            }
        }
    }

    @Override
    public void addHistory(Task task) {
        inMemoryHistoryManager.addHistory(task);
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }

    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    public HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    public HashMap<Integer, Subtask> getSubtaskHashMap() {
        return subtaskHashMap;
    }
}
