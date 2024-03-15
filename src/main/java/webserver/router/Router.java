package webserver.router;


import webserver.HttpRequestMsg;
import webserver.HttpResponseMsg;

public class Router {

    public HttpResponseMsg route(HttpRequestMsg httpRequestMsg) {
        if (httpRequestMsg.requireStaticFile()) {
            return new StaticRouter().route(httpRequestMsg);
        } else {
            return new DynamicRouter().route(httpRequestMsg);
        }
    }
}
