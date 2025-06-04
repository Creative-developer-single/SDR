package com.example.sdr.Core.ProjectManager.Components.AFC.AGC;

import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class AGC extends BaseComponent{
    
    public AGC(int block){
        super(block,1,1);
        blockLength = block;
        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
        op_in = SDRDataUtils.createComplexMatrix(inputCount, blockLength, 0, 0);
    }

    public void Calculate(){
        
    }
}
