package com.example.sdr.Core.ProjectManager.Components.Modulation;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class AMModulator extends BaseComponent{
    private int CarrierFrequency;
    private double ModulationIndex;
    private double BlockPhase;

    public AMModulator(int block, int inputCount, int outputCount, String ID) {
        super(block, inputCount, outputCount, ID);
        this.CarrierFrequency = 1000; // Default carrier frequency
        this.ModulationIndex = 1; // Default modulation index
        this.BlockPhase = 0; // Default phase
    }

    @Override
    public void refreshComponent() {
        // Refresh logic for AMModulator
        // This could include resetting internal states or parameters if needed
        BlockPhase = 0; // Reset phase for new calculations
    }

    @Override
    public void setOperationParams(SDRData[] data,int index){
        // 自适应速率
        if(data.length != blockLength) {
            resetBlockLength(data.length);
        }
        this.op_in[index] = data;
    }

    @Override
    public void Calculate() {
        // Implement AM modulation logic here
        // This is a placeholder for the actual modulation algorithm
        for (int i = 0; i < blockLength; i++) {
            // create Carrier Signal
            double carrierSignal = Math.cos(BlockPhase);
            ans[0][i].setReal(carrierSignal * (1 + ModulationIndex * op_in[0][i].getReal()));
            ans[0][i].setImag(0); // Assuming AM modulation has no imaginary part

            BlockPhase += 2 * Math.PI * CarrierFrequency / SampleRate;
        }

    }
}
