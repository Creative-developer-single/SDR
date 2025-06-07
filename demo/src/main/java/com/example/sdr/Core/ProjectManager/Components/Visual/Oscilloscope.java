package com.example.sdr.Core.ProjectManager.Components.Visual;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class Oscilloscope extends BaseComponent {
    private int bufferLength;
    private String DataType; // "Real" or "Imag"

    public Oscilloscope(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, inputCount, outputCount, ID);
        this.bufferLength = blockLength;
        this.DataType = "Real"; // Default to Real data type
    }

    public void Calculate(){
        // Do nothing
    }

    public void refreshComponent(){
        ans[0] = SDRDataUtils.createComplexArray(bufferLength, 0, 0);
    }

    public void setOperationParams(SDRData[] data,int index){
        for (int i = 0; i < data.length; i++){
            if (i == bufferLength) {
                break; // Prevent overflow if data exceeds buffer length
            }
            if (DataType.equals("Real")) {
                ans[0][i].setReal(data[i].getReal());
                ans[0][i].setImag(0);
            } else if (DataType.equals("Imag")) {
                ans[0][i].setReal(0); // Set real part to 0 for Imaginary data
                ans[0][i].setImag(data[i].getImag());
            } else {
                throw new IllegalArgumentException("Invalid data type. Use 'Real' or 'Imag'.");
            }
        }
    }
}
