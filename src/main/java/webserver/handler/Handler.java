package webserver.handler;

import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

public interface Handler {
    public HttpResponse handleRequest(HttpRequest httpRequest);
}
