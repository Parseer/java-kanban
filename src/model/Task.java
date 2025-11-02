package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private final String nameTask;  //название
    private String descriptionTask; // описание
    private final int id; // id который не меняется
    private TaskStatus typesOfStatuses; //статус выполнения
    private Duration duration;
    private LocalDateTime localDateTime;

    public Task(String nameTask, String descriptionTask, int id, TaskStatus typesOfStatuses) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.id = id;
        this.typesOfStatuses = typesOfStatuses;
        this.duration = null;
        this.localDateTime = null;
    }

    public Task(String nameTask, String descriptionTask, int id, TaskStatus typesOfStatuses, Duration duration, LocalDateTime localDateTime) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.id = id;
        this.typesOfStatuses = typesOfStatuses;
        this.duration = duration != null ? duration : Duration.ZERO;
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getEndTime() {
        if (localDateTime == null || duration == null) {
            return null;
        }
        return localDateTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return localDateTime;
    }

    public int getId() { //получение id
        return id;
    }

    public String getDescription() {
        return descriptionTask;
    }

    public TaskStatus getStatus() {
        return typesOfStatuses;
    }

    public void setStatus(TaskStatus typesOfStatuses) {
        this.typesOfStatuses = typesOfStatuses;
    }

    public String getName() {
        return nameTask;
    }
}
