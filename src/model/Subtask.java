package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final Epic epic;

    public Subtask(String nameTask, String descriptionTask, int idTask, TaskStatus typesOfStatuses, Epic epic) {
        super(nameTask, descriptionTask, idTask, typesOfStatuses);
        this.epic = epic;
    }

    public Subtask(String nameTask, String descriptionTask, int idTask, TaskStatus typesOfStatuses,
                   Epic epic, Duration duration, LocalDateTime startTime) {
        super(nameTask, descriptionTask, idTask, typesOfStatuses, duration, startTime);
        this.epic = epic;
    }

    public Epic getEpicSub() { // получение model.Epic к которому пренадлежит
        return epic;
    }
}
