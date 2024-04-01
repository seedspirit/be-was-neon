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
    private final String TABLE_HTML_FORMAT = """
                <tr>
                    <th>아이디</th>
                    <th>이름</th>
                </tr>
                %s
                            """;
    private final String TABLE_CSS_CLASS_NAME = "grid-table";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest){
        RequestLine requestLine = httpRequest.getRequestLine();
        byte[] body = loadCustomHtml();
        return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                .contentType(ContentType.findContentTypeByExtension(requestLine.getRequestTarget()))
                .body(body)
                .build();
    }

    private byte[] loadCustomHtml(){
        try (InputStream inputStream = this.getClass().getResourceAsStream(STATIC_DIR_PATH + USER_LIST_PAGE )) {
            checkInputStreamNull(inputStream);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            StringBuilder builder = new StringBuilder();
            String line;
            while(!(line = Reader.readLineFrom(bis)).isEmpty()) {
                if (line.contains(TABLE_CSS_CLASS_NAME)) {
                    line += String.format(TABLE_HTML_FORMAT, UserDatabase.generateUserInfoTable());
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
