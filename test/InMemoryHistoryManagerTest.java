import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    private static InMemoryHistoryManager historyManager;
    private static TaskManager taskManager;

    //убедитесь, что задачи, добавляемые в service.HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        taskManager = Managers.getDefault();
    }

    @Test
    public void saveTask() {
        taskManager.addTask("1", "2", TaskStatus.NEW);  // id 1
        taskManager.addEpic("1", "2", TaskStatus.NEW); // id 2
        taskManager.addSubtask("3", "5", TaskStatus.NEW, taskManager.getEpic(2)); //id 3

        Task task = taskManager.getTask(1);
        Epic epic = taskManager.getEpic(2);
        Subtask subtask = taskManager.getSubtask(3);

        historyManager.addHistory(taskManager.getTask(1));
        historyManager.addHistory(taskManager.getEpic(2));
        historyManager.addHistory(taskManager.getSubtask(3));

        //Меняем статусы задач
        taskManager.setStatusTask(taskManager.getTask(1).getId(), TaskStatus.IN_PROGRESS);
        taskManager.setStatusSubtask(taskManager.getSubtask(3).getId(), TaskStatus.IN_PROGRESS);
        //У model.Epic статус меняется автоматически, в зависимости от его подзадач. Теперь у него тоже In_PROGRESS

        //добавляем в history
        historyManager.addHistory(taskManager.getTask(1));
        historyManager.addHistory(taskManager.getEpic(2));
        historyManager.addHistory(taskManager.getSubtask(3));

        // сравниваем старые значения и новые в history
        // поменялись очередностью.
        assertEquals(task, historyManager.getHistory().get(2));
        assertEquals(epic, historyManager.getHistory().get(1));
        assertEquals(subtask, historyManager.getHistory().get(0));

    }

    @Test
    public void addTask() {
        taskManager.addTask("1", "2", TaskStatus.NEW);  // id 1
        taskManager.addEpic("1", "2", TaskStatus.NEW); // id 2
        taskManager.addSubtask("3", "5", TaskStatus.NEW, taskManager.getEpic(2)); //id 3

        historyManager.addHistory(taskManager.getTask(1));
        historyManager.addHistory(taskManager.getEpic(2));
        historyManager.addHistory(taskManager.getSubtask(3));

        //записали кол-во задач
        int sizeHistory = historyManager.getHistory().size();

        //добавляем в history те же задачи
        historyManager.addHistory(taskManager.getTask(1));
        historyManager.addHistory(taskManager.getEpic(2));
        historyManager.addHistory(taskManager.getSubtask(3));
        sizeHistory = historyManager.getHistory().size();

        //добавляем новые задачи
        taskManager.addTask("4", "4", TaskStatus.NEW);  // id 1
        taskManager.addEpic("5", "5", TaskStatus.NEW); // id 2
        taskManager.addSubtask("6", "6", TaskStatus.NEW, taskManager.getEpic(5)); //id 3
        historyManager.addHistory(taskManager.getTask(4));
        historyManager.addHistory(taskManager.getEpic(5));
        historyManager.addHistory(taskManager.getSubtask(6));
        //записали кол-во задач
        int newSizeHistory = historyManager.getHistory().size();

        //в первом их будет 3, во втором 6
        assertNotEquals(sizeHistory, historyManager.getHistory().size());
        assertEquals(newSizeHistory, historyManager.getHistory().size());

    }

    @Test
    public void dellTask() {
        taskManager.addTask("1", "2", TaskStatus.NEW);  // id 1
        taskManager.addEpic("1", "2", TaskStatus.NEW); // id 2
        taskManager.addSubtask("3", "5", TaskStatus.NEW, taskManager.getEpic(2)); //id 3
        taskManager.addTask("4", "4", TaskStatus.NEW);  // id 1
        taskManager.addEpic("5", "5", TaskStatus.NEW); // id 2
        taskManager.addSubtask("6", "6", TaskStatus.NEW, taskManager.getEpic(5)); //id 3

        //сохранили 6 задач
        historyManager.addHistory(taskManager.getTask(1));
        historyManager.addHistory(taskManager.getEpic(2));
        historyManager.addHistory(taskManager.getSubtask(3));
        historyManager.addHistory(taskManager.getTask(4));
        historyManager.addHistory(taskManager.getEpic(5));
        historyManager.addHistory(taskManager.getSubtask(6));

        //записали кол-во задач
        int SizeHistory = historyManager.getHistory().size();

        // Удалим 2 задачи
        historyManager.remove(1);
        historyManager.remove(3);

        //записали новый результат кол-во задач
        int newSizeHistory = historyManager.getHistory().size();

        assertNotEquals(SizeHistory, historyManager.getHistory().size());
        assertEquals(newSizeHistory, historyManager.getHistory().size());
    }

    @Test
    void testEmptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void testAddToHistory() {
        Task task = new Task("Задача", "Описание", 1, TaskStatus.NEW);
        historyManager.addHistory(task);

        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task, historyManager.getHistory().get(0));
    }

    @Test
    void testNoDuplicates() {
        Task task = new Task("Задача", "Описание", 1, TaskStatus.NEW);

        historyManager.addHistory(task);
        historyManager.addHistory(task); // Дубликат

        assertEquals(1, historyManager.getHistory().size());
    }

}