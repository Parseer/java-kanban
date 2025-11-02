package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.Subtask;
import model.TaskStatus;
import service.TaskManager;

import java.io.IOException;
import java.util.Map;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubtasksHandler(TaskManager taskManager) {
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
        if (path.equals("/subtasks")) {
            sendSuccess(exchange, taskManager.getSubtasks());
        } else if (path.matches("/subtasks/\\d+")) {
            int id = extractId(path);
            Subtask subtask = taskManager.getSubtask(id);
            if (subtask != null) {
                sendSuccess(exchange, subtask);
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
            Integer epicId = ((Number) requestData.get("epicId")).intValue();

            TaskStatus status = TaskStatus.valueOf(statusStr);
            Epic epic = taskManager.getEpic(epicId);

            if (epic == null) {
                sendBadRequest(exchange, "Эпик с ID " + epicId + " не найден");
                return;
            }

            // Проверяем, это создание или обновление
            Integer id = requestData.containsKey("id") ?
                    ((Number) requestData.get("id")).intValue() : 0;

            if (id == 0) {
                // Создание новой подзадачи
                taskManager.addSubtask(name, description, status, epic);
                sendCreated(exchange, "Подзадача создана");
            } else {
                // Обновление существующей подзадачи
                Subtask subtask = new Subtask(name, description, id, status, epic);
                taskManager.updateSubtask(subtask);
                sendCreated(exchange, "Подзадача обновлена");
            }

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("пересекается")) {
                sendHasInteractions(exchange);
            } else {
                sendBadRequest(exchange, "Неверные данные: " + e.getMessage());
            }
        } catch (Exception e) {
            sendBadRequest(exchange, "Ошибка в формате запроса");
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        if (path.matches("/subtasks/\\d+")) {
            int id = extractId(path);
            taskManager.deleteSubtask(id);
            sendText(exchange, "Подзадача удалена", 200);
        } else {
            sendNotFound(exchange);
        }
    }

    private int extractId(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[2]);
    }
}