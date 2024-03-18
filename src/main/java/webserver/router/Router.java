package webserver.router;


import webserver.HttpRequest;
import webserver.HttpResponse;

public class Router {

    public HttpResponse route(HttpRequest httpRequest) {
        if (httpRequest.requireStaticFile()) {
            return new StaticRouter().route(httpRequest);
        } else {
            return new DynamicRouter().route(httpRequest);
        }
    }
}
