package webserver.router;

import webserver.handler.ExceptionHandler;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.httpRequest.RequestLine;

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

            // RequestLine에서 Method를 판단한 이후 알맞은 라우터로 다시 라우팅
            String method = requestLine.getValueOf(METHOD);
            return router.get(method).route(httpRequest);
        } catch (Exception exception) {
            // 라우팅 단에서 예외가 발생했을 경우 그에 알맞은 http 응답 메시지를 반환
            return exceptionHandler.handleException(exception);
        }
    }
}
