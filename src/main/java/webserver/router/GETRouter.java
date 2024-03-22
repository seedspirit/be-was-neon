package webserver.router;

import webserver.handler.ResourceLoadHandler;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

public class GETRouter implements Router {

    public HttpResponse route(HttpRequest httpRequest) {
        ResourceLoadHandler resourceLoadHandler = new ResourceLoadHandler();
        return  resourceLoadHandler.handleRequest(httpRequest);
    }
}
