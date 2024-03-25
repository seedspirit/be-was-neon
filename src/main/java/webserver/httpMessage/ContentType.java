package webserver.httpMessage;

import java.util.Arrays;

import static util.constants.Delimiter.*;

public enum ContentType {
    HTML(".html", "text/html", "charset=utf-8"),
    CSS(".css", "text/css", "charset=utf-8"),
    JS(".js", "text/javascript", "charset=utf-8"),
    ICO(".ico", "image/x-icon", EMPTY),
    PNG(".png", "image/png", EMPTY),
    JPG(".jpg", "image/jpeg", EMPTY),
    SVG(".svg", "image/svg+xml", EMPTY),
    NONE(EMPTY, EMPTY, EMPTY);

    private final String extension;
    private final String mimeType;
    private final String parameter;

    ContentType(String extension, String mimeType, String parameter) {
        this.extension = extension;
        this.mimeType = mimeType;
        this.parameter = parameter;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimetype() {
        return mimeType;
    }

    public static ContentType of(String requestTarget){
        return Arrays.stream(values())
                .filter(v -> requestTarget.contains(v.getExtension()))
                .findFirst()
                .orElse(NONE);
    }

    public static String generateHttpContentTypeHeaderOf(ContentType contentType) {
        if (!contentType.parameter.equals(EMPTY)) {
            return contentType.mimeType + SEMICOLON + contentType.parameter;
        }
        return contentType.mimeType;
    }
}