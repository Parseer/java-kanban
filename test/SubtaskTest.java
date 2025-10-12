import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private static InMemoryTaskManager taskManager;

    @BeforeEach
    public void BeforeEach() {
        taskManager = new InMemoryTaskManager();
    }


    //создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void immutabilityOfSubtask() {
        taskManager.addEpic("1", "2", TaskStatus.NEW);
        String name = "1";
        String Description = "2";
        TaskStatus taskStatus = TaskStatus.NEW;
        int id = 2;

        taskManager.addSubtask("1", "2", TaskStatus.NEW, taskManager.getEpic(1)); //создаем задачу и добавляем в менеджер

        assertEquals(name, taskManager.getSubtask(2).getName(), "name задачи изменилось");
        assertEquals(Description, taskManager.getSubtask(2).getDescription(), "Description задачи изменилось");
        assertEquals(taskStatus, taskManager.getSubtask(2).getStatus(), "taskStatus задачи изменилось");
        assertEquals(id, taskManager.getSubtask(2).getId(), "id задачи изменилось");
        assertEquals(taskManager.getEpic(1), taskManager.getSubtask(2).getEpicSub(), "id задачи изменилось");
    }
}