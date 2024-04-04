package webserver.httpMessage.httpRequest.body;

import webserver.ImageFile;
import webserver.httpMessage.httpRequest.body.parser.MultipartFormatParser;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.constants.Delimiter.*;

public class MultipartBody implements RequestBody {
    private final String NAME_EXTRACTION_REGEX = "name=\"(.+?)\"";
    private final String FILENAME_EXTRACTION_REGEX = "filename=\"(.+?)\"";

    private final byte[] rawBytes;
    private final String boundary;
    private final MultipartFormatParser multipartFormatParser;

    private final String TITLE_NAME_PARAM = "title";
    private final String CONTENT_NAME_PARAM = "content";
    private final String FILE_NAME_PARAM = "file";

    private String title;
    private String content;
    private ImageFile imageFile;


    public MultipartBody(byte[] rawBytes, String boundary){
        this.rawBytes = rawBytes;
        this.boundary = boundary;
        this.multipartFormatParser = new MultipartFormatParser();
        List<List<byte[]>> multipartSegments = multipartFormatParser.splitBodyByBoundary(rawBytes, boundary);
        parseSegments(multipartSegments);
    }

    private void parseSegments(List<List<byte[]>> multipartSegments){
        for (List<byte[]> segment : multipartSegments) {
            extractValuesFromSegment(segment);
        }
    }

    private void extractValuesFromSegment(List<byte[]> segment){
        String paramName = extractParameter(new String(segment.get(0)), NAME_EXTRACTION_REGEX);
        switch (paramName) {
            case TITLE_NAME_PARAM -> title = extractText(segment, 2);
            case CONTENT_NAME_PARAM -> content = extractText(segment, 2);
            case FILE_NAME_PARAM -> imageFile = makeImageFile(segment);
        }
    }

    private String extractText(List<byte[]> segment, int startNum){
        byte[] textBytes = extractBytes(segment, startNum);
        return new String(textBytes);
    }

    private ImageFile makeImageFile(List<byte[]> segment) {
        String headerLine = new String(segment.get(0));
        String fileName = extractParameter(headerLine, FILENAME_EXTRACTION_REGEX);
        String contentType = extractContentType(new String(segment.get(1)));
        byte[] bytes = extractBytes(segment, 3);
        return new ImageFile(fileName, contentType, bytes);
    }

    private byte[] extractBytes(List<byte[]> segment, int startNum){
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i=startNum; i<segment.size(); i++) {
            buffer.write(segment.get(i), 0, segment.get(i).length);
        }
        return buffer.toByteArray();
    }

    private String extractContentType(String contentTypeLine){
        return contentTypeLine.split(COLON)[1].trim();
    }

    private String extractParameter(String line, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
           return matcher.group(1);
        }
        return null;
    }

    @Override
    public byte[] toBytes() {
        return rawBytes;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public ImageFile getImageFile() {
        return imageFile;
    }
}
