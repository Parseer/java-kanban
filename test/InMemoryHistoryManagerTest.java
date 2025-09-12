import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    private static InMemoryHistoryManager historyManager;
    private static TaskManager taskManager;

    //убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
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
        //У Epic статус меняется автоматически, в зависимости от его подзадач. Теперь у него тоже In_PROGRESS

        //добавляем в history
        historyManager.addHistory(taskManager.getTask(1));
        historyManager.addHistory(taskManager.getEpic(2));
        historyManager.addHistory(taskManager.getSubtask(3));

        // сравниваем старые значения и новые в history
        assertEquals(task, historyManager.getHistory().get(0));
        assertEquals(epic, historyManager.getHistory().get(1));
        assertEquals(subtask, historyManager.getHistory().get(2));

    }

}