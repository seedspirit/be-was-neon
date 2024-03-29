package webserver.router;

import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;

public interface Router {
    public HttpResponse route(HttpRequest httpRequest);
}
