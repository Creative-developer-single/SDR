package com.example.sdr.Core.ProjectManager.Components.Arithmetic;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class NonLinear extends BaseComponent{
    private int blockLength;

    final int MODE_RECTIFICATION = 0;
    final int MODE_ABS = 1;
    final int MODE_RELU = 2;
    final int MODE_CLIPPING = 3;


    private int mode;

    private double clippingLevel;

    public NonLinear(int blockLength,int inputCount,int outputCount){
        super(blockLength,inputCount,outputCount);

        op_in = new double[inputCount][blockLength];
        ans = new double[outputCount][blockLength];
    }

    public NonLinear(int blockLength,int inputCount,int outputCount,String ID){
        super(blockLength,inputCount,outputCount,ID);

        op_in = new double[inputCount][blockLength];
        ans = new double[outputCount][blockLength];
    }

    public double[] getAns(int index){
        return ans[index];
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    public void setClippingLevel(double clippingLevel){
        this.clippingLevel = clippingLevel;
    }

    public void Calculate(){
        double[] op_tmp = this.op_in[currentInputIndex];
        double[] ans_tmp = this.ans[currentOutputIndex];
        if(mode == MODE_RECTIFICATION){
            for(int i = 0; i < blockLength; i++){
                ans_tmp[i] = Math.max(0, op_tmp[i]);
            }
        }else if(mode == MODE_ABS){
            for(int i = 0; i < blockLength; i++){
                ans_tmp[i] = Math.abs(op_tmp[i]);
            }
        }else if(mode == MODE_RELU){
            for(int i = 0; i < blockLength; i++){
                ans_tmp[i] = Math.max(0, op_tmp[i]);
            }
        }else if(mode == MODE_CLIPPING){
            for(int i = 0; i < blockLength; i++){
                ans_tmp[i] = Math.max(-clippingLevel, Math.min(clippingLevel, op_tmp[i]));
            }
        }
    }

}
