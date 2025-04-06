package com.example.sdr.Core.ProjectManager.Components.AFC.AGC;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class AGC extends BaseComponent{
    
    public AGC(int block){
        super(block,1);
        blockLength = block;
        op_in[0] = new double[blockLength];
        ans = new double[blockLength];
    }

    public void Calculate(){
        
    }
}
