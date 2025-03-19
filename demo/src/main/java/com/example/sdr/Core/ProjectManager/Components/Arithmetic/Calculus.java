package com.example.sdr.Core.ProjectManager.Components.Arithmetic;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class Calculus extends BaseComponent{
    private int blockLength;
    
    final int MODE_DIFFERENTIAL = 0;
    final int MODE_INTEGRATION = 1;

    private int mode;
    private double[] ans;

    public Calculus(int blockLength,int mode){
        super(blockLength,1); //
        this.inputCount = 1;
        this.blockLength = blockLength;
        this.mode = mode;

        op_in = new double[1][blockLength];
        ans = new double[blockLength];
    }

    public Calculus(int blockLength,int mode,String ID){
        super(blockLength, 1,ID);
        this.inputCount = 1;
        this.blockLength = blockLength;
        this.mode = mode;

        op_in = new double[1][blockLength];
        ans = new double[blockLength];
    }

    public double[] getAns(){
        return ans;
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
                ans[i] = op_in[0][i] - op_in[0][i-1];
            }
            ans[0] = 0;
        }else if(mode == MODE_INTEGRATION){
            ans[0] = 0;
            for(int i = 1; i < blockLength; i++){
                ans[i] = ans[i - 1] + op_in[0][i];
            }
        }
    }
}
