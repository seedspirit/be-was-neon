package webserver.httpMessage.httpRequest;

import db.SessionDatabase;
import model.User;
import webserver.httpMessage.ContentType;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static webserver.httpMessage.HttpConstants.*;

public class RequestHeaders {

    private final Map<String, List<String>> requestHeaders;

    public RequestHeaders(Map<String, List<String>> requestHeaders){
        this.requestHeaders = requestHeaders;
    }

    public Map<String, List<String>> getValues() {
        return requestHeaders;
    }

    public boolean containsContentType(){
        return requestHeaders.containsKey(CONTENT_TYPE);
    }

    public int getContentLength(){
        return Integer.parseInt(requestHeaders.get(CONTENT_LENGTH).get(0));
    }

    public String getMultiFormBoundary(){
        String boundaryString = requestHeaders.get(CONTENT_TYPE).get(1);
        return boundaryString.substring(10);
    }

    public boolean contentTypeEqualsFormURLEncoded(){
        return requestHeaders.get(CONTENT_TYPE).get(0).equals(ContentType.FORM_URL_ENCODED.getMimetype());
    }

    public boolean contentTypeEqualsMultipartForm(){
        return requestHeaders.get(CONTENT_TYPE).get(0).equals(ContentType.MULTIPART_FORM_DATA.getMimetype());
    }

    public boolean hasLoginCookie(){
        return requestHeaders.get(COOKIE).stream()
                .anyMatch(sessionId -> sessionId.startsWith("sid="));
    }

    public String getLoginCookieSessionId() throws NoSuchElementException {
        return requestHeaders.get(COOKIE).stream()
                .filter(cookie -> cookie.startsWith("sid="))
                .findFirst()
                .map(cookie -> cookie.substring(4))
                .orElseThrow(NoSuchElementException::new);
    }

    public boolean sessionIdExistsInSessionDB() {
        try{
            return SessionDatabase.isSessionIdExists(getLoginCookieSessionId());
        } catch (NoSuchElementException e){
            return false;
        }
    }

    public boolean isClientSessionAuthenticated() {
        return hasLoginCookie() && sessionIdExistsInSessionDB();
    }

    public String getUserNameOfClient() throws NoSuchElementException {
        String sessionId = getLoginCookieSessionId();
        User user = SessionDatabase.findUserBySessionId(sessionId);
        return user.getName();
    }

    public List<String> getValueOf(String headerName){
        return requestHeaders.get(headerName);
    }
}
