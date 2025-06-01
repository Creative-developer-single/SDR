package com.example.sdr.Core.Components.Tools.Converter.StringToBytes;

import java.nio.charset.StandardCharsets;

public class BytesToString {

    // 将 byte[] 转换为 String，使用 UTF-8 解码
    public static String bytesToString(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Input byte array cannot be null.");
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
