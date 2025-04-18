package com.example.sdr.Core.ProjectManager.Components.Arithmetic;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class Calculus extends BaseComponent{
    private int blockLength;
    
    final int MODE_DIFFERENTIAL = 0;
    final int MODE_INTEGRATION = 1;

    private int mode;

    public Calculus(int blockLength,int inputCount,int outputCount){
        super(blockLength,inputCount,outputCount); //
        this.inputCount = 1;
        this.blockLength = blockLength;
        this.mode = MODE_DIFFERENTIAL;

        op_in = new double[inputCount][blockLength];
        ans = new double[outputCount][blockLength];
    }

    public Calculus(int blockLength,int inputCount,int outputCount,String ID){
        super(blockLength, 1,outputCount,ID);
        this.inputCount = 1;
        this.blockLength = blockLength;
        this.mode = MODE_DIFFERENTIAL;

        op_in = new double[inputCount][blockLength];
        ans = new double[outputCount][blockLength];
    }

    public double[] getAns(int index){
        return ans[index];
    }

    public void setOperationMode(int mode){
        this.mode = mode;
    }

    public void setInputData(double[] data){
        if(data.length != inputCount){
            throw new IllegalArgumentException("Invalid input count");
        }else{
            op_in[0] = data;
        }
    }

    public void Calculate(){
        if(mode == MODE_DIFFERENTIAL){
            for(int i = 1; i < blockLength; i++){
                ans[currentOutputIndex][i] = op_in[0][i] - op_in[0][i-1];
            }
            ans[currentOutputIndex][0] = 0;
        }else if(mode == MODE_INTEGRATION){
            ans[currentOutputIndex][0] = 0;
            for(int i = 1; i < blockLength; i++){
                ans[currentOutputIndex][i] = ans[currentOutputIndex][i - 1] + op_in[0][i];
            }
        }
    }
}
