import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


class TaskTest {
    private static InMemoryTaskManager taskManager;

    @BeforeEach
    public void BeforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    //проверьте, что экземпляры класса Task равны друг другу, если равен их id;
    @Test
    public void isEqualToById() {
        taskManager.addTask("3", "8", TaskStatus.NEW);
        Task taskTwo = taskManager.getTask(1);
        assertEquals(taskManager.getTask(1), taskTwo, "Задачи не совпадают.");
    }

    //проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void isEqualToByIdTaskChildEpic() {
        taskManager.addEpic("3", "8", TaskStatus.NEW);
        Epic epic = taskManager.getEpic(1);
        assertEquals(taskManager.getEpic(1), epic, "Задачи не совпадают.");
    }

    //проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    public void isEqualToByIdTaskChildSubtest() {
        taskManager.addEpic("3", "8", TaskStatus.NEW);
        taskManager.addSubtask("3", "8", TaskStatus.NEW, taskManager.getEpic(1));
        Subtask subtask = taskManager.getSubtask(1);
        assertEquals(taskManager.getSubtask(1), subtask, "Задачи не совпадают.");
    }

    //создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void immutabilityOfTask() {
        String name = "1";
        String Description = "2";
        TaskStatus taskStatus = TaskStatus.NEW;
        int id = 1;

        taskManager.addTask("1", "2", TaskStatus.NEW); //создаем задачу и добавляем в менеджер

        assertEquals(name, taskManager.getTask(1).getName(), "name задачи изменилось");
        assertEquals(Description, taskManager.getTask(1).getDescription(), "Description задачи изменилось");
        assertEquals(taskStatus, taskManager.getTask(1).getStatus(), "taskStatus задачи изменилось");
        assertEquals(id, taskManager.getTask(1).getId(), "id задачи изменилось");
    }


}