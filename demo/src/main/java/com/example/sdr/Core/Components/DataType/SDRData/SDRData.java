package com.example.sdr.Core.Components.DataType.SDRData;

public class SDRData {

    public enum DataType {
        REAL,
        COMPLEX
    }

    public enum ComputeMode {
        REAL,
        COMPLEX
    }

    private DataType type;
    private double real;
    private double imag;
    private ComputeMode computeMode;

    // ====== 构造器 ======

    // 实数
    public SDRData(double real) {
        this.type = DataType.REAL;
        this.real = real;
        this.imag = 0.0;
        this.computeMode = ComputeMode.REAL;
    }

    // 复数
    public SDRData(double real, double imag) {
        this.type = DataType.COMPLEX;
        this.real = real;
        this.imag = imag;
        this.computeMode = ComputeMode.COMPLEX;
    }

    // ====== 计算模式控制 ======

    public void setComputeMode(ComputeMode mode) {
        this.computeMode = mode;
        if (mode == ComputeMode.REAL) {
            this.imag = 0.0;  // 不释放 imag（单个数无所谓，直接归零）
            this.type = DataType.REAL;
        } else {
            this.type = DataType.COMPLEX;
        }
    }

    public ComputeMode getComputeMode() {
        return computeMode;
    }

    // ====== 基本 get/set ======

    public double getReal() {
        return real;
    }

    public double getImag() {
        return imag;
    }

    public DataType getType() {
        return type;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public void setImag(double imag) {
        this.imag = imag;
    }

    // ====== 运算接口（就地计算，覆盖本身） ======

    public void add(SDRData other) {
        double r2 = other.real;
        double i2 = (other.computeMode == ComputeMode.REAL) ? 0.0 : other.imag;

        if (this.computeMode == ComputeMode.REAL && other.computeMode == ComputeMode.REAL) {
            this.real += r2;
        } else {
            double i1 = (this.computeMode == ComputeMode.REAL) ? 0.0 : this.imag;
            this.real += r2;
            this.imag = i1 + i2;
            this.computeMode = ComputeMode.COMPLEX;  // 结果是复数
            this.type = DataType.COMPLEX;
        }
    }

    public void subtract(SDRData other) {
        double r2 = other.real;
        double i2 = (other.computeMode == ComputeMode.REAL) ? 0.0 : other.imag;

        if (this.computeMode == ComputeMode.REAL && other.computeMode == ComputeMode.REAL) {
            this.real -= r2;
        } else {
            double i1 = (this.computeMode == ComputeMode.REAL) ? 0.0 : this.imag;
            this.real -= r2;
            this.imag = i1 - i2;
            this.computeMode = ComputeMode.COMPLEX;
            this.type = DataType.COMPLEX;
        }
    }

    public void multiply(SDRData other) {
        double r1 = this.real;
        double i1 = (this.computeMode == ComputeMode.REAL) ? 0.0 : this.imag;
        double r2 = other.real;
        double i2 = (other.computeMode == ComputeMode.REAL) ? 0.0 : other.imag;

        double realPart = r1 * r2 - i1 * i2;
        double imagPart = r1 * i2 + i1 * r2;

        this.real = realPart;
        this.imag = imagPart;

        if (this.imag == 0.0) {
            this.computeMode = ComputeMode.REAL;
            this.type = DataType.REAL;
        } else {
            this.computeMode = ComputeMode.COMPLEX;
            this.type = DataType.COMPLEX;
        }
    }

    // 除法
    public void divide(SDRData other) {
        if (other.computeMode == ComputeMode.REAL && other.real == 0.0) {
            throw new ArithmeticException("Division by zero");
        }

        double r1 = this.real;
        double i1 = (this.computeMode == ComputeMode.REAL) ? 0.0 : this.imag;
        double r2 = other.real;
        double i2 = (other.computeMode == ComputeMode.REAL) ? 0.0 : other.imag;

        double denominator = r2 * r2 + i2 * i2;
        if (denominator == 0.0) {
            throw new ArithmeticException("Division by zero");
        }

        double realPart = (r1 * r2 + i1 * i2) / denominator;
        double imagPart = (i1 * r2 - r1 * i2) / denominator;

        this.real = realPart;
        this.imag = imagPart;

        if (this.imag == 0.0) {
            this.computeMode = ComputeMode.REAL;
            this.type = DataType.REAL;
        } else {
            this.computeMode = ComputeMode.COMPLEX;
            this.type = DataType.COMPLEX;
        }
    }

    // 求幅度的平方，赋予本身
    public void power() {
        if (this.computeMode == ComputeMode.REAL) {
            // 实数的平方
            this.real = this.real * this.real;
            this.imag = 0.0; // 保持为实数
            this.computeMode = ComputeMode.REAL;
            this.type = DataType.REAL;
        } else {
            // 复数的幅度平方
            double magnitudeSquared = this.real * this.real + this.imag * this.imag;
            this.real = magnitudeSquared;
            this.imag = 0.0; // 保持为实数
            this.computeMode = ComputeMode.REAL;
            this.type = DataType.REAL;
        }
    }

    public void abs(){
        if (this.computeMode == ComputeMode.REAL) {
            // 实数的绝对值
            this.real = Math.abs(this.real);
            this.imag = 0.0; // 保持为实数
            this.computeMode = ComputeMode.REAL;
            this.type = DataType.REAL;
        } else {
            // 复数的绝对值
            double magnitude = Math.sqrt(this.real * this.real + this.imag * this.imag);
            this.real = magnitude;
            this.imag = 0.0; // 保持为实数
            this.computeMode = ComputeMode.REAL;
            this.type = DataType.REAL;
        }
    }

    // 计算相位，赋予本身
    public void phase() {
        if (this.computeMode == ComputeMode.REAL) {
            // 实数的相位为0
            this.real = 0.0;
            this.imag = 0.0; // 保持为实数
            this.computeMode = ComputeMode.REAL;
            this.type = DataType.REAL;
        } else {
            // 复数的相位
            double phase = Math.atan2(this.imag, this.real);
            this.real = phase;
            this.imag = 0.0; // 保持为实数
            this.computeMode = ComputeMode.REAL;
            this.type = DataType.REAL;
        }
    }

    // 计算最小值，赋予本身
    public void Min(SDRData other) {
        if (this.computeMode == ComputeMode.REAL && other.computeMode == ComputeMode.REAL) {
            this.real = Math.min(this.real, other.real);
            this.imag = 0.0; // 保持为实数
            this.computeMode = ComputeMode.REAL;
            this.type = DataType.REAL;
        } else {
            double minReal = Math.min(this.real, other.real);
            double minImag = Math.min(this.imag, other.imag);
            this.real = minReal;
            this.imag = minImag;
            this.computeMode = ComputeMode.COMPLEX;
            this.type = DataType.COMPLEX;
        }
    }

    // 计算最大值，赋予本身
    public void Max(SDRData other){
        if (this.computeMode == ComputeMode.REAL && other.computeMode == ComputeMode.REAL) {
            this.real = Math.max(this.real, other.real);
            this.imag = 0.0; // 保持为实数
            this.computeMode = ComputeMode.REAL;
            this.type = DataType.REAL;
        } else {
            double maxReal = Math.max(this.real, other.real);
            double maxImag = Math.max(this.imag, other.imag);
            this.real = maxReal;
            this.imag = maxImag;
            this.computeMode = ComputeMode.COMPLEX;
            this.type = DataType.COMPLEX;
        }
    }

    // 比较
    public boolean equals(SDRData other) {
        if (this.computeMode != other.computeMode) {
            return false;
        }
        if (this.computeMode == ComputeMode.REAL) {
            return Double.compare(this.real, other.real) == 0;
        } else {
            return Double.compare(this.real, other.real) == 0 && Double.compare(this.imag, other.imag) == 0;
        }
    }

    // 从double中转换
    public void fromDouble(double value) {
        this.real = value;
        this.imag = 0.0; // 保持为实数
        this.computeMode = ComputeMode.REAL;
        this.type = DataType.REAL;
    }

    // 从Complex数中转换
    public void fromComplex(double real, double imag) {
        this.real = real;
        this.imag = imag;
        
        this.computeMode = ComputeMode.COMPLEX;
        this.type = DataType.COMPLEX;
        
    }

    public boolean equals(double value) {
        if (this.computeMode == ComputeMode.REAL) {
            return Double.compare(this.real, value) == 0;
        } else {
            return Double.compare(this.real, value) == 0 && Double.compare(this.imag, 0.0) == 0;
        }
    }

    public SDRData getCopy() {
        return new SDRData(real, imag);
    }

    public void Copy(SDRData other) {
        this.real = other.real;
        this.imag = other.imag;
        this.computeMode = other.computeMode;
        this.type = other.type;
    }

    public double getSingleValue(){
        if (computeMode == ComputeMode.REAL) {
            return real;
        }else {
            return imag;
        }
    }

    @Override
    public String toString() {
        if (computeMode == ComputeMode.REAL) {
            return String.format("SDRData [REAL] %.6f", real);
        } else {
            return String.format("SDRData [COMPLEX] %.6f + j%.6f", real, imag);
        }
    }
}
