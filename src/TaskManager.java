import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    //Создание Task задачи
    void addTask(String nameTask, String descriptionTask, TaskStatus typesOfStatuses);

    //Создание Epic задачи
    void addEpic(String nameTask, String descriptionTask, TaskStatus typesOfStatuses);

    //Создание Subtask задачи
    void addSubtask(String nameTask, String descriptionTask, TaskStatus typesOfStatuses, Epic epic);

    ArrayList<String> getAllTasks();

    // получение задач Task
    ArrayList<String> getTasks();

    // получение задач Epic
    ArrayList<String> getEpics();

    // получение задач Subtask
    ArrayList<String> getSubtasks();

    // Отчистка ArrayList
    void deleteAllTask();

    //получение по идентификатору
    Task getTask(int idTask);

    //получение по идентификатору
    Epic getEpic(int idTask);

    Subtask getSubtask(int idTask);

    //обновление данных
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    //удаление по id
    void deleteTask(int idTask);

    void deleteEpic(int idEpic);

    void deleteSubtask(int idSub);

    //получение списка всех подзадач Epic
    ArrayList<Subtask> getEpicList(int idEpic);

    // изменение статуса
    void setStatusTask(int idTask, TaskStatus typesOfStatuses);

    void setStatusSubtask(int idSubtask, TaskStatus typesOfStatuses);

    // проверка статуса для Epic
    void checkStatus(Epic epic);

    List<Task> getHistory();

    void addHistory(Task task);

    List<Task> getPrioritizedTasks();

    boolean hasTimeOverlap(Task task1, Task task2);

    boolean isTaskOverlapping(Task task);
}
