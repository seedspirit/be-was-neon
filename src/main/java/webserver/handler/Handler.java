package webserver.handler;

import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;

public interface Handler {
    public HttpResponse handleRequest(HttpRequest httpRequest);
}
