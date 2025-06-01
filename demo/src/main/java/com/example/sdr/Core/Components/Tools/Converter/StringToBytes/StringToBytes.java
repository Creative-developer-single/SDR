package com.example.sdr.Core.Components.Tools.Converter.StringToBytes;

import java.nio.charset.StandardCharsets;

public class StringToBytes {

    // 将 String 转换为 byte[]，使用 UTF-8 编码
    public static byte[] stringToBytes(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Input string cannot be null.");
        }
        return str.getBytes(StandardCharsets.UTF_8);
    }

}
