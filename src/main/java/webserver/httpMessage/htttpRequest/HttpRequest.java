package webserver.httpMessage.htttpRequest;

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

    public Map<String, List<String>> getHeaders() {
        return headers.getValues();
    }

    public Map<String, String> getRequestLine() {
        return requestLine.getValues();
    }

    public byte[] getBody() {
        return body.getValues();
    }

    public Optional<String> getLoginCookie(){
        return headers.getValueOf(COOKIE).stream()
                .filter(cookie -> cookie.startsWith("sid="))
                .findFirst()
                .map(cookie -> cookie.substring(4));
    }
}
