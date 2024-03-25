package webserver.router;


import webserver.exceptions.UnsupportedMethodException;
import webserver.exceptions.UrlFormatException;
import webserver.handler.ExceptionHandler;
import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class FrontRouter implements Router {
    private final String URL_VALIDATION_REG = "^\\/[^\\s]*$";
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
            String method = httpRequest.getMethod();
            checkRequestMethodValid(method);

            String requestTarget = httpRequest.getRequestTarget();
            checkRequestTargetFormatValid(requestTarget);

            return router.get(method).route(httpRequest);
        } catch (Exception exception) {
            return exceptionHandler.handleException(exception);
        }
    }

    private void checkRequestTargetFormatValid(String requestTarget) throws UrlFormatException {
        if(!isRequestTargetFormatValid(requestTarget)){
            throw new UrlFormatException();
        }
    }

    private void checkRequestMethodValid(String method) throws UnsupportedMethodException {
        if(!isRequestMethodValid(method)){
            throw new UnsupportedMethodException();
        }
    }

    private boolean isRequestTargetFormatValid(String requestTarget){
        return requestTarget.matches(URL_VALIDATION_REG);
    }

    private boolean isRequestMethodValid(String requestMethod){
        return router.containsKey(requestMethod);
    }
}
