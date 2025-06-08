package com.example.sdr.Core.ProjectManager.Components.Source;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

import java.util.Random;

/**
 * 随机高斯噪声信号发生器 (重构版)
 * <p>
 * 作为一个源组件，该组件没有输入，只有一个输出端口。
 * 它能生成指定能量（标准差）和类型（"Real" 或 "Complex" 字符串）的高斯白噪声。
 * 属性变量采用首字母大写命名法，以方便与前端对接。
 * </p>
 */
public class RandomGenerator extends BaseComponent {

    // ================== 自定义属性 (首字母大写) ==================

    /**
     * 噪声能量（标准差, σ）
     * 该值决定了生成噪声的幅度。
     */
    private double NoisePower = 1.0;

    /**
     * 噪声类型，使用字符串 "Real" 或 "Complex" 表示。默认为 "Real"。
     */
    private String NoiseType = "Real";


    // ================== internal Variables ==================

    /**
     * Java内置的随机数生成器
     */
    private final Random random;

    // ================== 构造函数 ==================

    public RandomGenerator(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, inputCount, outputCount, ID);
        this.random = new Random();
        this.setNoisePower(1.0); // 默认噪声能量为 1.0
        this.setNoiseType("Real"); // 默认噪声类型为 "Real"
        this.Type = "Driver"; // 设置组件类型为 "Driver"
    }

    @Override
    public void refreshComponent(){
        resetBlockLength(blockLength);
    }

    // ================== 重载核心方法 ==================

    @Override
    public void Calculate() {
        SDRData[] outputData = ans[0];

        // 使用字符串 equals 方法来判断噪声类型
        if ("Real".equals(this.NoiseType)) {
            // --- 生成实数高斯噪声 ---
            for (int i = 0; i < this.blockLength; i++) {
                double realNoise = random.nextGaussian() * this.NoisePower;
                outputData[i].fromDouble(realNoise);
            }
        } else if ("Complex".equals(this.NoiseType)) {
            // --- 生成复数高斯噪声 ---
            double scale = this.NoisePower / Math.sqrt(2.0);
            for (int i = 0; i < this.blockLength; i++) {
                double realPart = random.nextGaussian() * scale;
                double imagPart = random.nextGaussian() * scale;
                outputData[i].setReal(realPart);
                outputData[i].setImag(imagPart);
                outputData[i].setComputeMode(SDRData.ComputeMode.COMPLEX); // 设置为复数模式
            }
        }
        // 如果 NoiseType 字符串不是 "Real" 或 "Complex"，则不生成任何数据
    }

    @Override
    public Boolean ModifyPropertiesByName(String name, Object value) {
        try {
            // 注意：case 后的字符串现在是首字母大写
            switch (name) {
                case "NoisePower":
                    this.setNoisePower(Double.parseDouble(value.toString()));
                    return true;
                case "NoiseType":
                    this.setNoiseType(value.toString());
                    return true; // setNoiseType 内部包含验证
                default:
                    return super.ModifyPropertiesByName(name, value);
            }
        } catch (Exception e) {
            System.err.println("修改属性失败: " + name + ", 值: " + value + ". 错误: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public void setOperationParams(SDRData[] data, int index) {
        // 源组件没有输入，此方法不执行任何操作。
    }

    // ================== Getters 和 Setters ==================

    public double getNoisePower() {
        return NoisePower;
    }

    public void setNoisePower(double noisePower) {
        if (noisePower < 0) {
            throw new IllegalArgumentException("Noise power (standard deviation) cannot be negative.");
        }
        this.NoisePower = noisePower;
    }

    public String getNoiseType() {
        return NoiseType;
    }

    public void setNoiseType(String noiseType) {
        // 增加对输入字符串的验证，确保其值为 "Real" 或 "Complex"
        if ("Real".equals(noiseType) || "Complex".equals(noiseType)) {
            this.NoiseType = noiseType;
        } else {
            // 对于无效值，可以抛出异常或打印错误，这里选择抛出异常
            throw new IllegalArgumentException("NoiseType must be 'Real' or 'Complex', but received: " + noiseType);
        }
    }
}