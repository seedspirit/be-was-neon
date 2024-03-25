package webserver.httpMessage.htttpRequest;

import webserver.httpMessage.htttpRequest.requestParser.RequestBodyParser;
import webserver.httpMessage.htttpRequest.requestParser.RequestHeadersParser;
import webserver.httpMessage.htttpRequest.requestParser.RequestLineParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static webserver.httpMessage.HttpConstants.*;

public class RequestFactory {

    public HttpRequest createHttpRequestFrom(InputStream in) {
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        RequestLine requestLine = createRequestLine(br);
        RequestHeaders headers = createHeaders(br);
        RequestBody body = createBodyBasedOnHeaders(br, headers);
        return new HttpRequest(requestLine, headers, body);
    }

    private RequestLine createRequestLine(BufferedReader br) {
        Map<String, String> requestLine = new RequestLineParser().getParseResultFrom(br);
        return new RequestLine(requestLine);
    }

    private RequestHeaders createHeaders(BufferedReader br) {
        Map<String, List<String>> headers = new RequestHeadersParser().getParseResultFrom(br);
        return new RequestHeaders(headers);
    }

    private RequestBody createBodyBasedOnHeaders(BufferedReader br, RequestHeaders headers) {
        if (headers.containsKey(CONTENT_LENGTH)) {
            int contentLength = Integer.parseInt(headers.getValueOf(CONTENT_LENGTH).get(0));
            return createBody(br, contentLength);
        }
        return createEmptyBody();
    }

    private RequestBody createBody(BufferedReader br, int contentLength) {
        String body = new RequestBodyParser().getParseResultFrom(br, contentLength);
        return new RequestBody(body);
    }

    private RequestBody createEmptyBody() {
        return new RequestBody();
    }
}
