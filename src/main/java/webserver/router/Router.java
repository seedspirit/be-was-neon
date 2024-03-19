package webserver.router;


import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

public class Router {

    public HttpResponse route(HttpRequest httpRequest) {
        if (httpRequest.requireStaticFile()) {
            return new StaticRouter().route(httpRequest);
        } else {
            return new DynamicRouter().route(httpRequest);
        }
    }
}
