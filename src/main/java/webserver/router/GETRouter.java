package webserver.router;

import webserver.handler.ExceptionHandler;
import webserver.handler.Handler;
import webserver.handler.ResourceLoadHandler;
import webserver.handler.UserListPageBuildHandler;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static webserver.httpMessage.HttpConstants.REQUEST_TARGET;

public class GETRouter implements Router {

    private final Map<String, Handler> handlers;
    private final ExceptionHandler exceptionHandler;

    public GETRouter(){
        this.handlers = new HashMap<>();
        handlers.put("/user/list", new UserListPageBuildHandler());
        this.exceptionHandler = new ExceptionHandler();
    }

    public HttpResponse route(HttpRequest httpRequest) {
        try {
            RequestLine requestLine = httpRequest.getRequestLine();
            Handler handler = handlers.getOrDefault(requestLine.getValueOf(REQUEST_TARGET), new ResourceLoadHandler());
            return  handler.handleRequest(httpRequest);
        } catch (Exception exception) {
            return exceptionHandler.handleException(exception);
        }
    }
}
