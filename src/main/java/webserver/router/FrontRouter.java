package webserver.router;

import webserver.handler.ExceptionHandler;
import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.htttpRequest.RequestLine;

import java.util.HashMap;
import java.util.Map;

import static webserver.httpMessage.HttpConstants.METHOD;

public class FrontRouter implements Router {
    private final ExceptionHandler exceptionHandler;
    private final Map<String, Router> router;

    public FrontRouter(){
        router = new HashMap<>();
        router.put("GET", new GETRouter());
        router.put("POST", new POSTRouter());
        this.exceptionHandler = new ExceptionHandler();
    }

    public HttpResponse route(HttpRequest httpRequest) {
        try {
            RequestLine requestLine = httpRequest.getRequestLine();
            requestLine.checkMethodSupportedInServer();
            requestLine.checkRequestTargetFormatValid();

            String method = requestLine.getValueOf(METHOD);
            return router.get(method).route(httpRequest);
        } catch (Exception exception) {
            return exceptionHandler.handleException(exception);
        }
    }
}
