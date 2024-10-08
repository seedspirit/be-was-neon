package webserver.handler.dynamicHTML;

import webserver.URLConstants;
import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.html.ExceptionHTMLGenerator;
import webserver.handler.Handler;
import webserver.handler.ResourceLoader;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.*;
import static webserver.httpMessage.HttpStatus.NOT_FOUND;


public abstract class CustomHtmlBuilder extends ResourceLoader implements Handler {

    @Override
    public abstract HttpResponse handleRequest(HttpRequest httpRequest);

    public HttpResponse generateCustomHTML(String path, Map<String, String> contentReplacements) {
        try {
            String customizedHtml = loadHtml(path);
            for (Map.Entry<String, String> entry : contentReplacements.entrySet()) {
                customizedHtml = modifyHtmlBySelector(customizedHtml, entry.getKey(), entry.getValue());
            }
            return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                    .contentType(ContentType.HTML)
                    .body(customizedHtml.getBytes())
                    .build();
        } catch (NullPointerException e) {
            byte[] body = ExceptionHTMLGenerator.getHtml(INTERNAL_SERVER_ERROR).getBytes();
            return new HttpResponse.Builder(INTERNAL_SERVER_ERROR.getStatusCode(), INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .addHeaderComponent(LOCATION, URLConstants.LOGIN_INDEX_PAGE)
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

    public HttpResponse generateCustomHTML(String path, String marker, String insertionContent) {
        Map<String, String> contentReplacements = new HashMap<>();
        contentReplacements.put(marker, insertionContent);
        return generateCustomHTML(path, contentReplacements); // Reuse the Map-based method
    }

    private String modifyHtmlBySelector(String origin, String marker, String insertionContent){
        return origin.replaceFirst(marker, insertionContent);
    }

    private String loadHtml(String path) throws ResourceNotFoundException {
        byte[] contentBytes = load(path);
        return new String(contentBytes, StandardCharsets.UTF_8);
    }
}
