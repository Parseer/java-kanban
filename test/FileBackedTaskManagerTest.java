import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @TempDir
    Path tempDir;

    private File testFile;

    @Override
    protected FileBackedTaskManager createTaskManager() {
        try {
            testFile = Files.createTempFile(tempDir, "test", ".csv").toFile();
            return new FileBackedTaskManager(testFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSaveAndLoadEmpty() {
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testFile);

        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    void testSaveAndLoadTasks() {
        // Создаем и сохраняем задачи
        taskManager.addTask("Задача", "Описание", TaskStatus.NEW);
        taskManager.addEpic("Эпик", "Описание", TaskStatus.NEW);

        // Загружаем из файла
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(testFile);

        assertEquals(2, loadedManager.getAllTasks().size());
        assertNotNull(loadedManager.getTask(1));
        assertNotNull(loadedManager.getEpic(2));
    }

    @Test
    void testSaveToFile() {
        taskManager.addTask("Тест", "Описание", TaskStatus.NEW);
        assertTrue(testFile.exists());
        assertTrue(testFile.length() > 0);
    }
}