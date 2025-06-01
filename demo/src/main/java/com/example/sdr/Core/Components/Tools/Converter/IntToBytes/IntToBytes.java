package com.example.sdr.Core.Components.Tools.Converter.IntToBytes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class IntToBytes {

    // 将单个 int 转换为 4 字节 byte[]
    public static byte[] intToBytes(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.BIG_ENDIAN);  // 兼容 JavaScript
        buffer.putInt(value);
        return buffer.array();
    }

    // 将 int[] 转换为 byte[]，连续存储
    public static byte[] intArrayToBytes(int[] values) {
        ByteBuffer buffer = ByteBuffer.allocate(4 * values.length);
        buffer.order(ByteOrder.BIG_ENDIAN);  // 兼容 JavaScript
        for (int value : values) {
            buffer.putInt(value);
        }
        return buffer.array();
    }

}
