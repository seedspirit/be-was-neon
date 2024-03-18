package webserver.router;

import webserver.*;
import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.UrlFormatException;

public class StaticRouter {
    public HttpResponse route(HttpRequest httpRequest) {
        try {
            TargetHandlerExtractor extractor = new TargetHandlerExtractor();
            String targetHandler = extractor.extractTargetHandler(httpRequest);
            DefaultFileHandler defaultFileHandler = new DefaultFileHandler(httpRequest);
            byte[] body = defaultFileHandler.serialize();
            return new HttpResponse(HttpStatus.OK.getStatusCode(), HttpStatus.OK.getReasonPhrase(), httpRequest.getContentType(), body);
        } catch (UrlFormatException e) {
            return new HttpResponse(HttpStatus.BAD_REQUEST.getStatusCode(), HttpStatus.BAD_REQUEST.getReasonPhrase() + e.getMessage());
        } catch (ResourceNotFoundException e) {
            return new HttpResponse(HttpStatus.NOT_FOUND.getStatusCode(), HttpStatus.NOT_FOUND.getReasonPhrase() + e.getMessage());
        }
    }
}