package com.example.sdr.Core.ProjectManager.Components.ShapeFilter;


import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

/**
 * 升余弦/平方根升余弦 成型滤波器
 * <p>
 * 该滤波器用于脉冲成型，可以有效抑制码间串扰。
 * 采用 Overlap-Save 算法实现连续数据流的卷积，保持了滤波器的记忆性，
 * 使得分块处理的结果与对整个信号流进行处理的结果一致。
 * </p>
 */
public class RRCFilter extends BaseComponent {

    // ================== 自定义属性 (首字母大写) ==================
    private double Rs;      // 符号速率 (Symbol Rate)
    private int Span;       // 滤波器长度（以符号周期为单位）
    private double Alpha;   // 滚降因子 (Roll-off factor)
    private String Mode;    // 滤波器模式: "RRC" 或 "RC"

    // ================== 内部状态变量 ==================
    private SDRData[] filterTaps;      // 存储滤波器系数
    private SDRData[] overlapBuffer;   // 用于 Overlap-Save 算法的重叠区域缓存

    // ================== 构造函数 ==================
    public RRCFilter(int blockLength,int inputCount,int outputCount,String ID) {
        // 成型滤波器是1输入，1输出的组件
        super(blockLength, inputCount, outputCount, ID);
        this.Rs = 1000.0; // 默认符号速率为1kbps
        this.Span = 4;    // 默认滤波器长度为4个符号周期
        this.Alpha = 0.35; // 默认滚降因子为0.35
        this.Mode = "RRC"; // 默认模式为 RRC

        // 初始化时，生成滤波器并重置状态
        this.refreshComponent();
    }

    public RRCFilter(int blockLength, int sampleRate, double rs, int span, double alpha, String mode, String ID) {
        // 成型滤波器是1输入，1输出的组件
        super(blockLength, 1, 1, ID);
        this.SampleRate = sampleRate;
        this.Rs = rs;
        this.Span = span;
        this.Alpha = alpha;
        this.Mode = mode;

        // 初始化时，生成滤波器并重置状态
        this.refreshComponent();
    }

    // ================== 核心方法 ==================

    /**
     * 重载此方法，用于重新生成滤波器系数和重置状态
     * 当任何滤波器参数改变时，都应调用此方法。
     */
    @Override
    public void refreshComponent() {
        generateFilterTaps();
        // 初始化重叠缓存区，长度为 N-1，其中 N 是滤波器长度
        this.overlapBuffer = SDRDataUtils.createComplexArray(this.filterTaps.length - 1, 0, 0);
        for (int i = 0; i < this.overlapBuffer.length; i++) {
            this.overlapBuffer[i].setReal(0);
            this.overlapBuffer[i].setImag(0);
        }
    }

    /**
     * 核心计算方法，采用 Overlap-Save 算法实现卷积
     */
    @Override
    public void Calculate() {
        int N = this.filterTaps.length; // 滤波器长度
        int M = this.blockLength;       // 输入块长度
        int overlapLength = N - 1;

        if (op_in[0] == null) return; // 未设置输入数据

        // 1. 构造用于卷积的扩展输入块: [overlap_buffer, current_input_block]
        SDRData[] extendedInput = SDRDataUtils.createComplexArray(overlapLength+M, 0, 0);
        System.arraycopy(this.overlapBuffer, 0, extendedInput, 0, overlapLength);
        System.arraycopy(this.op_in[0], 0, extendedInput, overlapLength, M);

        // 2. 执行线性卷积
        SDRData[] convolutionResult = convolve(extendedInput, this.filterTaps);

        // 3. 提取有效部分 (Overlap-Save 的核心)
        // 抛弃前 N-1 个点（这些是因与上一个块的重叠数据卷积产生的瞬态/混叠部分）
        // 提取中间的 M (blockLength) 个点作为本次计算的有效输出
        System.arraycopy(convolutionResult, overlapLength, this.ans[0], 0, M);

        // 4. 更新下一次计算所需的重叠缓存区
        // 保存当前输入块的最后 N-1 个点
        System.arraycopy(this.op_in[0], M - overlapLength, this.overlapBuffer, 0, overlapLength);
    }
    
    // ================== 辅助方法 ==================

    /**
     * 生成滤波器系数
     */
    private void generateFilterTaps() {
        double sps = (double) this.SampleRate / this.Rs; // Samples per Symbol
        // 滤波器总长度 N = sps * span + 1
        // 为保证滤波器中心对称，总点数应为奇数
        int numTaps = (int)Math.round(sps * this.Span) + 1;
        if (numTaps % 2 == 0) {
            numTaps++; // 确保为奇数
        }
        this.filterTaps = SDRDataUtils.createComplexArray(numTaps, 0, 0);
        double Ts = 1.0 / this.Rs; // 符号周期

        for (int i = 0; i < numTaps; i++) {
            // 计算每个采样点相对于滤波器中心的时间 t
            double t = (i - numTaps / 2.0) / this.SampleRate;
            this.filterTaps[i].setReal(calculateTapValue(t, Ts, this.Alpha, this.Mode));
            this.filterTaps[i].setImag(0); // 仅实部有效
        }
    }

    /**
     * 根据模式计算单个滤波器系数的值
     */
    private double calculateTapValue(double t, double Ts, double alpha, String mode) {
        // 处理 t=0 的特殊情况，防止除零
        if (t == 0.0) {
            return mode.equalsIgnoreCase("RRC") ? 
                (1.0 / Ts) * (1.0 + alpha * (4.0 / Math.PI - 1.0)) : 
                1.0;
        }

        if (mode.equalsIgnoreCase("RRC")) {
            // RRC 滤波器系数公式
            double term1 = Math.sin(Math.PI * t / Ts * (1 - alpha));
            double term2 = 4 * alpha * t / Ts * Math.cos(Math.PI * t / Ts * (1 + alpha));
            double denominator = Math.PI * t / Ts * (1 - Math.pow(4 * alpha * t / Ts, 2));
            // 处理分母为0的特殊情况
            if (Math.abs(denominator) < 1e-9) {
                // 这是 t = +/- Ts / (4*alpha) 的情况
                double val = (alpha / (Ts * Math.sqrt(2))) *
                        ((1 + 2 / Math.PI) * Math.sin(Math.PI / (4 * alpha)) +
                         (1 - 2 / Math.PI) * Math.cos(Math.PI / (4 * alpha)));
                return val;
            }
            return (term1 + term2) / denominator;
        } else {
            // RC 滤波器系数公式
            double term_sinc = Math.sin(Math.PI * t / Ts) / (Math.PI * t / Ts);
            double term_cos = Math.cos(Math.PI * alpha * t / Ts) / (1 - Math.pow(2 * alpha * t / Ts, 2));
            return term_sinc * term_cos;
        }
    }
    
    /**
     * 线性卷积函数
     * @param signal 输入信号 (长度 M)
     * @param kernel 卷积核/滤波器 (长度 N)
     * @return 卷积结果 (长度 M+N-1)
     */
    private SDRData[] convolve(SDRData[] signal, SDRData[] kernel) {
        int signalLen = signal.length;
        int kernelLen = kernel.length;
        int resultLen = signalLen + kernelLen - 1;
        SDRData[] result = SDRDataUtils.createComplexArray(resultLen, 0, 0);

        for (int i = 0; i < resultLen; i++) {
            result[i].setReal(0); // 初始化为0
            result[i].setImag(0); // 初始化为0
            int kmin = (i >= kernelLen - 1) ? i - (kernelLen - 1) : 0;
            int kmax = (i < signalLen - 1) ? i : signalLen - 1;
            
            for (int k = kmin; k <= kmax; k++) {
                SDRData sigVal = signal[k];
                SDRData kerVal = kernel[i - k];
                // SDRData.multiply 返回新对象，我们需要就地计算
                SDRData temp = sigVal.getCopy();
                temp.multiply(kerVal);
                result[i].add(temp);
            }
        }
        return result;
    }

    // ================== Getters/Setters & Property Modifier ==================

    @Override
    public Boolean ModifyPropertiesByName(String name, Object value) {
        try {
            switch (name) {
                case "Rs": setRs(Double.parseDouble(value.toString())); return true;
                case "Span": setSpan(Integer.parseInt(value.toString())); return true;
                case "Alpha": setAlpha(Double.parseDouble(value.toString())); return true;
                case "Mode": setMode(value.toString()); return true;
                default: return super.ModifyPropertiesByName(name, value);
            }
        } catch (Exception e) {
            System.err.println("修改属性失败: " + name + ", 值: " + value + ". 错误: " + e.getMessage());
            return false;
        }
    }

    public void setRs(double rs) { this.Rs = rs; refreshComponent(); }
    public void setSpan(int span) { this.Span = span; refreshComponent(); }
    public void setAlpha(double alpha) { this.Alpha = alpha; refreshComponent(); }
    public void setMode(String mode) { this.Mode = mode; refreshComponent(); }
}