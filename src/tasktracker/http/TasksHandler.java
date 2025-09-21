package tasktracker.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.controllers.TaskManager;
import tracker.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TasksHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            if ("GET".equals(method)) {
                if (path.matches("/tasks/\\d+")) {
                    int id = Integer.parseInt(path.split("/")[2]);
                    Task task = manager.getTaskById(id);
                    if (task == null) {
                        sendNotFound(exchange, "Задача не найдена");
                        return;
                    }
                    sendText(exchange, gson.toJson(task), 200);
                } else {
                    List<Task> tasks = manager.getAllTasks();
                    sendText(exchange, gson.toJson(tasks), 200);
                }
            } else if ("POST".equals(method)) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                Task task = gson.fromJson(body, Task.class);

                if (task.getId() == 0) {
                    manager.createTask(task);
                } else {
                    manager.updateTask(task);
                }
                sendText(exchange, "", 201);
            } else if ("DELETE".equals(method) && path.matches("/tasks/\\d+")) {
                int id = Integer.parseInt(path.split("/")[2]);
                manager.deleteTask(id);
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
