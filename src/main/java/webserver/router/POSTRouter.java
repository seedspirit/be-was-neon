package webserver.router;

import webserver.TargetHandlerExtractor;
import webserver.UserCreateHandler;
import webserver.exceptions.UrlFormatException;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.*;
import static webserver.httpMessage.HttpStatus.BAD_REQUEST;

public class POSTRouter implements Router {
    public HttpResponse route(HttpRequest httpRequest) {
        try {
            TargetHandlerExtractor extractor = new TargetHandlerExtractor();
            String targetHandler = extractor.extractTargetHandler(httpRequest.getRequestTarget());
            switch (targetHandler) {
                case "/create" -> {
                    UserCreateHandler userCreateHandler = new UserCreateHandler();
                    userCreateHandler.addUserInDatabase(httpRequest.getBody());
                    final String redirectLocation = "http://localhost:8080/index.html";
                    return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                            .addHeaderComponent(LOCATION, redirectLocation)
                            .build();
                }
                default -> {
                    return new HttpResponse.Builder(NOT_FOUND.getStatusCode()
                            , NOT_FOUND.getReasonPhrase() + ": 요청한 리소스를 찾을 수 없습니다").build();
                }
            }
        } catch (UrlFormatException e) {
            return new HttpResponse.Builder(BAD_REQUEST.getStatusCode(), BAD_REQUEST.getReasonPhrase() + e.getMessage()).build();
        }
    }
}
