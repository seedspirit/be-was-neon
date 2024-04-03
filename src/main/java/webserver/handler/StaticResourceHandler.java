package webserver.handler;

import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.html.ExceptionHTMLGenerator;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.httpRequest.RequestLine;

import static webserver.httpMessage.HttpStatus.NOT_FOUND;
import static webserver.httpMessage.HttpStatus.OK;

public class StaticResourceHandler extends ResourceLoader implements Handler {

    public HttpResponse handleRequest(HttpRequest httpRequest) {
        RequestLine requestLine = httpRequest.getRequestLine();
        // 디렉토리 형태로 요청이 오는 경우 url에 /index.html을 추가
        if(requestLine.isNotStaticResourceRequest()){
            requestLine.addRequestTargetDefaultIndexPage();
        }
        try{
            byte[] body = load(requestLine.getRequestTarget());
            return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                        .contentType(ContentType.findContentTypeByExtension(requestLine.getRequestTarget()))
                        .body(body)
                        .build();
        } catch (ResourceNotFoundException e) {
            byte[] body = ExceptionHTMLGenerator.getHtml(NOT_FOUND).getBytes();
            return new HttpResponse.Builder(NOT_FOUND.getStatusCode(), NOT_FOUND.getReasonPhrase())
                    .contentType(ContentType.HTML)
                    .body(body)
                    .build();
        }
    }
}