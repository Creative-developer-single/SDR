package com.example.sdr.Core.ProjectManager.Components.RF;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class RPLL extends BaseComponent {
    // --- [成员变量定义，与之前相同] ---
    // 用户可配置参数
    private int CenterFrequency;
    private int CutOffFrequency;
    private int VCOGain;
    private int DesiredLockedFrequency;

    // 内部状态变量
    private double blockPhase;          // VCO的当前相位 (必须在块之间保持)
    private double integrator;          // PI控制器 - 积分器状态 (必须在块之间保持)
    
    private double paramA;              // PI控制器 - 比例增益 (Kp)
    private double paramB;              // PI控制器 - 积分增益 (Ki)

    public RPLL(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, inputCount, outputCount, ID);
        this.CenterFrequency = 1000;
        this.CutOffFrequency = 100;
        this.VCOGain = 1; 
        this.DesiredLockedFrequency = 1000;
        
        // 初始化状态
        reset();
        
        updateFilterParams();
    }
    
    /**
     * 主动重置锁相环的内部状态。
     * 当需要开始一段新的独立锁定时调用此方法。
     */
    public void reset() {
        this.blockPhase = 0.0;
        this.integrator = 0.0;
    }

    private void updateFilterParams() {
        // --- [代码与之前相同] ---
        if (this.SampleRate == 0 || this.VCOGain == 0) {
            this.paramA = 0;
            this.paramB = 0;
            return;
        }
        double damping = 0.707; 
        double omega_n = 2 * Math.PI * this.CutOffFrequency / this.SampleRate;
        this.paramA = (2.0 * damping * omega_n) / this.VCOGain;
        this.paramB = (omega_n * omega_n) / this.VCOGain;
    }

    @Override
    public void refreshComponent() {
        // --- [代码与之前相同] ---
        int freqDifference = (int)(2*Math.abs(DesiredLockedFrequency - CenterFrequency));
        this.CutOffFrequency = (int) (0.1 * freqDifference);
        this.VCOGain = 1;
        updateFilterParams();
    }

    @Override
    public void setOperationParams(SDRData[] data,int index){
        // --- [代码与之前相同] ---
        if (data.length != blockLength) {
            resetBlockLength(data.length);
        }
        this.op_in[index] = data;
    }
    
    @Override
    public void Calculate() {
        // --- [计算逻辑与之前完全相同，因为它已经是正确的状态更新逻辑] ---
        SDRData vco_conjugate = new SDRData(0, 0);
        SDRData mixed_signal = new SDRData(0, 0);

        for (int i = 0; i < blockLength; i++) {
            // 1. 鉴相器
            vco_conjugate.setReal(Math.cos(this.blockPhase));
            vco_conjugate.setImag(-Math.sin(this.blockPhase));
            mixed_signal.Copy(op_in[0][i]);
            mixed_signal.multiply(vco_conjugate);
            double phase_detector_output = mixed_signal.getImag();

            // 2. 环路滤波器 (PI Controller)
            double proportional = this.paramA * phase_detector_output;
            this.integrator += this.paramB * phase_detector_output;
            double controller_output = proportional + this.integrator;

            // 3. 压控振荡器 (VCO)
            double free_running_increment = 2 * Math.PI * this.CenterFrequency / this.SampleRate;
            double controlled_increment = controller_output * this.VCOGain;
            this.blockPhase += free_running_increment + controlled_increment;
            this.blockPhase %= (2 * Math.PI);

            // 4. 存储输出
            ans[0][i].setReal(Math.cos(this.blockPhase));
            ans[0][i].setImag(Math.sin(this.blockPhase));
            ans[0][i].setComputeMode(SDRData.ComputeMode.COMPLEX);
        }
    }
    
    // --- [兼容性方法，与之前相同] ---
    public double VCO(SDRData input) {
        return Math.cos(this.blockPhase);
    }
    public double LowPassFilter(double pd_current) {
        return pd_current;
    }
}