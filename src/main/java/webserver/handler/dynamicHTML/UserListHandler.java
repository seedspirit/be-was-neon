package webserver.handler.dynamicHTML;

import db.UserDatabase;

import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.html.ExceptionHTMLGenerator;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

import static webserver.httpMessage.HttpStatus.NOT_FOUND;
import static webserver.httpMessage.HttpStatus.OK;

public class UserListHandler extends CustomHtmlBuilder {
    private final String USER_INFO_TABLE_INSERTION_MARKER = "userInfoTable";
    private final String HTML_FILE_EXTENSION = ".html";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest){
        RequestLine requestLine = httpRequest.getRequestLine();
        // DB에 저장되어 있는 유저들의 아이디, 이름을 바탕으로 표 형태의 HTML을 만들어 삽입
        String tableHTML = UserDatabase.generateUserInfoTable();
        try {
            String basicHTML = loadHtml(requestLine.getRequestTarget() + HTML_FILE_EXTENSION);
            String customizedHtml = modifyHtmlBySelector(basicHTML, USER_INFO_TABLE_INSERTION_MARKER,  tableHTML);
            return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                    .contentType(ContentType.findContentTypeByExtension(requestLine.getRequestTarget()))
                    .body(customizedHtml.getBytes())
                    .build();
        } catch (ResourceNotFoundException e) {
            byte[] body = ExceptionHTMLGenerator.getHtml(NOT_FOUND).getBytes();
            return new HttpResponse.Builder(NOT_FOUND.getStatusCode(), NOT_FOUND.getReasonPhrase())
                    .contentType(ContentType.HTML)
                    .body(body)
                    .build();
        }
    }
}