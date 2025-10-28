import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    //Дополнительное задание и тесты
    public static void main(String[] args) {

        File file = new File("tasks.csv");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        System.out.println("------- Создание задач -------");

        manager.addEpic("Эпик 1", "Описание эпика 1", TaskStatus.NEW);
        manager.addTask("Задача 1", "Описание задачи 1", TaskStatus.NEW);

        // Получаем  эпик для создания подзадачи
        Epic epic = manager.getEpic(1);
        if (epic != null) {
            manager.addSubtask("Sub 1", "Описание sub", TaskStatus.NEW, epic);
        }

        manager.setStatusTask(2, TaskStatus.IN_PROGRESS);

        System.out.println("Задачи сохранены в файл");
        System.out.println("Все задачи: " + manager.getAllTasks());

        System.out.println("---------- Загрузка из файла ---------");

        // Загружаем в новый менеджер
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        // Проверяем, что задачи загрузились
        System.out.println("Задачи: " + loadedManager.getAllTasks());

        // Проверяем задачи
        Task loadedTask = loadedManager.getTask(2);
        if (loadedTask != null) {
            System.out.println("Задачи: " + loadedTask.getName() + " - " + loadedTask.getStatus());
        }

        Epic loadedEpic = loadedManager.getEpic(1);
        if (loadedEpic != null) {
            System.out.println("Эпик: " + loadedEpic.getName() + " - " + loadedEpic.getStatus());
            System.out.println("Sub эпика: " + loadedEpic.getSubtasks().size());
        }
    }


    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        try (FileWriter writer = new FileWriter(file)) {

            writer.write("id,type,name,status,description,epic\n");

            for (Task task : getTaskHashMap().values()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getEpicHashMap().values()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtaskHashMap().values()) {
                writer.write(toString(subtask) + "\n");
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка в охранении файла", e);
        }
    }

    public String toString(Task task) {
        TaskType type = getTaskType(task);
        String epicId = "";

        if (type == TaskType.SUBTASK) {
            epicId = String.valueOf(((Subtask) task).getEpicSub().getId());
        }

        return String.format("%d,%s,%s,%s,%s,%s", task.getId(), type, task.getName(), task.getStatus(), task.getDescription(), epicId);
    }

    private TaskType getTaskType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof Subtask) {
            return TaskType.SUBTASK;
        } else {
            return TaskType.TASK;
        }
    }

    private Task fromString(String value) {
        String[] line = value.split(",");

        int id = Integer.parseInt(line[0]);
        TaskType type = TaskType.valueOf(line[1]);
        String name = line[2];
        TaskStatus status = TaskStatus.valueOf(line[3]);
        String descript = line[4];

        switch (type) {
            case TASK:
                return new Task(name, descript, id, status);
            case EPIC:
                return new Epic(name, descript, id, status);
            case SUBTASK:
                int epicId = line.length > 5 && !line[5].isEmpty() ? Integer.parseInt(line[5]) : 0;
                Epic epic = getEpicHashMap().get(epicId);
                if (epic == null) {
                    throw new ManagerSaveException("Нет такой подзадачи" + id);
                }
                return new Subtask(name, descript, id, status, epic);
            default:
                throw new ManagerSaveException("Неизвестный тип" + id);
        }
    }

    //загрузка из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            if(!file.exists()){
                return manager; //пустой
            }
            List<String> lines = Files.readAllLines(file.toPath());
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (!line.isEmpty()) {
                    Task task = manager.fromString(line);
                    manager.addTaskToMap(task);
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загурзки файла", e);
        }

        return manager;
    }

    private void addTaskToMap(Task task) {
        //добавим задачи в мапы
        if (task instanceof Epic) {
            getEpicHashMap().put(task.getId(), (Epic) task);
        } else if (task instanceof Subtask) {
            getSubtaskHashMap().put(task.getId(), (Subtask) task);
            Subtask subtask = (Subtask) task;
            subtask.getEpicSub().addInList(subtask);
        } else {
            getTaskHashMap().put(task.getId(), task);
        }
    }

    @Override
    public void addTask(String nameTask, String descriptionTask, TaskStatus typesOfStatuses) {
        super.addTask(nameTask, descriptionTask, typesOfStatuses);
        save();
    }

    @Override
    public void addEpic(String nameTask, String descriptionTask, TaskStatus typesOfStatuses) {
        super.addEpic(nameTask, descriptionTask, typesOfStatuses);
        save();
    }

    @Override
    public void addSubtask(String nameTask, String descriptionTask, TaskStatus typesOfStatuses, Epic epic) {
        super.addSubtask(nameTask, descriptionTask, typesOfStatuses, epic);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int idTask) {
        super.deleteTask(idTask);
        save();
    }

    @Override
    public void deleteEpic(int idEpic) {
        super.deleteEpic(idEpic);
        save();
    }

    @Override
    public void deleteSubtask(int idSub) {
        super.deleteSubtask(idSub);
        save();
    }
}
