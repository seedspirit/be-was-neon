package webserver;

import java.util.Optional;

public class Router {
    public Optional<byte[]> route(HttpRequestMsg httpRequestMsg) {
        String targetHandler = extractTargetHandler(httpRequestMsg);
        Optional<byte[]> body = Optional.empty();
        try{
            switch (targetHandler) {
                case "create" -> {
                    UserCreateHandler userCreateHandler = new UserCreateHandler(httpRequestMsg.getRequestTarget());
                    userCreateHandler.addUserInDatabase();
                    break;
                }
                default -> {
                    DefaultFileHandler defaultFileHandler = new DefaultFileHandler(httpRequestMsg);
                    body = Optional.of(defaultFileHandler.serialize());
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return body;
    }

    private String extractTargetHandler(HttpRequestMsg httpRequestMsg) {
        String requestTarget = httpRequestMsg.getRequestTarget();
        return requestTarget.split("/")[1];
    }
}
