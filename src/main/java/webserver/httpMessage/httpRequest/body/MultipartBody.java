package webserver.httpMessage.httpRequest.body;

import util.ByteUtil;
import webserver.Article;
import webserver.ImageFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.constants.Delimiter.*;

public class MultipartBody implements RequestBody {

    private final byte[] rawBytes;
    private final String boundary;
    private final String bodyEndBoundary;
    private final List<byte[]> lines;
    private List<List<byte[]>> chunks;
    private List<Map<String, byte[]>> chuchu;
    private List<String> str;
    private final String NAME_EXTRACTION_REGEX = "name=\"(.+?)\"";
    private final String FILENAME_EXTRACTION_REGEX = "filename=\"(.+?)\"";
    private Article article;
    private String title;
    private String content;
    private ImageFile imageFile;


    public MultipartBody(byte[] rawBytes, String boundary){
        this.rawBytes = rawBytes;
        this.boundary = boundary;
        this.bodyEndBoundary = boundary + "--";
        this.lines = splitByCRLF(rawBytes);
        this.chunks = new ArrayList<>();
        this.str = new ArrayList<>();
        gatherByContents();
        visualize(lines);
        createArticle();
    }

    public void gatherByContents(){
        List<byte[]> chunk = new ArrayList<>();
        for (byte[] line : lines) {
            if(ByteUtil.findSubArray(line, boundary.getBytes(), 0, line.length) != -1){
                if(!chunk.isEmpty()){
                    chunks.add(chunk);
                    chunk = new ArrayList<>();
                }
                continue;
            }
            chunk.add(line);
        }
        if (!chunk.isEmpty()){
            chunks.add(chunk);
        }
    }

    private void createArticle(){
        for (List<byte[]> chunk: chunks){
            String paramName = extract(new String(chunk.get(0)), NAME_EXTRACTION_REGEX);
            switch (paramName) {
                case "title" -> {
                    byte[] tmp1 = makeTitleOrContent(chunk);
                    title = new String(tmp1);
                }
                case "content" -> {
                    byte[] tmp2 = makeTitleOrContent(chunk);
                    content = new String(tmp2);
                }
                case "file" -> {
                    imageFile = makeImageFile(chunk);
                }
            }
        }
        this.article = new Article(1234L, title, content, imageFile);
        imageFile.saveFile();
    }

    private ImageFile makeImageFile(List<byte[]> chunk) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        String fileName = extract(new String(chunk.get(0)), FILENAME_EXTRACTION_REGEX);
        String contentType = new String(chunk.get(1));
        String type = contentType.split(COLON)[1].trim();

        for (int i=3; i<chunk.size(); i++){
            buffer.write(chunk.get(i), 0, chunk.get(i).length);
        }
        return new ImageFile(fileName, type, buffer.toByteArray());
    }

    private byte[] makeTitleOrContent(List<byte[]> chunk){
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i=2; i<chunk.size(); i++) {
            buffer.write(chunk.get(i), 0, chunk.get(i).length);
        }
        return buffer.toByteArray();
    }

    private String extract(String line, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
           return matcher.group(1);
        }
        return null;
    }


    private List<byte[]> splitByCRLF(byte[] requestData) {
        List<byte[]> chunks = new ArrayList<>();
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        byte[] CRLFBytes = CRLF.getBytes();  // CRLF 바이트 시퀀스
        int dataIndex = 0;  // requestData 내 현재 처리 중인 데이터의 인덱스
        ByteArrayOutputStream chunkBuffer = new ByteArrayOutputStream();

        while (dataIndex < requestData.length) {
            int lengthToRead = Math.min(BUFFER_SIZE, requestData.length - dataIndex);
            System.arraycopy(requestData, dataIndex, buffer, 0, lengthToRead);
            dataIndex += lengthToRead;

            // 버퍼 내에서 CRLF 검색
            int bufferIndex = 0;  // 현재 버퍼 내 처리 중인 인덱스
            while (bufferIndex < lengthToRead) {
                int crlfIndex = ByteUtil.findSubArray(buffer, CRLFBytes, bufferIndex, lengthToRead);
                if (crlfIndex == -1) {
                    // CRLF를 찾지 못한 경우, 남은 모든 데이터를 현재 청크에 추가
                    chunkBuffer.write(buffer, bufferIndex, lengthToRead - bufferIndex);
                    break;
                }

                // CRLF 시퀀스 이전까지의 데이터를 청크로 추가
                chunkBuffer.write(buffer, bufferIndex, crlfIndex - bufferIndex + 2);
                chunks.add(chunkBuffer.toByteArray());
                chunkBuffer.reset();

                // 다음 청크의 시작 인덱스 업데이트 (CRLF 시퀀스 이후)
                bufferIndex = crlfIndex + CRLFBytes.length;
            }
        }

        // 마지막 청크 처리
        if (chunkBuffer.size() > 0) {
            chunks.add(chunkBuffer.toByteArray());
        }

        return chunks;
    }

    public void visualize(List<byte[]> chunks){
        for (byte[] chunk : chunks) {
            str.add(new String(chunk));
        }
    }

    public byte[] toBytes() {
        return rawBytes;
    }
}
