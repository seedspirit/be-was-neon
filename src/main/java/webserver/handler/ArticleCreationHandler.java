package webserver.handler;

import db.ArticleDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Article;
import webserver.MainRequestHandler;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestHeaders;
import webserver.httpMessage.httpRequest.body.FormBody;
import webserver.httpMessage.httpRequest.body.RequestBody;
import webserver.httpMessage.httpResponse.HttpResponse;

import java.util.concurrent.atomic.AtomicLong;

import static webserver.URLConstants.LOGIN_USER_DEFAULT_INDEX_PAGE;
import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.FOUND;

public class ArticleCreationHandler implements Handler {

    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);
    public static AtomicLong sequence = new AtomicLong();

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest){
        addArticleInDB(httpRequest.getHeaders(), httpRequest.getBody());
        return new HttpResponse.Builder(FOUND.getStatusCode(), FOUND.getReasonPhrase())
                .contentType(ContentType.HTML)
                .addHeaderComponent(LOCATION, LOGIN_USER_DEFAULT_INDEX_PAGE)
                .build();
    }

    private void addArticleInDB(RequestHeaders headers, RequestBody body){
        FormBody formBody = (FormBody) body;
        Long seq = sequence.getAndIncrement();
        Article article = new Article(seq, headers.getUserNameOfClient(),
                formBody.getArticleTitle(), formBody.getArticleContent());
        ArticleDatabase.addRecord(seq, article);

        logger.debug("게시글 저장 성공! UserId: {}, 게시글 넘버: {}, 게시글 제목: {}"
                , headers.getUserNameOfClient(), article.getArticleNumber(), article.getTitle());
    }
}
