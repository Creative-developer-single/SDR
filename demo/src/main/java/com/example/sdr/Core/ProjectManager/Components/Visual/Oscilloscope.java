package com.example.sdr.Core.ProjectManager.Components.Visual;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class Oscilloscope extends BaseComponent {
    private int bufferLength;
    private String dataType; // "Real" or "Imag"

    public Oscilloscope(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, inputCount, outputCount, ID);
        this.bufferLength = blockLength;
        this.dataType = "Real"; // Default to Real data type
    }

    public void Calculate(){
        // Do nothing
    }

    public void setOperationParams(SDRData[] data,int index){
        if (ans[0].length != bufferLength) {
            resetBlockLength(bufferLength);
        }
        for (int i = 0; i < data.length; i++){
            if (i == bufferLength) {
                break; // Prevent overflow if data exceeds buffer length
            }
            if (dataType.equals("Real")) {
                ans[0][i].setReal(data[i].getReal());
                ans[0][i].setImag(0);
            } else if (dataType.equals("Imag")) {
                ans[0][i].setReal(0); // Set real part to 0 for Imaginary data
                ans[0][i].setImag(data[i].getImag());
            } else {
                throw new IllegalArgumentException("Invalid data type. Use 'Real' or 'Imag'.");
            }
        }
        System.out.println("a sample: "+ans[0][0].getReal() + " + " + ans[0][0].getImag() + "i");
    }
}
