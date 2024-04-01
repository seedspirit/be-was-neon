package webserver.handler;

import util.Reader;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpResponse.HttpResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import static webserver.URLConstants.STATIC_DIR_PATH;

public abstract class CustomHtmlBuilder implements Handler {

    @Override
    public abstract HttpResponse handleRequest(HttpRequest httpRequest);

    public byte[] loadCustomHtml(String targetCSS, String marker, String insertionContent, String path){
        try (InputStream inputStream = this.getClass().getResourceAsStream(STATIC_DIR_PATH + path)) {
            checkInputStreamNull(inputStream);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            StringBuilder builder = new StringBuilder();
            String line;
            while(!(line = Reader.readLineFrom(bis)).isEmpty()) {
                if (line.contains(targetCSS)) {
                    line = line.replaceFirst(marker, insertionContent);
                }
                builder.append(line);
            }
            return builder.toString().getBytes();
        } catch (IOException e) {
            throw new NoSuchElementException();
        }
    }

    private void checkInputStreamNull(InputStream inputStream) throws NoSuchElementException {
        if (inputStream == null) {
            throw new NoSuchElementException();
        }
    }
}
