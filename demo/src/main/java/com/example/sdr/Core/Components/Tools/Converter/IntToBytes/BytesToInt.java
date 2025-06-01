package com.example.sdr.Core.Components.Tools.Converter.IntToBytes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BytesToInt {

    // 将 4 字节 byte[] 转换为单个 int
    public static int bytesToInt(byte[] bytes) {
        if (bytes.length != 4) {
            throw new IllegalArgumentException("Byte array must be exactly 4 bytes to convert to an int.");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);  // 兼容 JavaScript
        return buffer.getInt();
    }

    // 将 byte[] 转换为 int[]，长度必须是 4 的倍数
    public static int[] bytesToIntArray(byte[] bytes) {
        if (bytes.length % 4 != 0) {
            throw new IllegalArgumentException("Byte array length must be a multiple of 4.");
        }

        int numInts = bytes.length / 4;
        int[] result = new int[numInts];

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);  // 兼容 JavaScript

        for (int i = 0; i < numInts; i++) {
            result[i] = buffer.getInt();
        }

        return result;
    }

}
