package com.example.sdr.Core.ProjectManager.Components.AFC;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;
import java.util.Arrays;

/**
 * 自动增益控制 (Automatic Gain Control)
 * <p>
 * 该模块通过一个反馈环路动态调整信号增益，使输出信号的功率稳定在一个预设的目标水平。
 * 它使用一个滑动窗口来计算信号的短期平均功率，并据此调整增益。
 * </p>
 */
public class AGC extends BaseComponent {

    // ================== 自定义属性 (首字母大写) ==================
    private double TargetLevel;     // 目标功率水平
    private double Step;            // 增益调整步长 (环路增益)
    private int WindowLength;       // 用于功率判决的滑动窗口长度

    // ================== 内部状态变量 ==================
    private double currentGain;         // 当前增益值，具有记忆性
    private double[] powerWindowBuffer; // 存储最近功率值的滑动窗口
    private int windowIndex;            // 指向滑动窗口中下一个要被替换的位置
    private double currentPowerSum;     // 当前窗口内功率的总和，用于高效计算平均值

    public AGC(int blockLength,int inputCount, int outputCount, String ID) {
        // AGC 是1输入，1输出的组件
        super(blockLength, inputCount, outputCount, ID);
        this.TargetLevel = 1.0; // 默认目标功率水平为1
        this.Step = 0.01;       // 默认增益调整步长
        this.WindowLength = 10; // 默认滑动窗口长度
        
        // 初始化状态
        refreshComponent();
    }

    // ================== 构造函数 ==================
    public AGC(int blockLength, double targetLevel, double step, int windowLength, String ID) {
        // AGC 是1输入，1输出的组件
        super(blockLength, 1, 1, ID);
        this.TargetLevel = targetLevel;
        this.Step = step;
        this.WindowLength = windowLength;
        
        // 初始化状态
        refreshComponent();
    }

    /**
     * 重置AGC的状态
     */
    @Override
    public void refreshComponent() {
        this.currentGain = 1.0; // 初始增益为1
        this.powerWindowBuffer = new double[this.WindowLength];
        Arrays.fill(this.powerWindowBuffer, 0.0); // 初始时窗口内无功率
        this.windowIndex = 0;
        this.currentPowerSum = 0.0;
    }

    /**
     * 核心计算方法，逐点执行AGC反馈逻辑
     */
    @Override
    public void Calculate() {
        if (op_in == null || op_in[0] == null) {
            return;
        }

        // 逐个采样点处理
        for (int i = 0; i < this.blockLength; i++) {
            // 1. 对输入信号应用当前增益
            SDRData inputSample = op_in[0][i];
            SDRData outputSample = inputSample.getCopy();
            outputSample.setReal(inputSample.getReal() * this.currentGain);
            outputSample.setImag(inputSample.getImag() * this.currentGain);
            this.ans[0][i] = outputSample;

            // 2. 测量输出信号的瞬时功率 (幅度的平方)
            SDRData powerSample = outputSample.getCopy();
            powerSample.power();
            double currentSamplePower = powerSample.getReal();
            
            // 3. 更新滑动窗口和功率总和
            // 减去窗口中最老的值
            this.currentPowerSum -= this.powerWindowBuffer[this.windowIndex];
            // 加上最新的值
            this.currentPowerSum += currentSamplePower;
            // 将最新的值存入窗口
            this.powerWindowBuffer[this.windowIndex] = currentSamplePower;
            // 更新窗口索引（环形）
            this.windowIndex = (this.windowIndex + 1) % this.WindowLength;

            // 4. 计算窗口内的平均功率
            double averagePower = this.currentPowerSum / this.WindowLength;

            // 5. 计算误差 (目标功率 - 当前平均功率)
            double error = this.TargetLevel - averagePower;

            // 6. 根据误差和步长更新增益
            this.currentGain += this.Step * error;

            // 7. (可选但推荐) 增加增益保护，防止增益变为负数或过大
            if (this.currentGain < 0) {
                this.currentGain = 0;
            }
        }
    }

    // ================== Getters/Setters & Property Modifier ==================
    
    @Override
    public Boolean ModifyPropertiesByName(String name, Object value) {
        try {
            switch (name) {
                case "TargetLevel": setTargetLevel(Double.parseDouble(value.toString())); return true;
                case "Step": setStep(Double.parseDouble(value.toString())); return true;
                case "WindowLength": setWindowLength(Integer.parseInt(value.toString())); return true;
                default: return super.ModifyPropertiesByName(name, value);
            }
        } catch (Exception e) {
            System.err.println("修改属性失败: " + name + ", 值: " + value + ". 错误: " + e.getMessage());
            return false;
        }
    }

    public void setTargetLevel(double targetLevel) { this.TargetLevel = targetLevel; }
    public void setStep(double step) { this.Step = step; }
    public void setWindowLength(int windowLength) {
        if (windowLength > 0 && this.WindowLength != windowLength) {
            this.WindowLength = windowLength;
            // 窗口长度变化，必须重置状态
            refreshComponent();
        }
    }
}