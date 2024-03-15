package webserver.router;

import webserver.*;
import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.UrlFormatException;

public class DynamicRouter {
    public HttpResponseMsg route(HttpRequestMsg httpRequestMsg) {
        try {
            TargetHandlerExtractor extractor = new TargetHandlerExtractor();
            String targetHandler = extractor.extractTargetHandler(httpRequestMsg);
            switch (targetHandler) {
                case "/create" -> {
                    UserCreateHandler userCreateHandler = new UserCreateHandler();
                    userCreateHandler.addUserInDatabase(httpRequestMsg.getRequestTarget());
                    return new HttpResponseMsg(200, "OK");
                }
                case "/registration" -> {
                    DefaultFileHandler defaultFileHandler = new DefaultFileHandler(httpRequestMsg);
                    return new HttpResponseMsg(200, "OK", defaultFileHandler.serialize());
                }
                default -> {
                    return new HttpResponseMsg(404, "Not Found: 요청한 리소스를 찾을 수 없습니다");
                }
            }
        } catch (UrlFormatException e) {
            return new HttpResponseMsg(400, "Bad Request: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            return new HttpResponseMsg(404, "Not Found: " + e.getMessage());
        }
    }
}
