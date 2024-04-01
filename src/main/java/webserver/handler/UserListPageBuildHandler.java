package webserver.handler;

import db.UserDatabase;
import model.User;
import util.Reader;

import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;

import static webserver.URLConstants.STATIC_DIR_PATH;
import static webserver.URLConstants.USER_LIST_PAGE;
import static webserver.httpMessage.HttpStatus.OK;

public class UserListPageBuildHandler implements Handler {
    private final String TABLE_CSS_CLASS_NAME = "grid-table";
    private final String USER_INFO_TABLE_INSERTION_MARKER = "userInfoTable";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest){
        RequestLine requestLine = httpRequest.getRequestLine();
        String tableHTML = UserDatabase.generateUserInfoTable();
        byte[] body = loadCustomHtml(tableHTML, USER_LIST_PAGE);
        return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                .contentType(ContentType.findContentTypeByExtension(requestLine.getRequestTarget()))
                .body(body)
                .build();
    }

    private byte[] loadCustomHtml(String tableHTML, String path){
        try (InputStream inputStream = this.getClass().getResourceAsStream(STATIC_DIR_PATH + path)) {
            checkInputStreamNull(inputStream);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            StringBuilder builder = new StringBuilder();
            String line;
            while(!(line = Reader.readLineFrom(bis)).isEmpty()) {
                if (line.contains(TABLE_CSS_CLASS_NAME)) {
                    line = line.replaceFirst(USER_INFO_TABLE_INSERTION_MARKER, tableHTML);
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
