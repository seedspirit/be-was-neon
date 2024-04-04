package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class ImageFile {
    private String fileName;
    private byte[] fileBinary;
    private String contentType;
    private static final Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);


    public ImageFile(String fileName, String contentType, byte[] fileBinary){
        this.fileName = fileName;
        this.fileBinary = fileBinary;
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileBinary() {
        return fileBinary;
    }

    public void saveFile() {
        String filePath = "./src/main/resources/static/img/" + fileName;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileBinary, 0, fileBinary.length);
            logger.debug("이미지 파일이 성공적으로 생성되었습니다.");
        } catch (IOException e) {
            logger.error("파일 생성 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
