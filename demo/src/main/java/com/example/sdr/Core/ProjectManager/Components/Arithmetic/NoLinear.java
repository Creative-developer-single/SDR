package com.example.sdr.Core.ProjectManager.Components.Arithmetic;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class NoLinear extends BaseComponent{
    private int blockLength;

    final int MODE_RECTIFICATION = 0;
    final int MODE_ABS = 1;
    final int MODE_RELU = 2;
    final int MODE_CLIPPING = 3;

    private double[] op_in;
    private double[] ans;

    private int mode;

    private double clippingLevel;

    public NoLinear(int blockLength,int inputCount){
        super(blockLength,inputCount);
        this.blockLength = blockLength;

        op_in = new double[blockLength];
        ans = new double[blockLength];
    }

    public double[] getAns(){
        return ans;
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    public void setInputData(double[] data){
        if(data.length != blockLength){
            throw new IllegalArgumentException("Invalid block length");
        }else{
            op_in = data;
        }
    }

    public void setClippingLevel(double clippingLevel){
        this.clippingLevel = clippingLevel;
    }

    public void Calculate(){
        if(mode == MODE_RECTIFICATION){
            for(int i = 0; i < blockLength; i++){
                ans[i] = Math.max(0, op_in[i]);
            }
        }else if(mode == MODE_ABS){
            for(int i = 0; i < blockLength; i++){
                ans[i] = Math.abs(op_in[i]);
            }
        }else if(mode == MODE_RELU){
            for(int i = 0; i < blockLength; i++){
                ans[i] = Math.max(0, op_in[i]);
            }
        }else if(mode == MODE_CLIPPING){
            for(int i = 0; i < blockLength; i++){
                ans[i] = Math.max(-clippingLevel, Math.min(clippingLevel, op_in[i]));
            }
        }
    }

}
