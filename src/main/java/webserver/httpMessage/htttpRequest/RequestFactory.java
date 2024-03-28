package webserver.httpMessage.htttpRequest;

import webserver.httpMessage.htttpRequest.requestParser.RequestBodyParser;
import webserver.httpMessage.htttpRequest.requestParser.RequestHeadersFactory;
import webserver.httpMessage.htttpRequest.requestParser.RequestLineFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

import static webserver.httpMessage.HttpConstants.*;

public class RequestFactory {

    public HttpRequest createHttpRequestFrom(InputStream in) {
        BufferedInputStream bis = new BufferedInputStream(in);
        RequestLine requestLine = createRequestLine(bis);
        RequestHeaders headers = createHeaders(bis);
        RequestBody body = createBodyBasedOnHeaders(bis, headers);
        return new HttpRequest(requestLine, headers, body);
    }

    private RequestLine createRequestLine(BufferedInputStream bis) {
        return new RequestLineFactory().createRequestLineFrom(bis);
    }

    private RequestHeaders createHeaders(BufferedInputStream bis) {
        return new RequestHeadersFactory().createRequestHeadersFrom(bis);
    }

    private RequestBody createBodyBasedOnHeaders(BufferedInputStream bis, RequestHeaders headers) {
        if (headers.containsKey(CONTENT_LENGTH)) {
            int contentLength = Integer.parseInt(headers.getValueOf(CONTENT_LENGTH).get(0));
            return createBody(bis, contentLength);
        }
        return createEmptyBody();
    }

    private RequestBody createBody(BufferedInputStream bis, int contentLength) {
        byte[] body = new RequestBodyParser().getParseResultFrom(bis, contentLength);
        return new RequestBody(body);
    }

    private RequestBody createEmptyBody() {
        return new RequestBody();
    }
}
