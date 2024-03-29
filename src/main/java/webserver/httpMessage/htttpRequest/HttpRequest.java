package webserver.httpMessage.htttpRequest;

import webserver.httpMessage.htttpRequest.body.RequestBody;

public class HttpRequest {
    private final RequestLine requestLine;
    private final RequestHeaders headers;
    private final RequestBody body;

    public HttpRequest(RequestLine requestLine, RequestHeaders headers, RequestBody body){
        this.requestLine = requestLine;
        this.headers = headers;
        this.body = body;
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
