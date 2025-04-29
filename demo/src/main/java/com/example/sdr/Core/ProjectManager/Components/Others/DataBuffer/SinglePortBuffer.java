package com.example.sdr.Core.ProjectManager.Components.Others.DataBuffer;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class SinglePortBuffer extends BaseComponent{
    private int point;
    private int bufferLength;

    public SinglePortBuffer(int blockLength, int inputCount, int outputCount,String ID){
        super(blockLength, inputCount, outputCount,ID);
        ans = new double[outputCount][blockLength];
        point = 0;
    }

    public SinglePortBuffer(int blockLength, int bufferLength){
        super(blockLength, 1,1);
        ans = new double[inputCount][bufferLength];
        point = 0;
    }

    public SinglePortBuffer(int blockLength, int bufferLength, String ID){
        super(blockLength, 1,1 ,ID);
        ans = new double[outputCount][bufferLength];
        point = 0;
    }

    public void Calculate(){
        // Do nothing
    }

    public void resetBlockLength(int blockLength){
        this.blockLength = blockLength;
        ans = new double[outputCount][bufferLength];
        point = 0;
    }

    public void setOperationParams(double[] data,int index){
        storeData(data);
    }

    public void storeData(double[] data){
        for(int i=0;i<=data.length-1;i++){
            ans[0][point] = data[i];
            point++;
            point = point % ans[0].length;
        }
    }

    public static void main(String[] args){
        SinglePortBuffer buffer = new SinglePortBuffer(0, 0);
        System.out.println(buffer.getClass().getName());
    }
}
