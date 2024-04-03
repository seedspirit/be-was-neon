package webserver.handler.dynamicHTML;

import db.ArticleDatabase;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

public class ArticleListPageHandler extends CustomHtmlBuilder {
    private final String ARTICLE_INFO_INSERTION_MARKER = "articleListTableBody";
    private final String HTML_FILE_EXTENSION = ".html";

    @Override
    public HttpResponse handleRequest(HttpRequest httpRequest) {
        RequestLine requestLine = httpRequest.getRequestLine();
        String tableHtml = ArticleDatabase.generateArticleListHTML();
        return generateCustomHTML(
                requestLine.getRequestTarget() + HTML_FILE_EXTENSION,
                ARTICLE_INFO_INSERTION_MARKER,
                tableHtml);
    }
}