package com.example.sdr.Core.ProjectManager.Components.DeModulation;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;
import com.example.sdr.Core.ProjectManager.Components.FIR.FIRLowPass;
import com.example.sdr.Core.ProjectManager.Components.RF.RPLL;

/**
 * DSB (Double-Sideband) Demodulator Component.
 *
 * <p>该模块通过集成一个锁相环（RPLL）和一个FIR低通滤波器（FIRLowPass）来实现DSB信号的相干解调。</p>
 *
 * <h3>工作流程:</h3>
 * <ol>
 * <li><b>载波恢复</b>: RPLL锁定输入信号的载波，生成一个同步的本地载波。</li>
 * <li><b>混频</b>: 将输入信号与恢复的载波相乘，将基带信号移回零频。</li>
 * <li><b>低通滤波</b>: FIRLowPass滤除混频产生的高频分量，提取出原始的基带信号。</li>
 * </ol>
 *
 * <p><b>注意:</b> 您所提供的 RPLL 组件配置能力有限。为了设置特定的载波频率和环路带宽，
 * 您可能需要修改 RPLL.java 类以添加公共的设置方法（例如 setCenterFrequency, setCutOffFrequency）。
 * 当前实现会使用 RPLL 的默认或其内部 refreshComponent() 方法计算的参数。</p>
 */
public class DSBDeModulator extends BaseComponent {

    // 内部子组件
    private final RPLL pll;
    private final FIRLowPass lpf;

    // LPF 可配置参数
    private int LpfCutOffFrequency;
    private int LpfWindowLength;
    private String LpfWindowType;

    // 用于存放混频后信号的中间缓冲
    private SDRData[] mixedSignalBuffer;

    /**
     * DSBDeModulator 的构造函数。
     *
     * @param blockLength 每个处理块的样本数。
     * @param inputCount  输入端口数量 (应为 1)。
     * @param outputCount 输出端口数量 (应为 1)。
     * @param ID          组件的唯一标识符。
     */
    public DSBDeModulator(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, inputCount, outputCount, ID);

        // 初始化内部的 PLL 和 LPF
        this.pll = new RPLL(blockLength, 1, 1, ID + "_PLL");
        this.lpf = new FIRLowPass(blockLength, 1, 1, ID + "_LPF");

        // 为可配置的 LPF 设置默认参数
        this.LpfCutOffFrequency = 500; // 消息信号的默认截止频率
        this.LpfWindowLength = 64;
        this.LpfWindowType = "Hamming";

        // 为混频信号分配中间缓冲区
        this.mixedSignalBuffer = SDRDataUtils.createComplexArray(blockLength, 0, 0);
    }

    /**
     * 使用当前参数配置内部的低通滤波器。
     * PLL的配置由其自己的 `refreshComponent` 方法处理。
     */
    private void configureLPF() {
        if (getSampleRate() > 0) {
            lpf.setSampleRate(getSampleRate());
            lpf.setFilterParams(this.LpfCutOffFrequency, this.LpfWindowLength, this.LpfWindowType);
        }
    }

    /**
     * 设置输出低通滤波器的参数。
     *
     * @param cutOffFrequency 滤波器的截止频率。
     * @param windowLength    FIR 滤波器的长度/阶数。
     * @param windowType      FIR 滤波器的窗函数类型 (例如 "Hamming")。
     */
    public void setLowPassFilterParams(int cutOffFrequency, int windowLength, String windowType) {
        this.LpfCutOffFrequency = cutOffFrequency;
        this.LpfWindowLength = windowLength;
        this.LpfWindowType = windowType;
        configureLPF();
    }
    
    /**
     * 暴露内部的 PLL 组件以进行直接配置。
     * 提供此方法是为了解决 RPLL 类公共 API 有限的问题。
     * @return 内部的 RPLL 实例。
     */
    public RPLL getPll() {
        return pll;
    }

    /**
     * 暴露内部的 LPF 组件以进行直接配置。
     * @return 内部的 FIRLowPass 实例。
     */
    public FIRLowPass getLpf() {
        return lpf;
    }

    @Override
    public void setSampleRate(int sampleRate) {
        super.setSampleRate(sampleRate);
        // 将采样率传递给子组件
        pll.setSampleRate(sampleRate);
        lpf.setSampleRate(sampleRate);
        // 在采样率已知后重新计算系数
        pll.refreshComponent();
        configureLPF();
    }

    @Override
    public void refreshComponent() {
        // 重置并重新配置子组件到它们的初始状态
        pll.refreshComponent();
        lpf.refreshComponent();
    }

    @Override
    public void resetBlockLength(int newBlockLength) {
        super.resetBlockLength(newBlockLength);
        // 将块长度的变化传递给子组件，并调整内部缓冲区大小
        if (pll != null) pll.resetBlockLength(newBlockLength);
        if (lpf != null) lpf.resetBlockLength(newBlockLength);
        this.mixedSignalBuffer = SDRDataUtils.createComplexArray(newBlockLength, 0, 0);
    }

    @Override
    public void setOperationParams(SDRData[] data, int index) {
        if (data.length != this.blockLength) {
            resetBlockLength(data.length);
        }
        this.op_in[index] = data;
    }

    /**
     * 对输入数据块执行DSB解调过程。
     */
    @Override
    public void Calculate() {
        if (op_in[0] == null) {
            // 没有输入数据需要处理
            return;
        }

        // --- 步骤 1: 载波恢复 (PLL) ---
        // 将输入信号传递给PLL并执行其计算。
        // PLL将尝试锁定载波并生成一个同步的本地振荡器信号。
        pll.setOperationParams(op_in[0], 0);
        pll.Calculate();
        SDRData[] recoveredCarrier = pll.getAns(0);

        // --- 步骤 2: 混频 ---
        // 通过将输入信号与恢复的载波相乘来执行相干解调。
        // 对于标准的DSB信号（实数），我们将其与载波的实部（余弦分量）相乘。
        for (int i = 0; i < blockLength; i++) {
            double mixedRealValue = op_in[0][i].getReal() * recoveredCarrier[i].getReal();
            mixedSignalBuffer[i].setReal(mixedRealValue);
            mixedSignalBuffer[i].setImag(0); // 此阶段的结果被视为实数信号
        }

        // --- 步骤 3: 低通滤波 ---
        // 将混频后的信号通过LPF，以移除高频（2*fc）分量。
        lpf.setOperationParams(mixedSignalBuffer, 0);
        lpf.Calculate();
        SDRData[] demodulatedSignal = lpf.getAns(0);

        // --- 步骤 4: 输出 ---
        // 将最终解调的信号复制到此组件的输出缓冲区。
        for (int i = 0; i < blockLength; i++) {
            ans[0][i].Copy(demodulatedSignal[i]);
        }
    }
}