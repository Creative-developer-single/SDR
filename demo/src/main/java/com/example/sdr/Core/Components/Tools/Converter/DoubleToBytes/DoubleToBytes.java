package com.example.sdr.Core.Components.Tools.Converter.DoubleToBytes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DoubleToBytes {

    // 将单个 Double 转换为 8 字节 byte[]
    public static byte[] doubleToBytes(double value) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.BIG_ENDIAN);  // 兼容 JavaScript 默认大端序
        buffer.putDouble(value);
        return buffer.array();
    }

    // 将 Double 数组转换为 byte[]，连续存储
    public static byte[] doubleArrayToBytes(double[] values) {
        ByteBuffer buffer = ByteBuffer.allocate(8 * values.length);
        buffer.order(ByteOrder.BIG_ENDIAN);  // 兼容 JavaScript 默认大端序
        for (double value : values) {
            buffer.putDouble(value);
        }
        return buffer.array();
    }

}
