package tasktracker.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.controllers.TaskManager;
import tracker.model.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public SubtasksHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("GET".equals(method)) {
                if (path.matches("/subtasks/\\d+")) {
                    int id = Integer.parseInt(path.split("/")[2]);
                    Subtask subtask = manager.getSubtaskById(id);
                    if (subtask == null) {
                        sendNotFound(exchange, "Подзадача не найдена");
                        return;
                    }
                    sendText(exchange, gson.toJson(subtask), 200);
                } else {
                    List<Subtask> subtasks = manager.getAllSubtasks();
                    sendText(exchange, gson.toJson(subtasks), 200);
                }
            } else if ("POST".equals(method)) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Subtask subtask = gson.fromJson(body, Subtask.class);

                if (subtask.getId() == 0) {
                    manager.createSubtask(subtask);
                } else {
                    manager.updateSubtask(subtask);
                }
                sendText(exchange, "", 201);
            } else if ("DELETE".equals(method) && path.matches("/subtasks/\\d+")) {
                int id = Integer.parseInt(path.split("/")[2]);
                manager.deleteSubtask(id);
                sendText(exchange, "", 200);
            } else {
                sendNotFound(exchange, "Некорректный запрос");
            }
        } catch (IllegalStateException e) {
            sendHasInteractions(exchange, "Пересечение задач: " + e.getMessage());
        } catch (Exception e) {
            sendServerError(exchange, "Ошибка сервера: " + e.getMessage());
        }
    }
}
