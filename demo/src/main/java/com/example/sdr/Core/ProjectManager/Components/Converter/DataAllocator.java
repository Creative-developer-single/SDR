package com.example.sdr.Core.ProjectManager.Components.Converter;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class DataAllocator extends BaseComponent{
    public DataAllocator(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, 1, 2, ID);
        // 初始化数据分配
    }

    @Override
    public void Calculate(){
        // 复数数据转双路输出
        for(int i=0;i<blockLength;i++){
            ans[0][i].fromDouble(op_in[0][i].getReal());
            ans[1][i].fromDouble(op_in[0][i].getImag());
        }
    }

    @Override
    public void refreshComponent() {
        // 重置数据分配逻辑
        // 这里可以添加任何需要的初始化或重置逻辑
    }
}
