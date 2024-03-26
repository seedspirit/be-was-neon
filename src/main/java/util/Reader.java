package util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Reader {
    public static String readLineFrom(BufferedInputStream bufferedInputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        int previous = -1;
        int current = 0;

        while((current = bufferedInputStream.read()) != -1) {
            if (previous == '\r' && current == '\n') {
                builder.deleteCharAt(builder.length()-1);
                break;
            }
            builder.append((char) current);
            previous = current;
        }

        if (previous != -1 && previous != '\r'){
            builder.append((char) previous);
        }
        return builder.toString();
    }

    public static byte[] readBodyFrom(BufferedInputStream bufferedInputStream, int contentLength) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        final int BUFFER_SIZE = 8192;
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
