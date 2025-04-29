package com.example.sdr.Core.ProjectManager.Components.RF;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class VCOComponent extends BaseComponent {

    // 控制参数（默认值可调）
    private double centerFrequency = 1000.0; // 中心频率 Hz
    private double sampleRate = 48000.0;     // 采样率 Hz
    private double sensitivity = 100.0;      // 调制灵敏度 Hz/V
    private double phase = 0.0;              // 当前相位

    public VCOComponent(int blockLength) {
        super(blockLength, 1, 1);
    }

    public VCOComponent(int blockLength, String ID) {
        super(blockLength, 1, 1, ID);
    }

    public VCOComponent(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, inputCount, outputCount,ID);
    }

    public void setOperationParams(double[] input, int index) {
        if (input.length != blockLength) {
            throw new IllegalArgumentException("Invalid block length");
        }
        this.op_in[index] = input;
    }

    public void setModulationInput(double[] freqModInput) {
        setOperationParams(freqModInput, 0);
    }

    @Override
    public void Calculate() {
        double[] input = op_in[0];
        double[] output = ans[0];

        for (int i = 0; i < blockLength; i++) {
            double freq = centerFrequency + sensitivity * input[i];
            phase += 2 * Math.PI * freq / sampleRate;
            phase = phase % (2 * Math.PI);
            output[i] = Math.cos(phase);
        }
    }

    public double[] getValue(int index) {
        return ans[index];
    }

    public void setCenterFrequency(double fc) {
        this.centerFrequency = fc;
    }

    public void setSampleRate(double fs) {
        this.sampleRate = fs;
    }

    public void setSensitivity(double sensitivity) {
        this.sensitivity = sensitivity;
    }

    @Override
    public Boolean ModifyPropertiesByName(String name, Object value) {
        if (name.equals("centerFrequency")) {
            this.centerFrequency = Double.parseDouble(value.toString());
            return true;
        } else if (name.equals("sampleRate")) {
            this.sampleRate = Double.parseDouble(value.toString());
            return true;
        } else if (name.equals("sensitivity")) {
            this.sensitivity = Double.parseDouble(value.toString());
            return true;
        }
        return super.ModifyPropertiesByName(name, value);
    }
}
