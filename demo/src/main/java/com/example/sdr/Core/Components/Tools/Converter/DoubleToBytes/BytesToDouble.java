package com.example.sdr.Core.Components.Tools.Converter.DoubleToBytes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BytesToDouble {

    // 将 8 字节 byte[] 转换为单个 double
    public static double bytesToDouble(byte[] bytes) {
        if (bytes.length != 8) {
            throw new IllegalArgumentException("Byte array must be exactly 8 bytes to convert to a double.");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);  // 兼容 JavaScript
        return buffer.getDouble();
    }

    // 将 byte[] 转换为 double[]，长度必须是 8 的倍数
    public static double[] bytesToDoubleArray(byte[] bytes) {
        if (bytes.length % 8 != 0) {
            throw new IllegalArgumentException("Byte array length must be a multiple of 8.");
        }

        int numDoubles = bytes.length / 8;
        double[] result = new double[numDoubles];

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);  // 兼容 JavaScript

        for (int i = 0; i < numDoubles; i++) {
            result[i] = buffer.getDouble();
        }

        return result;
    }

}
