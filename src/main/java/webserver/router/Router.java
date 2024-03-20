package webserver.router;


import webserver.httpMessage.ContentType;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

public class Router {

    public HttpResponse route(HttpRequest httpRequest) {
        if (isStaticResourceRequest(httpRequest.getRequestTarget())) {
            return new StaticRouter().route(httpRequest);
        } else {
            return new DynamicRouter().route(httpRequest);
        }
    }

    private boolean isStaticResourceRequest(String requestTarget){
        return !ContentType.of(requestTarget).equals(ContentType.NONE);
    }
}
