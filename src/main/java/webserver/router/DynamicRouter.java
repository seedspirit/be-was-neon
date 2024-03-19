package webserver.router;

import webserver.*;
import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.UrlFormatException;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import webserver.httpMessage.HttpStatus;

import static webserver.httpMessage.HttpStatus.*;

public class DynamicRouter {
    public HttpResponse route(HttpRequest httpRequest) {
        try {
            TargetHandlerExtractor extractor = new TargetHandlerExtractor();
            String targetHandler = extractor.extractTargetHandler(httpRequest);
            switch (targetHandler) {
                case "/create" -> {
                    UserCreateHandler userCreateHandler = new UserCreateHandler();
                    userCreateHandler.addUserInDatabase(httpRequest.getBody());
                    return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase()).build();
                }
                case "/registration" -> {
                    DefaultFileHandler defaultFileHandler = new DefaultFileHandler(httpRequest);
                    return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                            .body(defaultFileHandler.serialize())
                            .build();
                }
                default -> {
                    return new HttpResponse.Builder(NOT_FOUND.getStatusCode()
                            , NOT_FOUND.getReasonPhrase() + ": 요청한 리소스를 찾을 수 없습니다").build();
                }
            }
        } catch (UrlFormatException | ResourceNotFoundException e) {
            return new HttpResponse.Builder(BAD_REQUEST.getStatusCode(), BAD_REQUEST.getReasonPhrase() + e.getMessage()).build();
        }
    }
}
