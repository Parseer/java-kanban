package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.TaskStatus;
import service.TaskManager;

import java.io.IOException;
import java.util.Map;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
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
        if (path.equals("/epics")) {
            sendSuccess(exchange, taskManager.getEpics());
        } else if (path.matches("/epics/\\d+")) {
            int id = extractId(path);
            Epic epic = taskManager.getEpic(id);
            if (epic != null) {
                sendSuccess(exchange, epic);
            } else {
                sendNotFound(exchange);
            }
        } else if (path.matches("/epics/\\d+/subtasks")) {
            int epicId = extractId(path);
            Epic epic = taskManager.getEpic(epicId);
            if (epic != null) {
                sendSuccess(exchange, taskManager.getEpicList(epicId));
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
            // Парсим JSON
            Map<String, Object> requestData = parseJson(body, Map.class);

            String name = (String) requestData.get("name");
            String description = (String) requestData.get("description");
            String statusStr = (String) requestData.get("status");

            TaskStatus status = TaskStatus.valueOf(statusStr);

            Integer id = requestData.containsKey("id") ?
                    ((Number) requestData.get("id")).intValue() : 0;

            if (id == 0) {
                // Создание нового эпика
                taskManager.addEpic(name, description, status);
                sendCreated(exchange, "Эпик создан");
            } else {
                Epic existingEpic = taskManager.getEpic(id);
                if (existingEpic != null) {
                    // Создаем обновленный эпик, сохраняя подзадачи
                    Epic updatedEpic = new Epic(name, description, id, status);
                    updatedEpic.setSubtask(existingEpic.getSubtasks()); // Сохраняем подзадачи
                    taskManager.updateEpic(updatedEpic);
                    sendCreated(exchange, "Эпик обновлен");
                } else {
                    sendNotFound(exchange);
                }
            }

        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("пересекается")) {
                sendHasInteractions(exchange);
            } else {
                sendBadRequest(exchange, "Неверный статус задачи: " + e.getMessage());
            }
        } catch (Exception e) {
            sendBadRequest(exchange, "Ошибка в формате запроса: " + e.getMessage());
        }
    }

    private void handleDelete(HttpExchange exchange, String path) throws IOException {
        if (path.matches("/epics/\\d+")) {
            int id = extractId(path);
            Epic epic = taskManager.getEpic(id);
            if (epic != null) {
                taskManager.deleteEpic(id);
                sendText(exchange, "Эпик удален", 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private int extractId(String path) {
        String[] parts = path.split("/");
        return Integer.parseInt(parts[2]);
    }
}