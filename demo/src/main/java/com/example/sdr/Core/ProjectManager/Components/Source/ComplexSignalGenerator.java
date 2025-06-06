package com.example.sdr.Core.ProjectManager.Components.Source;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class ComplexSignalGenerator extends BaseComponent {
    //定义参数
    //块长度
    private int blockLength;

    // 定义本地参数
    // 频率
    private double frequency;
    // 幅度
    private double amplitude;
    // 记忆相位
    private double blockPhase;

    // 信号类型
    private String signalType;

    // 定义符合基类类型的构造函数
    public ComplexSignalGenerator(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, inputCount, outputCount, ID);
        this.blockLength = blockLength;
        this.blockPhase = 0;
        this.signalType = "Sine";

        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
    }
   
    // 设置信号类型
    public void setSignalType(String signalType){
        if (signalType == null || signalType.isEmpty()) {
            throw new IllegalArgumentException("Signal type cannot be null or empty");
        }
        this.signalType = signalType;
    }

    // 设置信号频率
    public void setFrequency(double frequency) {
        if (frequency < 0) {
            throw new IllegalArgumentException("Frequency cannot be negative");
        }
        this.frequency = frequency;
    }

    // 设置信号幅度
    public void setAmplitude(double amplitude) {
        if (amplitude < 0) {
            throw new IllegalArgumentException("Amplitude cannot be negative");
        }
        this.amplitude = amplitude;
    }

    // 设置信号相位
    public void resetBlockPhase(double blockPhase){
        if (blockPhase < 0 || blockPhase >= 360) {
            throw new IllegalArgumentException("Block phase must be in the range [0, 360)");
        }
        this.blockPhase = blockPhase;
    }

    // 重载计算函数
    public void Calculate() {
        // 生成信号
        GeneralSignal();
    }

    // 生成信号
    public void GeneralSignal(){
        if(signalType.equals("Sine")){
            for (int i=0; i < blockLength ;i++){
                this.blockPhase += (2 * Math.PI * frequency / SampleRate);
                ans[0][i].setReal(amplitude * Math.cos(blockPhase));
                ans[0][i].setImag(amplitude * Math.sin(blockPhase));
            }
        }
    }
}
