package webserver.httpMessage.htttpRequest;

import webserver.httpMessage.htttpRequest.body.RequestBody;

import java.util.*;

import static webserver.httpMessage.HttpConstants.*;

public class HttpRequest {
    private final RequestLine requestLine;
    private final RequestHeaders headers;
    private final RequestBody body;

    public HttpRequest(RequestLine requestLine, RequestHeaders headers, RequestBody body){
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return requestLine.getValueOf(METHOD);
    }

    public String getRequestTarget() {
        return requestLine.getValueOf(REQUEST_TARGET);
    }

    public RequestHeaders getHeaders() {
        return headers;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public RequestBody getBody() {
        return body;
    }
}
