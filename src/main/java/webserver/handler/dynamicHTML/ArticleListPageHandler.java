package webserver.handler.dynamicHTML;

import db.ArticleDatabase;
import webserver.URLConstants;
import webserver.exceptions.ResourceNotFoundException;
import webserver.exceptions.html.ExceptionHTMLGenerator;
import webserver.httpMessage.ContentType;
import webserver.httpMessage.httpRequest.HttpRequest;
import webserver.httpMessage.httpRequest.RequestLine;
import webserver.httpMessage.httpResponse.HttpResponse;

import static webserver.httpMessage.HttpConstants.LOCATION;
import static webserver.httpMessage.HttpStatus.*;

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
        } catch (ResourceNotFoundException e) {
            byte[] body = ExceptionHTMLGenerator.getHtml(NOT_FOUND).getBytes();
            return new HttpResponse.Builder(NOT_FOUND.getStatusCode(), NOT_FOUND.getReasonPhrase())
                    .contentType(ContentType.HTML)
                    .body(body)
                    .build();
        }
    }
}
