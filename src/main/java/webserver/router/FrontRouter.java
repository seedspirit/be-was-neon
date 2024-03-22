package webserver.router;


import webserver.exceptions.UnsupportedMethodException;
import webserver.exceptions.UrlFormatException;
import webserver.handler.ExceptionHandler;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static webserver.httpMessage.HttpStatus.BAD_REQUEST;
import static webserver.httpMessage.HttpStatus.METHOD_NOT_ALLOWED;

public class FrontRouter implements Router {
    private final String URL_VALIDATION_REG = "^\\/[^\\s]*$";
    private final ExceptionHandler exceptionHandler;
    private Map<String, Router> router;

    public FrontRouter(){
        initRouter();
        this.exceptionHandler = new ExceptionHandler();
    }

    public HttpResponse route(HttpRequest httpRequest) {
        try {
            String method = httpRequest.getMethod();
            if(!isRequestMethodValid(method)){
               throw new UnsupportedMethodException();
            }

            String requestTarget = httpRequest.getRequestTarget();
            if(!isRequestTargetFormatValid(requestTarget)){
                throw new UrlFormatException();
            }
            return router.get(method).route(httpRequest);
        } catch (Exception exception) {
            return exceptionHandler.handleException(exception);
        }
    }

    private void initRouter(){
        router = new HashMap<>();
        router.put("GET", new GETRouter());
        router.put("POST", new POSTRouter());
    }

    private boolean isRequestTargetFormatValid(String requestTarget){
        return requestTarget.matches(URL_VALIDATION_REG);
    }

    private boolean isRequestMethodValid(String requestMethod){
        return router.containsKey(requestMethod);
    }
}
