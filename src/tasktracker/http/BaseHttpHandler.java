package tasktracker.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected void sendText(HttpExchange h, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h, String message) throws IOException {
        sendText(h, message, 404);
    }

    protected void sendHasInteractions(HttpExchange h, String message) throws IOException {
        sendText(h, message, 406);
    }

    protected void sendServerError(HttpExchange h, String message) throws IOException {
        sendText(h, message, 500);
    }
}
