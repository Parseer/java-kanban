import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {
    int counterId = 0;
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    private final Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime,
                            Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(Task::getId)
    );

    @Override
    public void addTask(String nameTask, String descriptionTask, TaskStatus typesOfStatuses) {
        counterId++;
        Task task = new Task(nameTask, descriptionTask, counterId, typesOfStatuses);

        if (isTaskOverlapping(task)) {
            throw new IllegalArgumentException("Задача пересекается по времени с существующей задачей");
        }

        taskHashMap.put(counterId, task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    //Создание Epic задачи
    @Override
    public void addEpic(String nameTask, String descriptionTask, TaskStatus typesOfStatuses) {
        counterId++; //Получаем новый id путем прибавления 1
        Epic epic = new Epic(nameTask, descriptionTask, counterId, typesOfStatuses); //создаем новую задачу
        epicHashMap.put(counterId, epic); // добавляем задачу в список
    }

    public void addEpic(String nameTask, String descriptionTask, TaskStatus typesOfStatuses,
                        Duration duration, LocalDateTime startTime) {
        counterId++;
        Epic epic = new Epic(nameTask, descriptionTask, counterId, typesOfStatuses, duration, startTime);
        epicHashMap.put(counterId, epic);

        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        }
    }

    //Создание Subtask задачи
    @Override
    public void addSubtask(String nameTask, String descriptionTask, TaskStatus typesOfStatuses, Epic epic) {
        counterId++;
        Subtask subtask = new Subtask(nameTask, descriptionTask, counterId, typesOfStatuses, epic);

        if (isTaskOverlapping(subtask)) {
            counterId--; // Откатываем counterId, так как задача не добавлена
            throw new IllegalArgumentException("Подзадача пересекается по времени с существующей задачей");
        }

        subtaskHashMap.put(counterId, subtask);
        epic.addInList(subtask);

        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }

        // Обновляем статус эпика после добавления подзадачи
        checkStatus(epic);
    }

    public void addSubtask(String nameTask, String descriptionTask, TaskStatus typesOfStatuses,
                           Epic epic, Duration duration, LocalDateTime startTime) {
        counterId++;
        Subtask subtask = new Subtask(nameTask, descriptionTask, counterId, typesOfStatuses, epic, duration, startTime);

        if (isTaskOverlapping(subtask)) {
            counterId--; // Откатываем counterId, так как задача не добавлена
            throw new IllegalArgumentException("Подзадача пересекается по времени с существующей задачей");
        }

        subtaskHashMap.put(counterId, subtask);
        epic.addInList(subtask);

        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }

        // Обновляем статус эпика после добавления подзадачи
        checkStatus(epic);
    }


    // получение всех задач
    @Override
    public ArrayList<String> getAllTasks() {
        return Stream.concat(
                        Stream.concat(
                                taskHashMap.values().stream(),
                                epicHashMap.values().stream()
                        ),
                        subtaskHashMap.values().stream()
                )
                .map(Task::getName)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    // получение задач Task
    @Override
    public ArrayList<String> getTasks() {
        return taskHashMap.isEmpty() ?
                null :
                taskHashMap.values().stream()
                        .map(Task::getName)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

    // получение задач Epic
    @Override
    public ArrayList<String> getEpics() {
        return epicHashMap.isEmpty() ?
                null :
                epicHashMap.values().stream()
                        .map(Task::getName)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

    // получение задач Subtask
    @Override
    public ArrayList<String> getSubtasks() {
        return subtaskHashMap.isEmpty() ?
                null :
                subtaskHashMap.values().stream()
                        .map(Task::getName)
                        .collect(Collectors.toCollection(ArrayList::new));
    }

    // Отчистка ArrayList
    @Override
    public void deleteAllTask() {
        taskHashMap.clear();
        epicHashMap.clear();
        subtaskHashMap.clear();
        inMemoryHistoryManager.clear();
        prioritizedTasks.clear();
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
        if (taskHashMap.containsKey(task.getId())) {
            Task oldTask = taskHashMap.get(task.getId());
            prioritizedTasks.remove(oldTask);

            if (isTaskOverlapping(task)) {
                prioritizedTasks.add(oldTask); // Возвращаем старую задачу обратно
                throw new IllegalArgumentException("Обновленная задача пересекается по времени с существующей задачей");
            }

            taskHashMap.put(task.getId(), task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicHashMap.containsKey(epic.getId())) {
            Epic oldEpic = epicHashMap.get(epic.getId());
            prioritizedTasks.remove(oldEpic);

            if (epic.getStartTime() != null && isTaskOverlapping(epic)) {
                prioritizedTasks.add(oldEpic); // Возвращаем старый эпик обратно
                throw new IllegalArgumentException("Обновленный эпик пересекается по времени с существующей задачей");
            }

            epicHashMap.put(epic.getId(), epic);
            if (epic.getStartTime() != null) {
                prioritizedTasks.add(epic);
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtaskHashMap.containsKey(subtask.getId())) {
            Subtask oldSubtask = subtaskHashMap.get(subtask.getId());
            prioritizedTasks.remove(oldSubtask);

            if (isTaskOverlapping(subtask)) {
                prioritizedTasks.add(oldSubtask); // Возвращаем старую подзадачу обратно
                throw new IllegalArgumentException("Обновленная подзадача пересекается по времени с существующей задачей");
            }

            subtaskHashMap.put(subtask.getId(), subtask);
            if (subtask.getStartTime() != null) {
                prioritizedTasks.add(subtask);
            }

            checkStatus(subtask.getEpicSub()); // обновит статус Epic
        }
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
        Epic epic = epicHashMap.get(idEpic);
        if (epic != null) {
            // Удаляем все подзадачи эпика
            epic.getSubtasks().stream()
                    .map(Subtask::getId)
                    .forEach(id -> {
                        Subtask subtask = subtaskHashMap.remove(id);
                        if (subtask != null) {
                            prioritizedTasks.remove(subtask);
                        }
                        inMemoryHistoryManager.remove(id);
                    });

            epicHashMap.remove(idEpic);
            prioritizedTasks.remove(epic);
            inMemoryHistoryManager.remove(idEpic);
        }
    }

    @Override
    public void deleteSubtask(int idSub) {
        Subtask subtask = subtaskHashMap.get(idSub);
        if (subtask != null) {
            int idEpicSub = subtask.getEpicSub().getId();
            Epic epic = epicHashMap.get(idEpicSub);

            if (epic != null) {
                epic.getSubtasks().remove(subtask);
            }

            subtaskHashMap.remove(idSub);
            prioritizedTasks.remove(subtask);
            inMemoryHistoryManager.remove(idSub);

            if (epic != null) {
                checkStatus(epic);
            }
        }
    }

    //получение списка всех подзадач Epic
    @Override
    public ArrayList<Subtask> getEpicList(int idEpic) {
        Epic epic = epicHashMap.get(idEpic);
        return epic != null ? epic.getSubtasks() : null;
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
        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean allNew = epic.getSubtasks().stream()
                .allMatch(subtask -> subtask.getStatus() == TaskStatus.NEW);

        boolean allDone = epic.getSubtasks().stream()
                .allMatch(subtask -> subtask.getStatus() == TaskStatus.DONE);

        boolean anyInProgress = epic.getSubtasks().stream()
                .anyMatch(subtask -> subtask.getStatus() == TaskStatus.IN_PROGRESS);

        if (allNew) {
            epic.setStatus(TaskStatus.NEW);
        } else if (allDone) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }

        // Если есть хоть одна IN_PROGRESS, устанавливаем IN_PROGRESS
        if (anyInProgress) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
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

    // New
    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public boolean hasTimeOverlap(Task task1, Task task2) {
        if (task1 == task2) {
            return false; // Не считаем пересечением одну и ту же задачу
        }

        if (task1.getStartTime() == null || task2.getStartTime() == null ||
                task1.getEndTime() == null || task2.getEndTime() == null) {
            return false;
        }

        return task1.getStartTime().isBefore(task2.getEndTime()) &&
                task2.getStartTime().isBefore(task1.getEndTime());
    }

    @Override
    public boolean isTaskOverlapping(Task newTask) {
        if (newTask.getStartTime() == null || newTask.getEndTime() == null) {
            return false;
        }

        return prioritizedTasks.stream()
                .filter(existingTask -> !existingTask.equals(newTask)) // Исключаем саму задачу при обновлении
                .anyMatch(existingTask -> hasTimeOverlap(newTask, existingTask));
    }
}
