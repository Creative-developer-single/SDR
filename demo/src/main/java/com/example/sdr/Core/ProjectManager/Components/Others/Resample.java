package com.example.sdr.Core.ProjectManager.Components.Others;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;
import com.example.sdr.Core.ProjectManager.Components.FIR.FIRLowPass;

/**
 * Resample 组件 (完整修正版)
 * <p>
 * 用于将输入信号从 SourceSampleRate 变换到 TargetSampleRate。
 * 内部使用 FIRLowPass 作为抗混叠滤波器，时间驱动插值实现。
 * 保证高质量重采样，支持非整数倍采样率变换。
 * </p>
 */
public class Resample extends BaseComponent {
    public int inputBlockLength;
    private FIRLowPass antiAliasFilter;
    private double phaseAccumulator = 0.0;
    private double phaseIncrement = 0.0;
    private SDRData[] workingBuffer;
    private SDRData lastFilteredSample;
    private double residualPhase = 0.0;

    public Resample(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, inputCount, outputCount, ID);
        this.Type = "SampleRateConverter";
        this.SourceSampleRate = 1000;
        this.TargetSampleRate = 1000;
        this.antiAliasFilter = new FIRLowPass(blockLength, 1, 1, ID + "_FIR");
        this.lastFilteredSample = new SDRData(0, 0);
        this.workingBuffer = SDRDataUtils.createComplexArray(blockLength + 1, 0, 0);
        this.inputBlockLength = blockLength;
        this.refreshComponent();
    }

    @Override
    public void refreshComponent() {
        // [核心修正] 相位步进的定义是 Source / Target
        // 为避免整数除法，强制转换为double
        this.phaseIncrement = (double)this.SourceSampleRate / (double)this.TargetSampleRate;

        // 重新配置抗混叠滤波器，截止频率应取二者中的较低者，以防混叠或过度滤波
        int cutoff = (int) (0.45 * Math.min(this.SourceSampleRate, this.TargetSampleRate));
        int windowLength = 64;
        this.antiAliasFilter.setFilterParams(cutoff, windowLength, "Hamming");
        // 注意：抗混叠滤波器工作在输入信号的采样率上
        this.antiAliasFilter.setSampleRate((int) this.SourceSampleRate);
        this.antiAliasFilter.refreshComponent();

        // 重置所有状态
        this.residualPhase = 0.0;
        this.phaseAccumulator = 0.0;
        this.lastFilteredSample.setReal(0);
        this.lastFilteredSample.setImag(0);

        // 根据变换比例，估算输出缓冲区大小
        int estimatedOutputLength = (int) Math.ceil(this.inputBlockLength / this.phaseIncrement) + 2;
        this.ans = SDRDataUtils.createComplexMatrix(outputCount, estimatedOutputLength, 0, 0);

        if (this.workingBuffer.length != this.inputBlockLength + 1) {
            this.workingBuffer = SDRDataUtils.createComplexArray(this.inputBlockLength + 1, 0, 0);
        }
    }

    @Override
    public void setOperationParams(SDRData[] data, int index) {
        if (data.length != inputBlockLength) {
            inputBlockLength = data.length;
            op_in = SDRDataUtils.createComplexMatrix(inputCount, inputBlockLength, 0, 0);
            refreshComponent();
        }
        op_in[index] = data;
    }

    @Override
    public void Calculate() {
        // 1️⃣ 抗混叠滤波
        this.antiAliasFilter.setOperationParams(op_in[0], 0);
        this.antiAliasFilter.Calculate();
        SDRData[] filteredData = this.antiAliasFilter.getAns(0);

        // 2️⃣ 准备用于插值的工作缓冲区，处理边界问题
        this.workingBuffer[0].Copy(this.lastFilteredSample);
        System.arraycopy(filteredData, 0, this.workingBuffer, 1, inputBlockLength);
        this.lastFilteredSample.Copy(filteredData[inputBlockLength - 1]);

        // 3️⃣ 时间驱动插值
        int outputIndex = 0;
        this.phaseAccumulator = this.residualPhase;

        // 注意：这里的循环变量 t, idx, frac 都是相对于输入样本序列的
        while (true) {
            // phaseAccumulator 是我们在输入序列时间轴上的"指针"
            double t = this.phaseAccumulator;

            // 如果指针超出了当前块能提供的范围（我们有 inputBlockLength 个新样本），则停止
            if (t >= inputBlockLength) {
                break;
            }
            if (outputIndex >= this.ans[0].length) {
                break;
            }

            int idx = (int) Math.floor(t);
            double frac = t - idx;

            SDRData left = this.workingBuffer[idx];
            SDRData right = this.workingBuffer[idx + 1];

            double real = (1.0 - frac) * left.getReal() + frac * right.getReal();
            double imag = (1.0 - frac) * left.getImag() + frac * right.getImag();

            this.ans[0][outputIndex].setReal(real);
            this.ans[0][outputIndex].setImag(imag);
            this.ans[0][outputIndex].setComputeMode(SDRData.ComputeMode.COMPLEX);

            // 每生成一个输出点，将指针向前移动一个步进
            this.phaseAccumulator += this.phaseIncrement;
            outputIndex++;
        }

        // 4️⃣ 计算剩余相位，用于下一个块
        this.residualPhase = this.phaseAccumulator - this.inputBlockLength;

        // 5️⃣ 清理输出缓冲区尾部
        for (int i = outputIndex; i < this.ans[0].length; i++) {
            this.ans[0][i].setReal(0);
            this.ans[0][i].setImag(0);
            this.ans[0][i].setComputeMode(SDRData.ComputeMode.COMPLEX);
        }
    }

    // Getters / Setters (保持不变)
    public double getSourceSampleRate() { return SourceSampleRate; }
    public void setSourceSampleRate(int sourceSampleRate) {
        SourceSampleRate = sourceSampleRate;
        refreshComponent();
    }
    public double getTargetSamplerate() { return TargetSampleRate; }
    public void setTargetSampleRate(int targetSampleRate) {
        TargetSampleRate = targetSampleRate;
        refreshComponent();
    }
}