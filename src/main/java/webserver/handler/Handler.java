package webserver.handler;

import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;

public interface Handler {
    public HttpResponse handleRequest(HttpRequest httpRequest);
}
