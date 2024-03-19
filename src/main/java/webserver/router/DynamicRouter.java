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
                    return new HttpResponse(HttpStatus.OK.getStatusCode(), HttpStatus.OK.getReasonPhrase());
                }
                case "/registration" -> {
                    DefaultFileHandler defaultFileHandler = new DefaultFileHandler(httpRequest);
                    return new HttpResponse(HttpStatus.OK.getStatusCode(), HttpStatus.OK.getReasonPhrase(), defaultFileHandler.serialize());
                }
                default -> {
                    return new HttpResponse(HttpStatus.NOT_FOUND.getStatusCode()
                            , HttpStatus.NOT_FOUND.getReasonPhrase() + ": 요청한 리소스를 찾을 수 없습니다");
                }
            }
        } catch (UrlFormatException | ResourceNotFoundException e) {
            return new HttpResponse(HttpStatus.BAD_REQUEST.getStatusCode(), HttpStatus.BAD_REQUEST.getReasonPhrase() + e.getMessage());
        }
    }
}
