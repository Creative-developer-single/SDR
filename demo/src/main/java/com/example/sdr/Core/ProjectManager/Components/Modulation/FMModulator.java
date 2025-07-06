package com.example.sdr.Core.ProjectManager.Components.Modulation;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;
import com.example.sdr.Core.ProjectManager.Components.RF.VCOComponent;

public class FMModulator extends BaseComponent{
    private int CarrierFrequency;
    private int FMModulationParams;
    private VCOComponent vco;

    public FMModulator(int block, int inputCount, int outputCount, String ID) {
        super(block, inputCount, outputCount, ID);
        this.CarrierFrequency = 1000; // Default carrier frequency
        this.FMModulationParams = 1; // Default FM modulation parameters
        this.vco = new VCOComponent(CarrierFrequency, FMModulationParams, SampleRate, 0);
    }

    @Override
    public void refreshComponent() {
        // Refresh logic for FMModulator
        // This could include resetting internal states or parameters if needed
        this.vco = new VCOComponent(CarrierFrequency, FMModulationParams, SampleRate, 0);
        this.vco.resetBlockLength(blockLength);
        this.vco.refreshComponent();
    }

    @Override
    public void setOperationParams(SDRData[] data, int index) {
        // 自适应速率
        if (data.length != blockLength) {
            resetBlockLength(data.length);
            this.vco.resetBlockLength(data.length);
        }
        this.op_in[index] = data;
    }

    @Override
    public void Calculate() {
        // Implement FM modulation logic here
        // This is a placeholder for the actual modulation algorithm
        // 将信号输入VCO
        vco.setOperationParams(op_in[0], 0);
        vco.Calculate();

        ans[0] = vco.getAns(0);
    }
}
