import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    protected abstract T createTaskManager();

    @BeforeEach
    void setUp() {
        taskManager = createTaskManager();
    }

    @Test
    void testAddAndGetTask() {
        taskManager.addTask("Тест задача", "Описание", TaskStatus.NEW);
        Task task = taskManager.getTask(1);

        assertNotNull(task);
        assertEquals("Тест задача", task.getName());
        assertEquals(TaskStatus.NEW, task.getStatus());
    }

    @Test
    void testAddAndGetEpic() {
        taskManager.addEpic("Тест эпик", "Описание", TaskStatus.NEW);
        Epic epic = taskManager.getEpic(1);

        assertNotNull(epic);
        assertEquals("Тест эпик", epic.getName());
        assertEquals(TaskStatus.NEW, epic.getStatus());
    }


    @Test
    void testDeleteTask() {
        taskManager.addTask("Задача", "Описание", TaskStatus.NEW);
        taskManager.deleteTask(1);

        assertNull(taskManager.getTask(1));
    }

    @Test
    void testGetAllTasks() {
        taskManager.addTask("Задача1", "Описание", TaskStatus.NEW);
        taskManager.addEpic("Эпик1", "Описание", TaskStatus.NEW);

        List<String> allTasks = taskManager.getAllTasks();

        assertEquals(2, allTasks.size());
        assertTrue(allTasks.contains("Задача1"));
        assertTrue(allTasks.contains("Эпик1"));
    }
}