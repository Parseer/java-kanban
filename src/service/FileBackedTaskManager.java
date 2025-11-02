package service;

import model.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public FileBackedTaskManager(File file) {
        this.file = file;
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

    private void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic,duration,startTime\n");

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
            throw new ManagerSaveException("Ошибка в сохранении файла", e);
        }
    }

    //загрузка из файла
    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            if (!file.exists()) {
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

    public void addTask(String nameTask, String descriptionTask, TaskStatus typesOfStatuses,
                        Duration duration, LocalDateTime startTime) {
        counterId++;
        Task task = new Task(nameTask, descriptionTask, counterId, typesOfStatuses, duration, startTime);
        getTaskHashMap().put(counterId, task);
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

    public void addEpic(String nameTask, String descriptionTask, TaskStatus typesOfStatuses,
                        Duration duration, LocalDateTime startTime) {
        counterId++;
        Epic epic = new Epic(nameTask, descriptionTask, counterId, typesOfStatuses, duration, startTime);
        getEpicHashMap().put(counterId, epic);
        save();
    }

    public void addSubtask(String nameTask, String descriptionTask, TaskStatus typesOfStatuses,
                           Epic epic, Duration duration, LocalDateTime startTime) {
        counterId++;
        Subtask subtask = new Subtask(nameTask, descriptionTask, counterId, typesOfStatuses, epic, duration, startTime);
        getSubtaskHashMap().put(counterId, subtask);
        epic.addInList(subtask);
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

    public String toString(Task task) {
        TaskType type = getTaskType(task);
        String epicId = "";
        String durationStr = "";
        String startTimeStr = "";

        if (type == TaskType.SUBTASK) {
            epicId = String.valueOf(((Subtask) task).getEpicSub().getId());
        }

        if (task.getDuration() != null && !task.getDuration().isZero()) {
            durationStr = String.valueOf(task.getDuration().toMinutes());
        }

        if (task.getStartTime() != null) {
            startTimeStr = task.getStartTime().format(DATE_TIME_FORMATTER);
        }

        return String.format("%d,%s,%s,%s,%s,%s,%s,%s",
                task.getId(), type, task.getName(), task.getStatus(),
                task.getDescription(), epicId, durationStr, startTimeStr);
    }

    private Task fromString(String value) {
        String[] line = value.split(",");

        int id = Integer.parseInt(line[0]);
        TaskType type = TaskType.valueOf(line[1]);
        String name = line[2];
        TaskStatus status = TaskStatus.valueOf(line[3]);
        String descript = line[4];

        Duration duration = null;
        LocalDateTime startTime = null;

        if (line.length > 6 && !line[6].isEmpty()) {
            duration = Duration.ofMinutes(Long.parseLong(line[6]));
        }

        if (line.length > 7 && !line[7].isEmpty()) {
            startTime = LocalDateTime.parse(line[7], DATE_TIME_FORMATTER);
        }

        switch (type) {
            case TaskType.TASK:
                return new Task(name, descript, id, status, duration, startTime);
            case TaskType.EPIC:
                return new Epic(name, descript, id, status, duration, startTime);
            case TaskType.SUBTASK:
                int epicId = line.length > 5 && !line[5].isEmpty() ? Integer.parseInt(line[5]) : 0;
                Epic epic = getEpicHashMap().get(epicId);
                if (epic == null) {
                    throw new ManagerSaveException("Нет такой подзадачи " + id);
                }
                return new Subtask(name, descript, id, status, epic, duration, startTime);
            default:
                throw new ManagerSaveException("Неизвестный тип " + id);
        }
    }

}
