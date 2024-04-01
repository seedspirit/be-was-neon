package webserver.handler;

import webserver.exceptions.ResourceNotFoundException;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;
import webserver.httpMessage.httpRequest.RequestLine;

import java.io.*;

import static webserver.URLConstants.STATIC_DIR_PATH;
import static webserver.httpMessage.HttpStatus.NOT_FOUND;
import static webserver.httpMessage.HttpStatus.OK;

public class ResourceLoadHandler implements Handler {

    public HttpResponse handleRequest(HttpRequest httpRequest) {
        RequestLine requestLine = httpRequest.getRequestLine();
        // 디렉토리 형태로 요청이 오는 경우 url에 /index.html을 추가
        if(requestLine.isNotStaticResourceRequest()){
            requestLine.addRequestTargetDefaultIndexPage();
        }
        try {
            byte[] body = load(requestLine.getRequestTarget());
            return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                    .contentType(ContentType.findContentTypeByExtension(requestLine.getRequestTarget()))
                    .body(body)
                    .build();
        } catch (ResourceNotFoundException e) {
            return new HttpResponse.Builder(NOT_FOUND.getStatusCode(), NOT_FOUND.getReasonPhrase() + e.getMessage()).build();
        }
    }

    private byte[] load(String path) throws ResourceNotFoundException {
        String targetPath = STATIC_DIR_PATH + path;
        InputStream inputStream = this.getClass().getResourceAsStream(targetPath);
        if (inputStream == null) {
            throw new ResourceNotFoundException("요청한 리소스를 찾을 수 없습니다", path);
        }
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            byte[] bytes = bis.readAllBytes();
            return bytes;
        } catch (IOException e) {
            throw new ResourceNotFoundException("요청한 리소스를 %s에서 읽는 중에 에러가 발생했습니다", path);
        }
    }
}