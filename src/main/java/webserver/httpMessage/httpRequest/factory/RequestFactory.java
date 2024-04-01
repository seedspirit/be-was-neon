package webserver.httpMessage.httpRequest.factory;

import webserver.exceptions.ParsingException;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.body.RequestBody;
import webserver.httpMessage.httpRequest.RequestHeaders;
import webserver.httpMessage.httpRequest.RequestLine;

import java.io.*;

public class RequestFactory {

    private InputStream in;

    public RequestFactory(InputStream in){
        this.in = in;
    }

    public HttpRequest createHttpRequest() throws ParsingException {
        BufferedInputStream bis = new BufferedInputStream(in);
        RequestLine requestLine = createRequestLine(bis);
        RequestHeaders headers = createHeaders(bis);
        RequestBody body = createBody(bis, headers);
        return new HttpRequest(requestLine, headers, body);
    }

    private RequestLine createRequestLine(BufferedInputStream bis) throws ParsingException {
        return new RequestLineFactory().createRequestLineFrom(bis);
    }

    private RequestHeaders createHeaders(BufferedInputStream bis) throws ParsingException {
        return new RequestHeadersFactory().createRequestHeadersFrom(bis);
    }

    private RequestBody createBody(BufferedInputStream bis, RequestHeaders requestHeaders) throws ParsingException {
        return new RequestBodyFactory().createRequestBodyFrom(bis, requestHeaders);
    }
}
