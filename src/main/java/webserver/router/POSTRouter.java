package webserver.router;

import webserver.handler.*;
import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.htttpRequest.RequestLine;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class POSTRouter implements Router {

    private final Map<String, Handler> handlers;
    private final ExceptionHandler exceptionHandler;

    public POSTRouter(){
        this.handlers = new HashMap<>();
        handlers.put("/create", new UserCreateHandler());
        handlers.put("/login", new LoginHandler());
        handlers.put("/logout", new LogoutHandler());
        this.exceptionHandler = new ExceptionHandler();
    }

    public HttpResponse route(HttpRequest httpRequest) {
        try {
            RequestLine requestLine = httpRequest.getRequestLine();
            String targetHandler = requestLine.getInitialPathSegment();
            checkRequestCanBeHandle(targetHandler);
            Handler handler = handlers.get(targetHandler);
            return handler.handleRequest(httpRequest);
        } catch (Exception exception) {
            return exceptionHandler.handleException(exception);
        }
    }

    private void checkRequestCanBeHandle(String targetHandler) throws NoSuchElementException {
        if (!handlers.containsKey(targetHandler)){
            throw new NoSuchElementException();
        }
    }
}
