package webserver.router;


import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static webserver.httpMessage.HttpStatus.BAD_REQUEST;
import static webserver.httpMessage.HttpStatus.METHOD_NOT_ALLOWED;

public class FrontRouter implements Router {
    private final String URL_VALIDATION_REG = "^\\/[^\\s]*$";
    private Map<String, Router> router;

    public FrontRouter(){
        initRouter();
    }

    public HttpResponse route(HttpRequest httpRequest) {
        String method = httpRequest.getMethod();
        if(!isRequestMethodValid(method)){
            return new HttpResponse.Builder(METHOD_NOT_ALLOWED.getStatusCode(),
                    METHOD_NOT_ALLOWED.getReasonPhrase()).build();
        }

        String requestTarget = httpRequest.getRequestTarget();
        if(!isRequestTargetFormatValid(requestTarget)){
            return new HttpResponse.Builder(BAD_REQUEST.getStatusCode(),
                    BAD_REQUEST.getReasonPhrase() + ": 잘못된 형식의 URL입니다").build();
        }
        return router.get(method).route(httpRequest);
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
