package com.example.sdr.Core.ProjectManager.Components.RF;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class VCOComponent extends BaseComponent {

    // 控制参数（默认值可调）
    private double CenterFrequency = 1000.0; // 中心频率 Hz
    private double SampleRate = 48000.0;     // 采样率 Hz
    private double Sensitivity = 100.0;      // 调制灵敏度 Hz/V
    private double Phase = 0.0;              // 当前相位

    public VCOComponent(int blockLength) {
        super(blockLength, 1, 1);
    }

    public VCOComponent(int blockLength, String ID) {
        super(blockLength, 1, 1, ID);
    }

    public VCOComponent(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, inputCount, outputCount,ID);
    }

    @Override
    public void setOperationParams(SDRData[] input, int index) {
        if(input.length != blockLength) {
            resetBlockLength(input.length);
        }
        this.op_in[index] = input;
    }

    public void setModulationInput(SDRData[] freqModInput) {
        setOperationParams(freqModInput, 0);
    }

    @Override
    public void Calculate() {


        for (int i = 0; i < blockLength; i++) {
            
            double freq = CenterFrequency + Sensitivity * op_in[0][i].getReal(); // 获取调制输入的实部作为频率偏移量
            Phase += 2 * Math.PI * freq / SampleRate;
            Phase = Phase % (2 * Math.PI);
            ans[0][i].fromDouble(Math.cos(Phase));
        }
    }


    public void setCenterFrequency(double fc) {
        this.CenterFrequency = fc;
    }

    public void setSampleRate(double fs) {
        this.SampleRate = fs;
    }

    public void setSensitivity(double Sensitivity) {
        this.Sensitivity = Sensitivity;
    }

    @Override
    public Boolean ModifyPropertiesByName(String name, Object value) {
        if (name.equals("CenterFrequency")) {
            this.CenterFrequency = Double.parseDouble(value.toString());
            return true;
        } else if (name.equals("SampleRate")) {
            this.SampleRate = Double.parseDouble(value.toString());
            return true;
        } else if (name.equals("Sensitivity")) {
            this.Sensitivity = Double.parseDouble(value.toString());
            return true;
        }
        return super.ModifyPropertiesByName(name, value);
    }
}
