package webserver;

import webserver.exceptions.UrlFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TargetHandlerExtractor {
    private final String INITIAL_PATH_SEGMENT_EXTRACTION_PATTERN = "^\\/[^\\/\\?]+";
    public String extractTargetHandler(HttpRequest httpRequest) throws UrlFormatException {
        String requestTarget = httpRequest.getRequestTarget();
        Pattern pattern = Pattern.compile(INITIAL_PATH_SEGMENT_EXTRACTION_PATTERN);
        Matcher matcher = pattern.matcher(requestTarget);
        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new UrlFormatException("잘못된 형식의 URL입니다");
        }
    }
}
