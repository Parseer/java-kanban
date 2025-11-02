import http.HttpTaskServer;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private HttpTaskServer server;
    private HttpClient client;
    private Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        server = new HttpTaskServer();
        server.start();
        client = HttpClient.newHttpClient();
        gson = new Gson();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    // Тесты для задач

    @Test
    void testCreateTask() throws IOException, InterruptedException {
        Map<String, Object> taskData = new HashMap<>();
        taskData.put("name", "Test Task");
        taskData.put("description", "Test Description");
        taskData.put("status", "NEW");

        String taskJson = gson.toJson(taskData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("создана"));
    }

    @Test
    void testGetAllTasks() throws IOException, InterruptedException {
        // создаем задачу
        createTestTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    void testGetTaskByIdNotFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/999"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    void testUpdateTask() throws IOException, InterruptedException {
        // создаем задачу
        int taskId = createTestTask();

        Map<String, Object> taskData = new HashMap<>();
        taskData.put("id", taskId);
        taskData.put("name", "Updated Task");
        taskData.put("description", "Updated Description");
        taskData.put("status", "IN_PROGRESS");

        String taskJson = gson.toJson(taskData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("обновлена"));
    }

    @Test
    void testDeleteTask() throws IOException, InterruptedException {
        // создаем задачу
        int taskId = createTestTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + taskId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void testCreateEpic() throws IOException, InterruptedException {
        Map<String, Object> epicData = new HashMap<>();
        epicData.put("name", "Test Epic");
        epicData.put("description", "Test Epic Description");
        epicData.put("status", "NEW");

        String epicJson = gson.toJson(epicData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("создан"));
    }

    @Test
    void testGetAllEpics() throws IOException, InterruptedException {
        // создаем эпик
        createTestEpic();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    void testGetAllSubtasks() throws IOException, InterruptedException {
        // эпик и подзадачу
        int epicId = createTestEpic();
        createTestSubtask(epicId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    @Test
    void testDeleteSubtask() throws IOException, InterruptedException {
        // эпик и подзадачу
        int epicId = createTestEpic();
        int subtaskId = createTestSubtask(epicId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subtaskId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void testGetPrioritizedTasks() throws IOException, InterruptedException {
        // несколько задач
        createTestTask();
        createTestTask();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotNull(response.body());
    }

    // неккоректный запрос

    @Test
    void testMethodNotAllowed() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .PUT(HttpRequest.BodyPublishers.ofString("{}"))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode());
    }

    @Test
    void testNotFoundEndpoint() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/unknown"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    private int createTestTask() throws IOException, InterruptedException {
        Map<String, Object> taskData = new HashMap<>();
        taskData.put("name", "Test Task");
        taskData.put("description", "Test Description");
        taskData.put("status", "NEW");

        String taskJson = gson.toJson(taskData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return 1;
    }

    private int createTestEpic() throws IOException, InterruptedException {
        Map<String, Object> epicData = new HashMap<>();
        epicData.put("name", "Test Epic");
        epicData.put("description", "Test Epic Description");
        epicData.put("status", "NEW");

        String epicJson = gson.toJson(epicData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .header("Content-Type", "application/json")
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        return 2;
    }

    private int createTestSubtask(int epicId) throws IOException, InterruptedException {
        Map<String, Object> subtaskData = new HashMap<>();
        subtaskData.put("name", "Test Subtask");
        subtaskData.put("description", "Test Subtask Description");
        subtaskData.put("status", "NEW");
        subtaskData.put("epicId", epicId);

        String subtaskJson = gson.toJson(subtaskData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .header("Content-Type", "application/json")
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        return 3;
    }

}