package com.idemia.model;

public final class HexUtils {
    private final static String[] HEX_CHARS = new String[] { "0",
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "A",
            "B",
            "C",
            "D",
            "E",
            "F" };

    public static String byte2hex(Byte b) {
        int high = (b & 0xf0) >> 4;
        int low = b & 0x0f;
        return HEX_CHARS[high] + HEX_CHARS[low];
    }
}
