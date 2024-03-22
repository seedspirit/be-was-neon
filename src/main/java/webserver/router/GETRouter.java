package webserver.router;

import db.SessionDatabase;
import webserver.handler.ResourceLoadHandler;
import webserver.exceptions.ResourceNotFoundException;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import java.util.Optional;

import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.*;
import static webserver.httpMessage.HttpStatus.NOT_FOUND;

public class GETRouter implements Router {
    private final String INDEX_PAGE = "/index.html";

    public HttpResponse route(HttpRequest httpRequest) {
        try {
            String requestTarget = httpRequest.getRequestTarget();
            if(isNotStaticResourceRequest(requestTarget)){
                requestTarget += INDEX_PAGE;
            }
            ResourceLoadHandler loader = new ResourceLoadHandler();
            byte[] body = loader.load(requestTarget);
            return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                    .contentType(ContentType.of(httpRequest.getRequestTarget()))
                    .body(body)
                    .build();
        } catch (ResourceNotFoundException e) {
            return new HttpResponse.Builder(NOT_FOUND.getStatusCode(), NOT_FOUND.getReasonPhrase() + e.getMessage()).build();
        }
    }

    private boolean isNotStaticResourceRequest(String requestTarget){
        return ContentType.of(requestTarget).equals(ContentType.NONE);
    }
}
