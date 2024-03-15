package webserver.router;

import webserver.DefaultFileHandler;
import webserver.HttpRequestMsg;
import webserver.HttpResponseMsg;
import webserver.TargetHandlerExtractor;
import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.UrlFormatException;

public class StaticRouter {
    public HttpResponseMsg route(HttpRequestMsg httpRequestMsg) {
        try {
            TargetHandlerExtractor extractor = new TargetHandlerExtractor();
            String targetHandler = extractor.extractTargetHandler(httpRequestMsg);
            DefaultFileHandler defaultFileHandler = new DefaultFileHandler(httpRequestMsg);
            byte[] body = defaultFileHandler.serialize();
            return new HttpResponseMsg(200, "OK", httpRequestMsg.getContentType(), body);
        } catch (UrlFormatException e) {
            return new HttpResponseMsg(400, "Bad Request: " + e.getMessage());
        } catch (ResourceNotFoundException e) {
            return new HttpResponseMsg(404, "Not Found: " + e.getMessage());
        }
    }
}