package webserver.handler.dynamicHTML;

import db.ArticleDatabase;
import db.SessionDatabase;
import model.User;

import webserver.URLConstants;
import webserver.handler.dynamicHTML.CustomHtmlBuilder;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestHeaders;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.FOUND;
import static webserver.httpMessage.HttpStatus.OK;

public class LoginUserMainPageHandler extends CustomHtmlBuilder {
    private final String USER_NAME_INSERTION_MARKER = "userNameText";
    private final String ARTICLE_INFO_INSERTION_MARKER = "articleListTableBody";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest){
        RequestHeaders requestHeaders = httpRequest.getHeaders();
        RequestLine requestLine = httpRequest.getRequestLine();
        String sessionId = requestHeaders.getLoginCookieSessionId();
        try{
            User user = SessionDatabase.findUserBySessionId(sessionId);
            String basicHtml = loadHtml(requestLine.getRequestTarget());
            String customizedHtml = modifyHtmlBySelector(basicHtml, USER_NAME_INSERTION_MARKER, user.getUserId());
            customizedHtml = modifyHtmlBySelector(customizedHtml, ARTICLE_INFO_INSERTION_MARKER, ArticleDatabase.generateArticleListHTML());
            return new HttpResponse.Builder(OK.getStatusCode(), OK.getReasonPhrase())
                    .contentType(ContentType.findContentTypeByExtension(requestLine.getRequestTarget()))
                    .body(customizedHtml.getBytes())
                    .build();
        } catch (NullPointerException e) {
            return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                    .addHeaderComponent(LOCATION, URLConstants.LOGIN_INDEX_PAGE)
                    .build();
        }
    }
}