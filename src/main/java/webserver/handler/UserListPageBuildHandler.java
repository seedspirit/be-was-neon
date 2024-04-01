package webserver.handler;

import db.UserDatabase;

import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

import static webserver.httpMessage.HttpStatus.OK;

public class UserListPageBuildHandler extends CustomHtmlBuilder {
    private final String TABLE_CSS_CLASS_NAME = "grid-table";
    private final String USER_INFO_TABLE_INSERTION_MARKER = "userInfoTable";
    private final String HTML_FILE_EXTENSION = ".html";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest){
        RequestLine requestLine = httpRequest.getRequestLine();
        String tableHTML = UserDatabase.generateUserInfoTable();
        byte[] body = loadCustomHtml(
                TABLE_CSS_CLASS_NAME,
                USER_INFO_TABLE_INSERTION_MARKER,
                tableHTML,
                requestLine.getRequestTarget() + HTML_FILE_EXTENSION);
        return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                .contentType(ContentType.findContentTypeByExtension(requestLine.getRequestTarget()))
                .body(body)
                .build();
    }
}
