package webserver.router;

import webserver.handler.*;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static webserver.URLConstants.DEFAULT_INDEX_PAGE;
import static webserver.httpMessage.HttpConstants.REQUEST_TARGET;

public class GETRouter implements Router {

    private final Map<String, Handler> handlers;
    private final ExceptionHandler exceptionHandler;

    public GETRouter(){
        this.handlers = new HashMap<>();
        handlers.put("/user/list", new UserListPageBuildHandler());
        handlers.put("/main/index.html", new CustomUserHtmlBuildHandler());
        this.exceptionHandler = new ExceptionHandler();
    }

    public HttpResponse route(HttpRequest httpRequest) {
        try {
            RequestLine requestLine = httpRequest.getRequestLine();
            String finalUrl = editMainPath(requestLine.getValueOf(REQUEST_TARGET));
            Handler handler = handlers.getOrDefault(finalUrl, new ResourceLoadHandler());
            return  handler.handleRequest(httpRequest);
        } catch (Exception exception) {
            return exceptionHandler.handleException(exception);
        }
    }

    private String editMainPath(String path){
        if(path.equals("/main")){
            return path + DEFAULT_INDEX_PAGE;
        }
        return path;
    }
}
