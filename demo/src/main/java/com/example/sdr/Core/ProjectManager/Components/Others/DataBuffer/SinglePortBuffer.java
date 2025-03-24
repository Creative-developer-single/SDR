package com.example.sdr.Core.ProjectManager.Components.Others.DataBuffer;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class SinglePortBuffer extends BaseComponent{
    private int point;
    private int bufferLength;

    public SinglePortBuffer(int blockLength, int bufferLength){
        super(blockLength, 0);
        ans = new double[bufferLength];
        point = 0;
    }

    public SinglePortBuffer(int blockLength, int bufferLength, String ID){
        super(blockLength, 0 ,ID);
        ans = new double[bufferLength];
        point = 0;
    }

    public void Calculate(){
        // Do nothing
    }

    public void resetBlockLength(int blockLength){
        this.blockLength = blockLength;
        ans = new double[bufferLength];
        point = 0;
    }

    public void setOperationParams(double[] data,int index){
        storeData(data);
    }

    public void storeData(double[] data){
        for(int i=0;i<=data.length-1;i++){
            ans[point] = data[i];
            point++;
            point = point % ans.length;
        }
    }
}
