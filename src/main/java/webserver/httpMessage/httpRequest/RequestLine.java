package webserver.httpMessage.httpRequest;

import webserver.URLConstants;
import webserver.exceptions.UnsupportedMethodException;
import webserver.exceptions.UrlFormatException;
import webserver.httpMessage.ContentType;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static webserver.URLConstants.DEFAULT_INDEX_PAGE;
import static webserver.httpMessage.HttpConstants.METHOD;
import static webserver.httpMessage.HttpConstants.REQUEST_TARGET;

public class RequestLine {
    private final Map<String, String> requestLine;
    private final String INITIAL_PATH_SEGMENT_EXTRACTION_PATTERN = "^\\/[^\\/\\?]+";
    private final String URL_VALIDATION_REG = "^\\/[^\\s]*$";

    public RequestLine(Map<String, String> requestLine){
        this.requestLine = requestLine;
    }

    public Map<String, String> getValues() {
        return requestLine;
    }

    public String getValueOf(String key){
        return requestLine.get(key);
    }

    public void addRequestTargetDefaultIndexPage(){
        requestLine.put(REQUEST_TARGET, getRequestTarget() + DEFAULT_INDEX_PAGE);
    }

    public boolean requiresPublicAccessURL(){
        return URLConstants.AccessLevel.belongsToPublic(getRequestTarget());
    }

    public boolean isNotStaticResourceRequest(){
        return ContentType.findContentTypeByExtension(getRequestTarget()).equals(ContentType.NONE);
    }

    public String getInitialPathSegment() throws UrlFormatException {
        Pattern pattern = Pattern.compile(INITIAL_PATH_SEGMENT_EXTRACTION_PATTERN);
        Matcher matcher = pattern.matcher(getRequestTarget());
        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new UrlFormatException();
        }
    }

    public void checkMethodSupportedInServer() throws UnsupportedMethodException {
        String method = requestLine.get(METHOD);
        if(!method.equals("GET") && !method.equals("POST")){
            throw new UnsupportedMethodException();
        }
    }

    public void checkRequestTargetFormatValid() throws UrlFormatException {
        if(!getRequestTarget().matches(URL_VALIDATION_REG)){
            throw new UrlFormatException();
        }
    }
    public String getRequestTarget() {
        return requestLine.get(REQUEST_TARGET);
    }
}
