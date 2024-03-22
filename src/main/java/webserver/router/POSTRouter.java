package webserver.router;

import webserver.handler.*;
import webserver.exceptions.UrlFormatException;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import static webserver.httpMessage.HttpStatus.NOT_FOUND;
import static webserver.httpMessage.HttpStatus.BAD_REQUEST;

public class POSTRouter implements Router {

    private final Map<String, Handler> handlers;
    public POSTRouter(){
        this.handlers = new HashMap<>();
        handlers.put("/create", new UserCreateHandler());
        handlers.put("/login", new LoginHandler());
        handlers.put("/logout", new LogoutHandler());
    }

    public HttpResponse route(HttpRequest httpRequest) {
        try {
            TargetHandlerExtractor extractor = new TargetHandlerExtractor();
            String targetHandler = extractor.extractTargetHandler(httpRequest.getRequestTarget());
            if (handlers.containsKey(targetHandler)){
                Handler handler = handlers.get(targetHandler);
                return handler.handleRequest(httpRequest);
            }
            return generateNotFoundResponse();
        } catch (UrlFormatException e) {
            return new HttpResponse.Builder(BAD_REQUEST.getStatusCode(), BAD_REQUEST.getReasonPhrase() + e.getMessage()).build();
        }
    }

    private HttpResponse generateNotFoundResponse(){
        return new HttpResponse.Builder(NOT_FOUND.getStatusCode(),
                NOT_FOUND.getReasonPhrase() + ": 요청한 리소스를 찾을 수 없습니다").build();
    }
}
