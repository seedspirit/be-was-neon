package webserver.handler.dynamicHTML;

import webserver.exceptions.ResourceNotFoundException;
import webserver.handler.Handler;
import webserver.handler.ResourceLoader;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;


import java.nio.charset.StandardCharsets;


public abstract class CustomHtmlBuilder extends ResourceLoader implements Handler {

    @Override
    public abstract HttpResponse handleRequest(HttpRequest httpRequest);

    public String modifyHtmlBySelector(String origin, String marker, String insertionContent){
        return origin.replaceFirst(marker, insertionContent);
    }

    public String loadHtml(String path) throws ResourceNotFoundException {
        byte[] contentBytes = load(path);
        return new String(contentBytes, StandardCharsets.UTF_8);
    }
}