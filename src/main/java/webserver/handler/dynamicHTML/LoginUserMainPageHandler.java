package webserver.handler.dynamicHTML;

import db.ArticleDatabase;
import db.SessionDatabase;
import model.User;

import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestHeaders;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import static webserver.URLConstants.LOGIN_USER_DEFAULT_INDEX_PAGE_WITH_CONTENT;


public class LoginUserMainPageHandler extends CustomHtmlBuilder {
    private final String USER_NAME_INSERTION_MARKER = "userNameText";
    private final String ARTICLES_INSERTION_MARKER = "articlePosts";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest){
        RequestHeaders requestHeaders = httpRequest.getHeaders();
        RequestLine requestLine = httpRequest.getRequestLine();
        String sessionId = requestHeaders.getLoginCookieSessionId();
        User user = SessionDatabase.findUserBySessionId(sessionId);
        if(ArticleDatabase.isAnyArticleExists()){
            Map<String, String> contentReplacements = new HashMap<>();
            contentReplacements.put(USER_NAME_INSERTION_MARKER, user.getUserId());
            contentReplacements.put(ARTICLES_INSERTION_MARKER, ArticleDatabase.generateArticlePostHTML());
            return generateCustomHTML(
                    LOGIN_USER_DEFAULT_INDEX_PAGE_WITH_CONTENT,
                    contentReplacements
            );
        }
        return generateCustomHTML(
                requestLine.getRequestTarget(),
                USER_NAME_INSERTION_MARKER,
                user.getUserId());
    }
}