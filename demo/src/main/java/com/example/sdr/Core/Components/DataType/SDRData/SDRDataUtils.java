package com.example.sdr.Core.Components.DataType.SDRData;

import java.util.Arrays;

public class SDRDataUtils {

    // ===== 单个数创建 =====

    public static SDRData createReal(double value) {
        return new SDRData(value);
    }

    public static SDRData createComplex(double real, double imag) {
        return new SDRData(real, imag);
    }

    public static SDRData createComplexPolar(double magnitude, double phase) {
        double real = magnitude * Math.cos(phase);
        double imag = magnitude * Math.sin(phase);
        return new SDRData(real, imag);
    }

    // ===== 批量创建 SDRData[]，默认 REAL =====

    public static SDRData[] createRealArray(int size, double initValue) {
        SDRData[] array = new SDRData[size];
        for (int i = 0; i < size; i++) {
            array[i] = createReal(initValue);
        }
        return array;
    }

    // ===== 批量创建 SDRData[]，COMPLEX =====

    public static SDRData[] createComplexArray(int size, double initReal, double initImag) {
        SDRData[] array = new SDRData[size];
        for (int i = 0; i < size; i++) {
            array[i] = createComplex(initReal, initImag);
        }
        return array;
    }

    // ===== 批量创建 COMPLEX POLAR =====

    public static SDRData[] createComplexPolarArray(int size, double magnitude, double phase) {
        SDRData[] array = new SDRData[size];
        for (int i = 0; i < size; i++) {
            array[i] = createComplexPolar(magnitude, phase);
        }
        return array;
    }

    // ===== 从double数组创建 SDRData[] =====
    public static SDRData[] fromDoubleArray(double[] values) {
        SDRData[] array = new SDRData[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i].fromDouble(values[i]);
        }
        return array;
    }

    // ===== 二维数组创建 =====

    // REAL 矩阵
    public static SDRData[][] createRealMatrix(int numRows, int numCols, double initValue) {
        SDRData[][] matrix = new SDRData[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                matrix[i][j] = createReal(initValue);
            }
        }
        return matrix;
    }

    // COMPLEX 矩阵
    public static SDRData[][] createComplexMatrix(int numRows, int numCols, double initReal, double initImag) {
        SDRData[][] matrix = new SDRData[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                matrix[i][j] = createComplex(initReal, initImag);
            }
        }
        return matrix;
    }

    // COMPLEX 极坐标 矩阵
    public static SDRData[][] createComplexPolarMatrix(int numRows, int numCols, double magnitude, double phase) {
        SDRData[][] matrix = new SDRData[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                matrix[i][j] = createComplexPolar(magnitude, phase);
            }
        }
        return matrix;
    }

    /*
     * 转换到其他
     */
    public static double[] toDoubleArray(SDRData[] data) {
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i].getSingleValue(); // 只取实部
        }
        return result;
    }

    public static double[] toRealArray(SDRData[] data) {
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i].getReal(); // 只取实部
        }
        return result;
    }

    public static double[] toImaginaryArray(SDRData[] data) {
        double[] result = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i].getImag(); // 只取虚部
        }
        return result;
    }

    // 转换到double数组，1D Complex格式，奇数表示实部，偶数表示虚部
    public static double[] toComplexArray(SDRData[] data) {
        double[] result = new double[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            result[i * 2] = data[i].getReal(); // 实部
            result[i * 2 + 1] = data[i].getImag(); // 虚部
        }
        return result;
    }

    // 从1D Complex格式的double数组转换回 SDRData 数组
    public static SDRData[] fromComplexArray(double[] values) {
        if (values.length % 2 != 0) {
            throw new IllegalArgumentException("Array length must be even for complex data");
        }
        SDRData[] data = new SDRData[values.length / 2];
        for (int i = 0; i < data.length; i++) {
            data[i] = createComplex(values[i * 2], values[i * 2 + 1]);
        }
        return data;
    }

    //转换到二进制，先实部后虚部，四个字节表示一个double
    public static byte[] toByteArray(SDRData[] data) {
        byte[] result = new byte[data.length * 16]; // 每个 double 占 8 字节，实部和虚部各占 8 字节
        for (int i = 0; i < data.length; i++) {
            long realBits = Double.doubleToLongBits(data[i].getReal());
            long imagBits = Double.doubleToLongBits(data[i].getImag());
            for (int j = 0; j < 8; j++) {
                result[i * 16 + j] = (byte) (realBits >>> (56 - j * 8));
                result[i * 16 + j + 8] = (byte) (imagBits >>> (56 - j * 8));
            }
        }
        return result;
    }

    // 转换到二进制，但是只转换Length个数
    public static byte[] toByteArray(SDRData[] data, int length) {
        if (length > data.length) {
            throw new IllegalArgumentException("Length cannot be greater than data array size");
        }
        byte[] result = new byte[length * 16]; // 每个 double 占 8 字节，实部和虚部各占 8 字节
        for (int i = 0; i < length; i++) {
            long realBits = Double.doubleToLongBits(data[i].getReal());
            long imagBits = Double.doubleToLongBits(data[i].getImag());
            for (int j = 0; j < 8; j++) {
                result[i * 16 + j] = (byte) (realBits >>> (56 - j * 8));
                result[i * 16 + j + 8] = (byte) (imagBits >>> (56 - j * 8));
            }
        }
        return result;
    }

    // 从二进制转换回 SDRData 数组
    public static SDRData[] fromByteArray(byte[] bytes) {
        if (bytes.length % 16 != 0) {
            throw new IllegalArgumentException("Byte array length must be a multiple of 16");
        }
        int size = bytes.length / 16;
        SDRData[] data = new SDRData[size];
        for (int i = 0; i < size; i++) {
            long realBits = 0;
            long imagBits = 0;
            for (int j = 0; j < 8; j++) {
                realBits |= ((long) (bytes[i * 16 + j] & 0xFF)) << (56 - j * 8);
                imagBits |= ((long) (bytes[i * 16 + j + 8] & 0xFF)) << (56 - j * 8);
            }
            data[i] = new SDRData(Double.longBitsToDouble(realBits), Double.longBitsToDouble(imagBits));
        }
        return data;
    }

    /*
     * 非创建类，批量操作类
     */
    public static void toRealMode(SDRData[] data) {
        for (SDRData d : data) {
            d.setComputeMode(SDRData.ComputeMode.REAL);
        }
    }

    public static void toComplexMode(SDRData[] data) {
        for (SDRData d : data) {
            d.setComputeMode(SDRData.ComputeMode.COMPLEX);
        }
    }

    // 补0到指定长度
    public static SDRData[] padWithZeros(SDRData[] data, int targetLength) {
        if (data.length >= targetLength) {
            // 如果原数组长度已经大于或等于目标长度，直接返回targetLength长度的数据
            return Arrays.copyOf(data, targetLength);

        }
        SDRData[] paddedArray = new SDRData[targetLength];
        System.arraycopy(data, 0, paddedArray, 0, data.length);
        for (int i = data.length; i < targetLength; i++) {
            paddedArray[i] = createReal(0); // 填充0
        }
        return paddedArray;
    }

    // 转换所有值为幅度
    public static SDRData[] toMagnitudeArray(SDRData[] data) {
        SDRData[] magnitudeArray = new SDRData[data.length];
        for (int i = 0; i < data.length; i++) {
            double magnitude = Math.sqrt(Math.pow(data[i].getReal(), 2) + Math.pow(data[i].getImag(), 2));
            magnitudeArray[i] = createReal(magnitude);
        }
        return magnitudeArray;
    }

    // 转换所有值为相位
    public static SDRData[] toPhaseArray(SDRData[] data) {
        SDRData[] phaseArray = new SDRData[data.length];
        for (int i = 0; i < data.length; i++) {
            double phase = Math.atan2(data[i].getImag(), data[i].getReal());
            phaseArray[i] = createReal(phase);
        }
        return phaseArray;
    }
}
