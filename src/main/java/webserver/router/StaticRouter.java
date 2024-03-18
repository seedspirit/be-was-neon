package webserver.router;

import webserver.DefaultFileHandler;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.TargetHandlerExtractor;
import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.UrlFormatException;

public class StaticRouter {
    public HttpResponse route(HttpRequest httpRequest) {
        try {
            TargetHandlerExtractor extractor = new TargetHandlerExtractor();
            String targetHandler = extractor.extractTargetHandler(httpRequest);
            DefaultFileHandler defaultFileHandler = new DefaultFileHandler(httpRequest);
            byte[] body = defaultFileHandler.serialize();
            return new HttpResponse(200, "OK", httpRequest.getContentType(), body);
        } catch (UrlFormatException e) {
            return new HttpResponse(400, "Bad Request: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            return new HttpResponse(404, "Not Found: " + e.getMessage());
        }
    }
}