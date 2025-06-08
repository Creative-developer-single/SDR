package com.example.sdr.Core.ProjectManager.Components.Source;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

import java.util.Random;

/**
 * 随机比特序列发生器
 * <p>
 * 用于生成一个随机的比特流（在信号层面映射为+1和-1），常用于通信链路的测试。
 * 该组件的关键特性是能够根据比特率（Rb）和采样率（SampleRate）自动调整，
 * 生成一个符合sps（samples-per-symbol）的脉冲序列。
 * </p>
 * <p>
 * 它具有记忆性，可以跨越多次`Calculate()`调用连续生成码流。
 * 只有在调用`refreshComponent()`时，其内部相位（计时器）才会被重置。
 * </p>
 */
public class RandomNumGenerator extends BaseComponent {

    // ================== 自定义属性 (首字母大写) ==================

    /**
     * 比特速率 (bits per second)
     */
    private double Rb = 1000.0; // 默认1kbps

    // ================== 内部状态变量 ==================

    /**
     * 每个比特持续的采样点数 (Samples per Bit)
     * 由 SampleRate / Rb 计算得出
     */
    private double SamplesPerBit;

    /**
     * 时间累加器/相位计时器 (单位：采样点)
     * 用于追踪当前比特周期的剩余时间，实现模块的记忆性。
     * 当该值小于等于0时，生成一个新的比特。
     */
    private double TimeAccumulator = 0.0;
    
    /**
     * 随机数生成工具
     */
    private final Random RandomGen;

    // ================== 构造函数 ==================

    public RandomNumGenerator(int blockLength, int inputCount, int outputCount,String ID) {
        // 作为源，输入为0，输出为1
        super(blockLength, 0, 1, ID);
        this.RandomGen = new Random();
        
        // 初始化时，计算一次sps并重置状态
        this.refreshComponent();
    }

    // ================== 重载核心方法 ==================

    /**
     * 核心计算方法
     * <p>
     * 根据内部计时器生成一个长度为 blockLength 的脉冲序列。
     * 每个脉冲代表一个比特，脉冲之间的间隔由 SamplesPerBit 决定。
     * </p>
     */
    @Override
    public void Calculate() {
        // 首先，将输出缓存区清零
        for(int i = 0; i < this.blockLength; i++) {
            this.ans[0][i].fromDouble(0);
            this.ans[0][i].setComputeMode(SDRData.ComputeMode.REAL);
        }
        
        // 遍历当前处理块的每一个采样点
        for (int i = 0; i < this.blockLength; i++) {
            // 如果计时器小于等于0，说明一个比特的周期已经结束，需要生成新比特
            if (this.TimeAccumulator <= 0.0) {
                // 1. 生成一个随机比特，并映射到+1或-1（BPSK调制）
                double bitValue = this.RandomGen.nextBoolean() ? -1 : 1;
                this.ans[0][i].fromDouble(bitValue);

                // 2. 为计时器增加一个完整的比特周期
                this.TimeAccumulator += this.SamplesPerBit;
            }
            
            // 3. 无论是否生成了新比特，每个采样点都使计时器减1，模拟时间的流逝
            this.TimeAccumulator -= 1.0;
        }
    }

    /**
     * 重置组件状态
     * <p>
     * 此方法用于重置内部的相位计时器，并根据当前的Rb和SampleRate重新计算sps。
     * 这满足了“保留记忆性，直到被外部调用重置”的需求。
     * </p>
     */
    @Override
    public void refreshComponent() {
        // 1. 重置相位/时间累加器
        this.TimeAccumulator = 0.0;

        // 2. 根据最新的Rb和SampleRate重新计算每个比特占用的采样点数
        if (this.Rb > 0 && this.SampleRate > 0) {
            this.SamplesPerBit = (double)this.SampleRate / this.Rb;
        } else {
            // 防止除零错误，如果速率无效，则设置为一个极大值
            this.SamplesPerBit = Double.POSITIVE_INFINITY;
        }
    }

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
        // 当比特率发生变化时，必须调用refreshComponent来更新内部状态
        this.refreshComponent();
    }
}