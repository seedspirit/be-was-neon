package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.httpMessage.ContentType;

import java.io.*;

import static webserver.URLConstants.IMAGE_DIRECTORY_FULL_PATH;


public class ImageFile {
    private String fileName;
    private byte[] fileBinary;
    private final ContentType contentType;
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);


    public ImageFile(String fileName, String mimeType, byte[] fileBinary){
        this.fileName = fileName;
        this.fileBinary = fileBinary;
        this.contentType = ContentType.findContentTypeByMimeType(mimeType);
    }

    public void saveFileInServer() {
        String filePath = IMAGE_DIRECTORY_FULL_PATH + fileName;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileBinary, 0, fileBinary.length);
            logger.debug("이미지 파일이 성공적으로 생성되었습니다.");
        } catch (IOException e) {
            logger.error("파일 생성 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileBinary() {
        return fileBinary;
    }
}
