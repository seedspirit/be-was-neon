package webserver.handler.dynamicHTML;

import db.ArticleDatabase;
import webserver.URLConstants;
import webserver.handler.dynamicHTML.CustomHtmlBuilder;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.FOUND;
import static webserver.httpMessage.HttpStatus.OK;

public class ArticleListPageHandler extends CustomHtmlBuilder {
    private final String ARTICLE_INFO_INSERTION_MARKER = "articleListTableBody";
    private final String HTML_FILE_EXTENSION = ".html";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) {
        RequestLine requestLine = httpRequest.getRequestLine();
        String tableHtml = ArticleDatabase.generateArticleListHTML();
        try {
            String basicHtml = loadHtml(requestLine.getRequestTarget() + HTML_FILE_EXTENSION);
            String customizedHtml = modifyHtmlBySelector(basicHtml, ARTICLE_INFO_INSERTION_MARKER, tableHtml);
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
