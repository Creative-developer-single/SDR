package com.example.sdr.Core.Components.Tools.BytesTools;

public class BytesConcat {
    public static byte[] concat(byte[]... arrays) {
        // 计算总长度
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }
    
        // 申请目标数组
        byte[] result = new byte[totalLength];
        int currentPos = 0;
    
        // 逐个拷贝
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, currentPos, array.length);
            currentPos += array.length;
        }
    
        return result;
    }
}
