package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Reader {
    public static String readLineFrom(BufferedInputStream bufferedInputStream) throws IOException {
        ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
        int readByte;
        boolean foundNewLine = false;

        while (!foundNewLine && (readByte = bufferedInputStream.read()) != -1) {
            if (readByte == '\n') {
                foundNewLine = true;
            } else if (readByte != '\r') {
                bufferStream.write(readByte);
            }
        }
        return bufferStream.toString(StandardCharsets.UTF_8);
    }

    public static byte[] readBodyFrom(BufferedInputStream bufferedInputStream, int contentLength) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int totalReadBytes = 0;

        while (totalReadBytes < contentLength) {
            int windowSize = Math.min(contentLength - totalReadBytes, BUFFER_SIZE);
            int readBytes = bufferedInputStream.read(buffer, 0, windowSize);
            if (readBytes == -1) {
                break;
            }
            byteArrayOutputStream.write(buffer, 0, readBytes);
            totalReadBytes += readBytes;
        }
        return byteArrayOutputStream.toByteArray();
    }
}
