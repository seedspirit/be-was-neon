package webserver.httpMessage.htttpRequest;

import webserver.httpMessage.htttpRequest.requestParser.RequestBodyParser;
import webserver.httpMessage.htttpRequest.requestParser.RequestHeadersParser;
import webserver.httpMessage.htttpRequest.requestParser.RequestLineParser;

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
        Map<String, String> requestLine = new RequestLineParser().getParseResultFrom(bis);
        return new RequestLine(requestLine);
    }

    private RequestHeaders createHeaders(BufferedInputStream bis) {
        Map<String, List<String>> headers = new RequestHeadersParser().getParseResultFrom(bis);
        return new RequestHeaders(headers);
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
