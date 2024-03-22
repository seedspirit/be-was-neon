package webserver.router;

import webserver.handler.*;
import webserver.exceptions.UrlFormatException;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;
import webserver.httpMessage.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class POSTRouter implements Router {

    private final Map<String, Handler> handlers;
    private final ExceptionHandler exceptionHandler;
    private final String INITIAL_PATH_SEGMENT_EXTRACTION_PATTERN = "^\\/[^\\/\\?]+";

    public POSTRouter(){
        this.handlers = new HashMap<>();
        handlers.put("/create", new UserCreateHandler());
        handlers.put("/login", new LoginHandler());
        handlers.put("/logout", new LogoutHandler());
        this.exceptionHandler = new ExceptionHandler();
    }

    public HttpResponse route(HttpRequest httpRequest) {
        try {
            String targetHandler = extractTargetHandler(httpRequest.getRequestTarget());
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

    private String extractTargetHandler(String requestTarget) throws UrlFormatException {
        Pattern pattern = Pattern.compile(INITIAL_PATH_SEGMENT_EXTRACTION_PATTERN);
        Matcher matcher = pattern.matcher(requestTarget);
        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new UrlFormatException();
        }
    }
}
