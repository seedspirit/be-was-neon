package webserver.handler;

import webserver.exceptions.UrlFormatException;
import webserver.httpMessage.HttpResponse;
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
    }

    public HttpResponse handleException(Exception exception){
        HttpStatus status = exceptions.getOrDefault(exception.getClass(), INTERNAL_SERVER_ERROR);
        return new HttpResponse.Builder(status.getStatusCode(), status.getReasonPhrase()).build();
    }
}