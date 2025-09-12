import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private static InMemoryTaskManager taskManager;

    @BeforeEach
    public void BeforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    //создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void immutabilityOfEpic() {
        String name = "1";
        String Description = "2";
        TaskStatus taskStatus = TaskStatus.NEW;
        int id = 1;

        taskManager.addEpic("1", "2", TaskStatus.NEW); //создаем задачу и добавляем в менеджер
        assertEquals(name, taskManager.getEpic(1).getName(), "name задачи изменилось");
        assertEquals(Description, taskManager.getEpic(1).getDescription(), "Description задачи изменилось");
        assertEquals(taskStatus, taskManager.getEpic(1).getStatus(), "taskStatus задачи изменилось");
        assertEquals(id, taskManager.getEpic(1).getId(), "id задачи изменилось");
    }

}