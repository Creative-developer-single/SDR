package com.example.sdr.Core.ProjectManager.Components.Others.DataBuffer;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class SinglePortBuffer extends BaseComponent{
    private int point;

    public SinglePortBuffer(int blockLength, int size){
        super(blockLength, size);
        ans = new double[size];
        point = 0;
    }

    public SinglePortBuffer(int blockLength, int size, String ID){
        super(blockLength, size, ID);
        ans = new double[size];
        point = 0;
    }

    public void Calculate(){
        // Do nothing
    }

    public void setOperationParams(double[] data,int index){
        storeData(data);
    }

    public void storeData(double[] data){
        for(int i=0;i<=data.length-1;i++){
            ans[point] = data[i];
            point++;
        }
    }
}
