package webserver;

import webserver.exceptions.ResourceNotFoundException;

public class DefaultFileHandler {
    private final HttpRequestMsg httpRequestMessage;

    public DefaultFileHandler(HttpRequestMsg httpRequestMessage) {
        this.httpRequestMessage = httpRequestMessage;
    }

    public byte[] serialize() throws ResourceNotFoundException {
        PathRedirector redirector = new PathRedirector();
        ResourceLoader loader = new ResourceLoader();
        String requestTarget = httpRequestMessage.getRequestTarget();

        String path = redirector.makeRedirection(requestTarget);
        String data = loader.load(path);
        return data.getBytes();
    }
}
