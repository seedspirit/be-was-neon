package webserver.handler;

import webserver.exceptions.ResourceNotFoundException;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.io.*;

import static webserver.httpMessage.HttpStatus.NOT_FOUND;
import static webserver.httpMessage.HttpStatus.OK;

public class ResourceLoadHandler implements Handler {
    private final String INDEX_PAGE = "/index.html";
    private final String STATIC_DIR_PATH = "/static";

    public HttpResponse handleRequest(HttpRequest httpRequest) {
        String requestTarget = httpRequest.getRequestTarget();
        if(isNotStaticResourceRequest(requestTarget)){
            requestTarget += INDEX_PAGE;
        }
        try {
            byte[] body = load(requestTarget);
            return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                    .contentType(ContentType.of(httpRequest.getRequestTarget()))
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


    private boolean isNotStaticResourceRequest(String requestTarget){
        return ContentType.of(requestTarget).equals(ContentType.NONE);
    }
}