package com.example.sdr.Core.Components.DataType.SDRData;

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

}
