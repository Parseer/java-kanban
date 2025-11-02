package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import model.TaskStatus;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            switch (method) {
                case "GET":
                    handleGet(exchange, path);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange, path);
                    break;
                default:
                    sendText(exchange, "Метод не поддерживается", 405);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGet(HttpExchange exchange, String path) throws IOException {
        if (path.equals("/tasks")) {
            sendSuccess(exchange, taskManager.getTasks());
        } else if (path.matches("/tasks/\\d+")) {
            int id = extractId(path);
            Task task = taskManager.getTask(id);
            if (task != null) {
                sendSuccess(exchange, task);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readText(exchange);

        try {
            Map<String, Object> requestData = parseJson(body, Map.class);

            String name = (String) requestData.get("name");
            String description = (String) requestData.get("description");
            String statusStr = (String) requestData.get("status");

            TaskStatus status = TaskStatus.valueOf(statusStr);

            Duration duration = null;
            LocalDateTime startTime = null;

            if (requestData.containsKey("duration")) {
                duration = Duration.ofMinutes(((Number) requestData.get("duration")).longValue());
            }

            if (requestData.containsKey("startTime")) {
                startTime = LocalDateTime.parse((String) requestData.get("startTime"));
            }

            // Проверяем, это создание или обновление
            Integer id = requestData.containsKey("id") ?
                    ((Number) requestData.get("id")).intValue() : 0;

            if (id == 0) {
                // Создание новой задачи
                if (duration != null && startTime != null) {
                    // Используем перегруженный метод с временем
                    taskManager.addTask(name, description, status, duration, startTime);
                } else {
                    taskManager.addTask(name, description, status);
                }
                sendCreated(exchange, "Задача создана");
            } else {
                // Обновление существующей задачи
                Task updatedTask;
                if (duration != null && startTime != null) {
                    updatedTask = new Task(name, description, id, status, duration, startTime);
                } else {
                    updatedTask = new Task(name, description, id, status);
                }
                taskManager.updateTask(updatedTask);
                sendCreated(exchange, "Задача обновлена");
            }

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("пересекается")) {
                sendHasInteractions(exchange);
            } else {
                sendBadRequest(exchange, "Неверные данные: " + e.getMessage());
            }
        } catch (Exception e) {
            sendBadRequest(exchange, "Ошибка в формате запроса: " + e.getMessage());
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        if (path.matches("/tasks/\\d+")) {
            int id = extractId(path);
            Task task = taskManager.getTask(id);
            if (task != null) {
                taskManager.deleteTask(id);
                sendText(exchange, "Задача удалена", 200);
            } else {
                sendNotFound(exchange);
            }
        } else if (path.equals("/tasks")) {
            taskManager.deleteAllTask();
            sendText(exchange, "Все задачи удалены", 200);
        } else {
            sendNotFound(exchange);
        }
    }

    private int extractId(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[2]);
    }
}