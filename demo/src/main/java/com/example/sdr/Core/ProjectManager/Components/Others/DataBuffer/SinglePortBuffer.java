package com.example.sdr.Core.ProjectManager.Components.Others.DataBuffer;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class SinglePortBuffer extends BaseComponent{
    private int point;
    private int bufferLength;
    private String BufferMode;

    public SinglePortBuffer(int blockLength, int inputCount, int outputCount,String ID){
        super(blockLength, inputCount, outputCount,ID);
        this.BufferMode = "Complex";
        ans = SDRDataUtils.createComplexMatrix(outputCount, bufferLength, 0, 0);
        point = 0;
    }

    public SinglePortBuffer(int blockLength, int bufferLength){
        super(blockLength, 1,1);
        this.BufferMode = "Complex";
        ans = SDRDataUtils.createComplexMatrix(1, bufferLength, 0, 0);
        point = 0;
    }

    public SinglePortBuffer(int blockLength, int bufferLength, String ID){
        super(blockLength, 1,1 ,ID);
        this.BufferMode = "Complex";
        ans = SDRDataUtils.createComplexMatrix(1, bufferLength, 0, 0);
        point = 0;
    }

    public void Calculate(){
        // Do nothing
    }

    public void resetBlockLength(int blockLength){
        this.blockLength = blockLength;
        ans = SDRDataUtils.createComplexMatrix(outputCount, bufferLength, 0, 0);
        point = 0;
    }

    public void setOperationParams(SDRData[] data,int index){
        if(ans[0].length != bufferLength){
            resetBlockLength(bufferLength);
        }
        storeData(data);
    }

    public void storeData(SDRData[] data){
        for(int i=0;i<=data.length-1;i++){
            ans[0][point].Copy(data[i]);
            point++;
            point = point % ans[0].length;
        }
    }

    public SDRData[] getAns(int index){
        switch(BufferMode){
            case "Complex":
                return ans[0];
            case "Real":
                SDRDataUtils.toRealMode(ans[index]);
                return ans[0];
            case "Imag":
                SDRDataUtils.toComplexMode(ans[index]);
                return ans[0];
            default:
                throw new IllegalArgumentException("Invalid Buffer Mode");
        }
    }

    public static void main(String[] args){
        SinglePortBuffer buffer = new SinglePortBuffer(0, 0);
        System.out.println(buffer.getClass().getName());
    }
}
