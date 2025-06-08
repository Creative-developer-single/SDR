package com.example.sdr.Core.ProjectManager.Components.Converter;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class DataCombiner extends BaseComponent {
    public DataCombiner(int blockLength, int inputCount, int outputCount, String ID) {
        // 初始化数据合并组件
        // 这里可以添加任何需要的初始化逻辑
        super(blockLength, inputCount, outputCount, ID);

    }

    @Override
    public void refreshComponent() {
        // 重置数据合并逻辑
        // 这里可以添加任何需要的初始化或重置逻辑
        resetBlockLength(blockLength);
    }

    @Override
    public void setOperationParams(SDRData[] data, int index) {
        // 自适应输入速率
        if (data.length != blockLength) {
            resetBlockLength(data.length);
        }
        // 设置输入数据
        op_in[index] = data;
    }

    @Override
    public void Calculate(){
        // 合并双路实虚数据输出
        for(int i=0;i<blockLength;i++){
            ans[0][i].setReal(op_in[0][i].getReal());
            ans[0][i].setImag(op_in[1][i].getReal());
        }
    }
}
