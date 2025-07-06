package com.example.sdr.Core.ProjectManager.Components.Arithmetic;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class Calculus extends BaseComponent{
    private int blockLength;
    
    final int MODE_DIFFERENTIAL = 0;
    final int MODE_INTEGRATION = 1;

    private String mode;//Diff Inte

    public Calculus(int blockLength,int inputCount,int outputCount){
        super(blockLength,inputCount,outputCount); //
        this.inputCount = 1;
        this.blockLength = blockLength;
        this.mode = "Inte";

        op_in = SDRDataUtils.createComplexMatrix(inputCount, blockLength, 0, 0);
        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
    }

    public Calculus(int blockLength,int inputCount,int outputCount,String ID){
        super(blockLength, 1,outputCount,ID);
        this.inputCount = 1;
        this.blockLength = blockLength;
        this.mode = "Diff";

        op_in = SDRDataUtils.createComplexMatrix(inputCount, blockLength, 0, 0);
        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
    }

    @Override
    public void refreshComponent() {
        // Reset output array length to blockLength
        if (ans[0] == null || ans[0].length != blockLength) {
            resetBlockLength(blockLength);
        }
    }

    @Override
    public void setOperationParams(SDRData[] data, int index) {
        // 自适应速率
        if(data.length != blockLength) {
            resetBlockLength(data.length);
        }
        this.op_in[index] = data;
    }

    public void setOperationMode(String mode){
        this.mode = mode;
    }

    public void setInputData(SDRData[] data){
        if(data.length != inputCount){
            throw new IllegalArgumentException("Invalid input count");
        }else{
            op_in[0] = data;
        }
    }

    public void Calculate(){
        SDRData tmp = new SDRData(0, 0);
        
        if(mode == "Diff"){
            for(int i = 1; i < blockLength; i++){
                tmp.Copy(op_in[0][i]);
                tmp.subtract(op_in[0][i - 1]);
                ans[currentOutputIndex][i].Copy(tmp);
            }
            ans[currentOutputIndex][0] = new SDRData(0, 0); // First element is set to zero
        }else if(mode == "Inte"){
            ans[currentOutputIndex][0] = new SDRData(0, 0);
            tmp.Copy(ans[currentInputIndex][0]);
            for(int i = 1; i < blockLength; i++){
                tmp.add(op_in[0][i - 1]);
                ans[currentOutputIndex][i].Copy(tmp);
            }
        }
    }
}
