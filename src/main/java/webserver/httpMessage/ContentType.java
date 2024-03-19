package webserver.httpMessage;

import java.util.Arrays;

public enum ContentType {
    HTML(".html", "text/html"),
    CSS(".css", "text/css"),
    JS(".js", "text/javascript"),
    ICO(".ico", "image/x-icon"),
    PNG(".png", "image/png"),
    JPG(".jpg", "image/jpeg"),
    SVG(".svg", "image/svg+xml"),
    NONE("", "");

    private final String extension;
    private final String mimeType;

    ContentType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public String getMimetype() {
        return mimeType;
    }

    public static ContentType findMatchingContentType(String requestLine){
        return Arrays.stream(values())
                .filter(v -> requestLine.contains(v.getExtension()))
                .findFirst()
                .orElse(NONE);
    }
}
