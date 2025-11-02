import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    //убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    private static TaskManager taskManager;
    private static HistoryManager historyManager;


    @BeforeAll
    public static void beforeAll() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void readyToWorkNull() {
        assertNotNull(taskManager.getAllTasks()); // Должен вернуть пустой лист
        assertNotNull(taskManager.getTasks()); //Вернет пустой список
        assertNotNull(taskManager.getEpics()); //Вернет пустой список
        assertNotNull(taskManager.getSubtasks()); //Вернет пустой список

    }

    @Test
    public void readyToWorkAdd() {
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

    @Test
    public void readyToWorkHistoryNull() {
        assertNotNull(historyManager.getHistory()); // Должен вернуть пустой лист, но не null
    }

    @Test
    public void readyToWorkAddHistory() {
        historyManager.addHistory(new Task("1", "2", 1, TaskStatus.NEW)); //
        historyManager.addHistory(new Task("1", "2", 2, TaskStatus.NEW)); //
        historyManager.addHistory(new Epic("1", "2", 3, TaskStatus.NEW)); //
        historyManager.addHistory(new Subtask("1", "2", 1, TaskStatus.NEW, taskManager.getEpic(3)));
        //теперь идет удаление повторов из истории
        assertEquals(3, historyManager.getHistory().size());
    }

    @Test
    public void delSubNotOldData() {
        //сохраним id подзадачи

        //удаляем задачи
        taskManager.deleteSubtask(4);
        taskManager.deleteTask(1);
        taskManager.deleteEpic(3);

        //удаленные задачи == null
        assertNull(taskManager.getSubtask(4));
        assertNull(taskManager.getEpic(3));
        assertNull(taskManager.getTask(1));
    }

    @Test
    public void noSubInsEpic() {
        Epic epicTwo = new Epic("1", "1", 10, TaskStatus.NEW);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание", 11, TaskStatus.NEW, epicTwo);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание", 12, TaskStatus.NEW, epicTwo);

        taskManager.addEpic(epicTwo.getName(), epicTwo.getDescription(), epicTwo.getStatus());
        taskManager.addSubtask(subtask1.getName(), subtask1.getDescription(), subtask1.getStatus(), epicTwo);
        taskManager.addSubtask(subtask1.getName(), subtask1.getDescription(), subtask1.getStatus(), epicTwo);


        taskManager.deleteSubtask(subtask1.getId());

        // проверяем что в эпике не осталось старого ID
        assertFalse(epicTwo.getSubtasks().contains(subtask1));
        assertEquals(2, epicTwo.getSubtasks().size());

    }
}