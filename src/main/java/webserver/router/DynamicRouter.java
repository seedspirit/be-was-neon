package webserver.router;

import webserver.*;
import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.UrlFormatException;

public class DynamicRouter {
    public HttpResponse route(HttpRequest httpRequest) {
        try {
            TargetHandlerExtractor extractor = new TargetHandlerExtractor();
            String targetHandler = extractor.extractTargetHandler(httpRequest);
            switch (targetHandler) {
                case "/create" -> {
                    UserCreateHandler userCreateHandler = new UserCreateHandler();
                    userCreateHandler.addUserInDatabase(httpRequest.getRequestTarget());
                    return new HttpResponse(200, "OK");
                }
                case "/registration" -> {
                    DefaultFileHandler defaultFileHandler = new DefaultFileHandler(httpRequest);
                    return new HttpResponse(200, "OK", defaultFileHandler.serialize());
                }
                default -> {
                    return new HttpResponse(404, "Not Found: 요청한 리소스를 찾을 수 없습니다");
                }
            }
        } catch (UrlFormatException e) {
            return new HttpResponse(400, "Bad Request: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            return new HttpResponse(404, "Not Found: " + e.getMessage());
        }
    }
}
