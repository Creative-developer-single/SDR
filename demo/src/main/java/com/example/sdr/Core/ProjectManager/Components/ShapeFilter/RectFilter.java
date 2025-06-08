package com.example.sdr.Core.ProjectManager.Components.ShapeFilter;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

/**
 * 矩形脉冲成型滤波器
 * <p>
 * 这是最简单的脉冲成型方式。同样采用 Overlap-Save 算法实现连续数据流的卷积，
 * 保持了滤波器的记忆性，确保分块处理结果的正确性。
 * </p>
 */
public class RectFilter extends BaseComponent {

    // ================== 自定义属性 (首字母大写) ==================
    private double Rs;      // 符号速率 (Symbol Rate)
    private int Span;       // 滤波器长度（以符号周期为单位）

    // ================== 内部状态变量 ==================
    private SDRData[] filterTaps;      // 存储滤波器系数
    private SDRData[] overlapBuffer;   // 用于 Overlap-Save 算法的重叠区域缓存

    public RectFilter(int blockLength, int inputCount, int outputCount, String ID) {
        // 成型滤波器是1输入，1输出的组件
        super(blockLength, inputCount, outputCount, ID);
        this.Rs = 1000.0; // 默认符号速率为1kbps
        this.Span = 4;    // 默认滤波器长度为4个符号周期

        // 初始化时，生成滤波器并重置状态
        this.refreshComponent();
    }

    // ================== 构造函数 ==================
    public RectFilter(int blockLength, int sampleRate, double rs, int span, String ID) {
        // 成型滤波器是1输入，1输出的组件
        super(blockLength, 1, 1, ID);
        this.SampleRate = sampleRate;
        this.Rs = rs;
        this.Span = span;

        // 初始化时，生成滤波器并重置状态
        this.refreshComponent();
    }

    // ================== 核心方法 ==================

    /**
     * 重载此方法，用于重新生成滤波器系数和重置状态
     */
    @Override
    public void refreshComponent() {
        generateFilterTaps();
        // 初始化重叠缓存区，长度为 N-1，其中 N 是滤波器长度
        this.overlapBuffer = SDRDataUtils.createComplexArray(this.filterTaps.length - 1, 0, 0);
    }

    @Override
    public void setOperationParams(SDRData[] data, int index) {
        // 自适应输入速率
        if (data.length != this.blockLength) {
            resetBlockLength(data.length);
        }
        // 设置输入数据
        this.op_in[index] = data;
    }

    /**
     * 核心计算方法，采用 Overlap-Save 算法实现卷积
     * 该方法与 RRCFilter 中的实现完全相同，体现了架构的复用性。
     */
    @Override
    public void Calculate() {
        int N = this.filterTaps.length; // 滤波器长度
        int M = this.blockLength;       // 输入块长度
        int overlapLength = N - 1;

        if (op_in[0] == null || overlapLength < 0) return; // 未设置输入数据或滤波器未初始化

        // 1. 构造用于卷积的扩展输入块: [overlap_buffer, current_input_block]
        SDRData[] extendedInput = SDRDataUtils.createComplexArray(overlapLength + M, 0, 0);
        System.arraycopy(this.overlapBuffer, 0, extendedInput, 0, overlapLength);
        System.arraycopy(this.op_in[0], 0, extendedInput, overlapLength, M);

        // 2. 执行线性卷积
        SDRData[] convolutionResult = convolve(extendedInput, this.filterTaps);

        // 3. 提取有效部分 (Overlap-Save 的核心)
        System.arraycopy(convolutionResult, overlapLength, this.ans[0], 0, M);

        // 4. 更新下一次计算所需的重叠缓存区
        System.arraycopy(this.op_in[0], M - overlapLength, this.overlapBuffer, 0, overlapLength);
    }
    
    // ================== 辅助方法 ==================

    /**
     * 生成滤波器系数
     * 这是与 RRCFilter 类的核心区别。
     */
    private void generateFilterTaps() {
        double sps = (double) this.SampleRate / this.Rs; // Samples per Symbol
        int numTaps = (int)Math.round(sps * this.Span);
        
        // 防止计算出0长度的滤波器
        if (numTaps == 0) {
            numTaps = 1;
        }

        this.filterTaps = SDRDataUtils.createComplexArray(numTaps, 0, 0);

        // 对于矩形脉冲，所有系数都相同。
        // 为了使滤波器在直流（DC）处的增益为1（即不改变信号的平均电平），
        // 所有系数的和应为1。因此，每个系数的值是 1.0 / N。
        double tapValue = 1.0 / numTaps;

        for (int i = 0; i < numTaps; i++) {
            this.filterTaps[i].setReal(tapValue);
        }
    }
    
    /**
     * 线性卷积函数 (与 RRCFilter 中的完全相同)
     */
    private SDRData[] convolve(SDRData[] signal, SDRData[] kernel) {
        int signalLen = signal.length;
        int kernelLen = kernel.length;
        int resultLen = signalLen + kernelLen - 1;
        SDRData[] result = SDRDataUtils.createComplexArray(resultLen, 0, 0);

        for (int i = 0; i < resultLen; i++) {
            result[i].setReal(0);
            result[i].setImag(0);
            int kmin = (i >= kernelLen - 1) ? i - (kernelLen - 1) : 0;
            int kmax = (i < signalLen - 1) ? i : signalLen - 1;
            
            for (int k = kmin; k <= kmax; k++) {
                SDRData sigVal = signal[k];
                SDRData kerVal = kernel[i - k];
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
                default: return super.ModifyPropertiesByName(name, value);
            }
        } catch (Exception e) {
            System.err.println("修改属性失败: " + name + ", 值: " + value + ". 错误: " + e.getMessage());
            return false;
        }
    }

    public void setRs(double rs) { this.Rs = rs; refreshComponent(); }
    public void setSpan(int span) { this.Span = span; refreshComponent(); }
}