package com.example.sdr.Core.ProjectManager.Components.Arithmetic;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
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

        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
        op_in = SDRDataUtils.createComplexMatrix(inputCount, blockLength, 0, 0);
    }

    public NonLinear(int blockLength,int inputCount,int outputCount,String ID){
        super(blockLength,inputCount,outputCount,ID);

        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
        op_in = SDRDataUtils.createComplexMatrix(inputCount, blockLength, 0, 0);
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    public void setClippingLevel(double clippingLevel){
        this.clippingLevel = clippingLevel;
    }

    public void Calculate(){
        SDRData valueZero = new SDRData(0, 0);
        SDRData tmp = new SDRData(0, 0);
        if(mode == MODE_RECTIFICATION){
            for(int i = 0; i < blockLength; i++){
                tmp.Copy(op_in[0][i]);
                tmp.Max(valueZero);
                ans[0][i].Copy(tmp);
            }
        }else if(mode == MODE_ABS){
            for(int i = 0; i < blockLength; i++){
                tmp.Copy(op_in[0][i]);
                tmp.abs();
                ans[0][i].Copy(tmp);
            }
        }else if(mode == MODE_RELU){
            for(int i = 0; i < blockLength; i++){
                tmp.Copy(op_in[0][i]);
                tmp.Max(valueZero);
                ans[0][i].Copy(tmp);
            }
        }
        /*else if(mode == MODE_CLIPPING){
            for(int i = 0; i < blockLength; i++){
                ans_tmp[i] = Math.max(-clippingLevel, Math.min(clippingLevel, op_tmp[i]));
            }
        }*/
    }

}
