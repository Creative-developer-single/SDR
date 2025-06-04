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
