import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private static InMemoryTaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    //проверьте, что service.InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    public void addTestAndSearch() {
        taskManager.addTask("1", "2", TaskStatus.NEW); // id 1
        taskManager.addTask("1", "2", TaskStatus.NEW);  // id 2
        taskManager.addEpic("1", "2", TaskStatus.NEW); // id 3
        taskManager.addSubtask("3", "5", TaskStatus.NEW, taskManager.getEpic(3)); //id 4

        Task task = taskManager.getTask(1);
        Task taskTwo = taskManager.getTask(2);
        Epic epic = taskManager.getEpic(3);
        Subtask subtask = taskManager.getSubtask(4);

        assertEquals(4, taskManager.getAllTasks().size(), "Кол-во совпадает");
        assertEquals(task, taskManager.getTask(1), "Равны следовательно найден, найдена model.Task");
        assertEquals(taskTwo, taskManager.getTask(2), "Равны следовательно найден, найдена TaskTwo");
        assertEquals(epic, taskManager.getEpic(3), "Равны следовательно найден, найден model.Epic");
        assertEquals(subtask, taskManager.getSubtask(4), "Равны следовательно найден, найдена model.Subtask");
    }

    //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    public void noConflictId() {
        Task task = new Task("1", "2", 1, TaskStatus.NEW);  // присвоился id 1 в ручную
        taskManager.addTask("1", "2", TaskStatus.NEW); // присвоился id 1 автоматически

        Task tasktest = taskManager.getTask(task.getId());
        Task taskTwo = taskManager.getTask(1);
        assertEquals(tasktest.getId(), taskTwo.getId()); //"Id одинаковые, -> конфликта при создании нет, потому что задача с заданным id
        // не попадает в список где id авто. -> задачи созданные вручную не будут учавствовать.
    }
}