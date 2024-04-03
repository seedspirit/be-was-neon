package webserver.router;

import webserver.handler.*;
import webserver.handler.dynamicHTML.ArticleListPageHandler;
import webserver.handler.dynamicHTML.LoginUserMainPageHandler;
import webserver.handler.dynamicHTML.UserListHandler;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class GETRouter implements Router {

    private final Map<String, Handler> handlers;
    private final ExceptionHandler exceptionHandler;

    public GETRouter(){
        this.handlers = new HashMap<>();
        handlers.put("/user/list", new UserListHandler());
        handlers.put("/main/index.html", new LoginUserMainPageHandler());
        handlers.put("/article/list", new ArticleListPageHandler());
        this.exceptionHandler = new ExceptionHandler();
    }

    public HttpResponse route(HttpRequest httpRequest) {
        try {
            RequestLine requestLine = httpRequest.getRequestLine();
            editMainPath(requestLine);
            Handler handler = handlers.getOrDefault(requestLine.getRequestTarget(), new StaticResourceHandler());
            return  handler.handleRequest(httpRequest);
        } catch (Exception exception) {
            return exceptionHandler.handleException(exception);
        }
    }

    private void editMainPath(RequestLine requestLine){
        if(requestLine.getRequestTarget().equals("/main")){
            requestLine.addRequestTargetDefaultIndexPage();
        }
    }
}
