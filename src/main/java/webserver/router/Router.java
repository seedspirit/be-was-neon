package webserver.router;

import webserver.httpMessage.htttpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;

public interface Router {
    public HttpResponse route(HttpRequest httpRequest);
}
