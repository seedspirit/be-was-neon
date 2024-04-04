package util;

public class ByteUtil {
    public static int findSubArray(byte[] source, byte[] target, int startFrom, int endIndex) {
        if (target.length == 0 || source.length < target.length || endIndex > source.length) {
            return -1;
        }

        for (int startIndex = startFrom; startIndex <= endIndex - target.length; startIndex++) {
            if (source[startIndex] == target[0]) {
                boolean isMatch = true;
                for (int offset = 1; offset < target.length; offset++) {
                    if (source[startIndex + offset] != target[offset]) {
                        isMatch = false;
                        break;
                    }
                }
                if (isMatch) {
                    return startIndex;
                }
            }
        }
        return -1;
    }
}
