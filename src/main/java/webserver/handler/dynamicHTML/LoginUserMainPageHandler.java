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
        String sessionId = requestHeaders.getLoginCookieSessionId();
        User user = SessionDatabase.findUserBySessionId(sessionId);
        Map<String, String> contentReplacements = new HashMap<>();
        contentReplacements.put(USER_NAME_INSERTION_MARKER, user.getUserId());
        if(ArticleDatabase.isAnyArticleExists()){
            contentReplacements.put(ARTICLES_INSERTION_MARKER, ArticleDatabase.generateArticlePostHTML());
        } else {
            contentReplacements.put(ARTICLES_INSERTION_MARKER, NO_CONTENT_HTML);
        }
        return generateCustomHTML(
                LOGIN_USER_DEFAULT_INDEX_PAGE_WITH_CONTENT,
                contentReplacements
        );
    }

    private final String NO_CONTENT_HTML = """
            <div class="post">
              <div class="post__account">
                <img class="post__account__img" src="../img/noPost.png" />
                <p class="post__account__nickname">Welcome To Codestargram!</p>
              </div>
              <img class="post__img" src="../img/noPost.png" />
              <div class="post__menu">
                <ul class="post__menu__personal">
                  <li>
                    <button class="post__menu__btn">
                      <img src="../img/like.svg" />
                    </button>
                  </li>
                  <li>
                    <button class="post__menu__btn">
                      <img src="../img/sendLink.svg" />
                    </button>
                  </li>
                </ul>
                <button class="post__menu__btn">
                  <img src="../img/bookMark.svg" />
                </button>
              </div>
              <p class="post__article">
                아직 업로드 된 콘텐츠가 없네요! 코드스타크램의 첫번째 업로더가 되어주세요 💃🕺
              </p>
            </div>
            """;
}