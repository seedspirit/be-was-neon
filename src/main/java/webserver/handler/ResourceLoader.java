package webserver.handler;

import webserver.exceptions.ResourceNotFoundException;

import java.io.*;

import static webserver.URLConstants.STATIC_DIR_PATH;

public class ResourceLoader {
    public byte[] load(String path) throws ResourceNotFoundException {
        String targetPath = STATIC_DIR_PATH + path;
        InputStream inputStream = this.getClass().getResourceAsStream(targetPath);
        checkInputStreamNull(inputStream);
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            final int BUFFER_SIZE = 1024;
            byte[] data = new byte[BUFFER_SIZE];
            int readSize;

            while ((readSize = bis.read(data, 0, BUFFER_SIZE)) != -1) {
                buffer.write(data, 0, readSize);
            }
            buffer.flush();
            return buffer.toByteArray();
        } catch (IOException e){
            throw new ResourceNotFoundException();
        }
    }


    private void checkInputStreamNull(InputStream inputStream) throws ResourceNotFoundException {
        if (inputStream == null) {
            throw new ResourceNotFoundException();
        }
    }
}
