package webserver.router;

import webserver.*;
import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.UrlFormatException;
import webserver.httpMessage.HttpRequest;
import webserver.httpMessage.HttpResponse;

import static webserver.httpMessage.HttpStatus.*;

public class StaticRouter {
    public HttpResponse route(HttpRequest httpRequest) {
        try {
            TargetHandlerExtractor extractor = new TargetHandlerExtractor();
            String targetHandler = extractor.extractTargetHandler(httpRequest);
            DefaultFileHandler defaultFileHandler = new DefaultFileHandler(httpRequest);
            byte[] body = defaultFileHandler.serialize();
            return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                    .contentType(httpRequest.getContentType())
                    .body(body)
                    .build();
        } catch (UrlFormatException e) {
            return new HttpResponse.Builder(BAD_REQUEST.getStatusCode(), BAD_REQUEST.getReasonPhrase() + e.getMessage()).build();
        } catch (ResourceNotFoundException e) {
            return new HttpResponse.Builder(NOT_FOUND.getStatusCode(), NOT_FOUND.getReasonPhrase() + e.getMessage()).build();
        }
    }
}