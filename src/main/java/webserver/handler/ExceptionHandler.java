package webserver.handler;

import webserver.exceptions.UnsupportedMethodException;
import webserver.exceptions.UrlFormatException;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static webserver.httpMessage.HttpStatus.*;

public class ExceptionHandler {

    private final Map<Class<? extends Exception>, HttpStatus> exceptions;

    public ExceptionHandler(){
        this.exceptions = new HashMap<>();
        exceptions.put(UrlFormatException.class, BAD_REQUEST);
        exceptions.put(NoSuchElementException.class, NOT_FOUND);
        exceptions.put(UnsupportedMethodException.class, METHOD_NOT_ALLOWED);
    }

    // 라우팅 단계에서 발생하는 예외를 받아, 그에 알맞는 상태 코드의 응답을 클라이언트에 반환
    public HttpResponse handleException(Exception exception){
        HttpStatus status = exceptions.getOrDefault(exception.getClass(), INTERNAL_SERVER_ERROR);
        return new HttpResponse.Builder(status.getStatusCode(), status.getReasonPhrase()).build();
    }
}