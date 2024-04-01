package webserver.handler;

import db.SessionDatabase;
import model.User;
import util.Reader;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestHeaders;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static webserver.URLConstants.*;
import static webserver.httpMessage.HttpStatus.OK;

public class CustomUserHtmlBuildHandler implements Handler {
    private final String USER_NAME_BUTTON_CSS_ID = "authenticated-userNameButton";
    private final String USER_NAME_INSERTION_MARKER = "userNameText";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest){
        RequestHeaders requestHeaders = httpRequest.getHeaders();
        RequestLine requestLine = httpRequest.getRequestLine();
        String sessionId = requestHeaders.getLoginCookieSessionId();
        User user = SessionDatabase.findUserBySessionId(sessionId);
        byte[] body = loadCustomHtml(user.getUserId(), LOGIN_USER_DEFAULT_INDEX_PAGE);
        return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                .contentType(ContentType.findContentTypeByExtension(requestLine.getRequestTarget()))
                .body(body)
                .build();
    }

    private byte[] loadCustomHtml(String userId, String path){
        try (InputStream inputStream = this.getClass().getResourceAsStream(STATIC_DIR_PATH + path)) {
            checkInputStreamNull(inputStream);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            StringBuilder builder = new StringBuilder();
            String line;
            while(!(line = Reader.readLineFrom(bis)).isEmpty()) {
                if (line.contains(USER_NAME_BUTTON_CSS_ID)) {
                    line = line.replaceFirst(USER_NAME_INSERTION_MARKER, userId);
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
