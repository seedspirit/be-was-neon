package webserver.httpMessage.httpRequest.body.parser;

import util.ByteUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static util.constants.Delimiter.CRLF;

public class MultipartFormatParser {

    public List<List<byte[]>> splitBodyByBoundary(byte[] rawBytes, String boundary){
        List<byte[]> lines = splitByCRLF(rawBytes);
        return gatherByContents(lines, boundary);
    }

    private List<byte[]> splitByCRLF(byte[] requestData) {
        List<byte[]> line = new ArrayList<>();
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
                line.add(chunkBuffer.toByteArray());
                chunkBuffer.reset();

                // 다음 청크의 시작 인덱스 업데이트 (CRLF 시퀀스 이후)
                bufferIndex = crlfIndex + CRLFBytes.length;
            }
        }

        // 마지막 청크 처리
        if (chunkBuffer.size() > 0) {
            line.add(chunkBuffer.toByteArray());
        }

        return line;
    }

    private List<List<byte[]>> gatherByContents(List<byte[]> lines, String boundary){
        List<List<byte[]>> chunks = new ArrayList<>();
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
        return chunks;
    }
}
