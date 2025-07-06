package com.example.sdr.Core.ProjectManager.Components.Modulation;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class DSBSCModulator extends BaseComponent{
    private int CarrierFrequency;
    private int PilotPower;
    private double BlockPhase;

    public DSBSCModulator(int block, int inputCount, int outputCount, String ID) {
        super(block, inputCount, outputCount, ID);
        this.CarrierFrequency = 1000; // Default carrier frequency
        this.PilotPower = 1; // Default pilot power
        this.BlockPhase = 0; // Default phase
    }

    @Override
    public void refreshComponent() {
        // Refresh logic for DSBSCModulator
        // This could include resetting internal states or parameters if needed
        this.BlockPhase = 0; // Reset phase for new calculations
    }

    @Override
    public void setOperationParams(SDRData[] data, int index) {
        // 自适应速率
        if (data.length != blockLength) {
            resetBlockLength(data.length);
        }
        this.op_in[index] = data;
    }

    @Override
    public void Calculate() {
        // Implement DSBSC modulation logic here
        // This is a placeholder for the actual modulation algorithm
        for (int i = 0; i < blockLength; i++) {
            // Example modulation logic
            double carrierSignal = Math.cos(BlockPhase);
            ans[0][i].setReal(carrierSignal * (PilotPower + op_in[0][i].getReal()));
            ans[0][i].setImag(0); // Assuming DSBSC modulation has no imaginary part

            BlockPhase += 2 * Math.PI * CarrierFrequency / SampleRate;
        }
    }
}
