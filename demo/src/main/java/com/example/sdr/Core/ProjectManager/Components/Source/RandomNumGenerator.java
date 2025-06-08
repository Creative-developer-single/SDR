package com.example.sdr.Core.ProjectManager.Components.Source;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

import java.util.Random;

/**
 * 随机比特序列发生器（时间驱动插值版）
 *
 * 用于生成一个随机的比特流（信号层面映射为+1和-1），可与成型滤波器配合使用。
 * 本版本使用"时间驱动插值"，保证符号切换严格按时间驱动，窗口间连续性好，避免jitter。
 */
public class RandomNumGenerator extends BaseComponent {

    // ================== 自定义属性 ==================

    /**
     * 比特速率 (bits per second)
     */
    private double Rb = 1000.0; // 默认 1 kbps

    // ================== 内部状态变量 ==================

    /**
     * 符号相位累加器 (单位: 符号周期的归一化相位，0.0~1.0)
     */
    private double symbolPhase = 0.0;

    /**
     * 每个采样点推进的符号相位增量 (Rb / SampleRate)
     */
    private double symbolPhaseIncrement = 0.0;

    /**
     * 当前符号值 (保持在整个符号周期内，+1 或 -1)
     */
    private double currentBitValue = 1.0;

    /**
     * 随机数生成器
     */
    private final Random RandomGen;

    // ================== 构造函数 ==================

    public RandomNumGenerator(int blockLength, int inputCount, int outputCount, String ID) {
        // 作为源，输入为0，输出为1
        super(blockLength, 0, 1, ID);
        this.RandomGen = new Random();
        this.Type = "Driver";

        // 初始化状态
        this.refreshComponent();
    }

    // ================== 核心计算方法 ==================
    @Override
    public void Calculate() {
        // 清空输出缓存
        for (int i = 0; i < this.blockLength; i++) {
            this.ans[0][i].fromDouble(0);
            this.ans[0][i].setComputeMode(SDRData.ComputeMode.REAL);
        }

        // 时间驱动插值：按采样时钟推进符号相位
        for (int i = 0; i < this.blockLength; i++) {
            // 推进相位
            this.symbolPhase += this.symbolPhaseIncrement;

            // 如果相位 >= 1，进入下一个符号
            if (this.symbolPhase >= 1.0) {
                this.symbolPhase -= 1.0;
                this.currentBitValue = this.RandomGen.nextBoolean() ? -1.0 : 1.0;
            }

            // 当前采样点输出当前符号值
            this.ans[0][i].fromDouble(this.currentBitValue);
        }
    }

    // ================== 刷新组件状态 ==================

    @Override
    public void refreshComponent() {
        // 重置相位
        this.symbolPhase = 0.0;

        // 计算符号相位增量
        if (this.Rb > 0 && this.SampleRate > 0) {
            this.symbolPhaseIncrement = this.Rb / this.SampleRate;
        } else {
            this.symbolPhaseIncrement = 0.0;
        }

        // 重置输出数组长度
        resetBlockLength(blockLength);

        // 初始化当前符号
        this.currentBitValue = this.RandomGen.nextBoolean() ? -1.0 : 1.0;
    }

    // ================== 修改属性接口 ==================

    @Override
    public Boolean ModifyPropertiesByName(String name, Object value) {
        try {
            if ("Rb".equals(name)) {
                this.setRb(Double.parseDouble(value.toString()));
                return true;
            }
            return super.ModifyPropertiesByName(name, value);
        } catch (Exception e) {
            System.err.println("修改属性失败: " + name + ", 值: " + value + ". 错误: " + e.getMessage());
            return false;
        }
    }

    // ================== Getters 和 Setters ==================

    public double getRb() {
        return Rb;
    }

    public void setRb(double rb) {
        if (rb <= 0) {
            throw new IllegalArgumentException("Rb (bit rate) must be a positive number.");
        }
        this.Rb = rb;
        // 当比特率发生变化时，更新内部状态
        this.refreshComponent();
    }
}
