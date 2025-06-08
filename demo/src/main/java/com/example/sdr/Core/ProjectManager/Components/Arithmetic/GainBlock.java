package com.example.sdr.Core.ProjectManager.Components.Arithmetic;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class GainBlock extends BaseComponent{
    private double GainFactor;

    public GainBlock(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, inputCount, outputCount, ID);
        
        // 初始化增益块
    }

    @Override
    public void Calculate() {
        // 对输入数据应用增益因子
        for (int i = 0; i < blockLength; i++) {
            ans[0][i].setReal(op_in[0][i].getReal() * GainFactor);
            ans[0][i].setImag(op_in[0][i].getImag() * GainFactor);
        }
    }

    
}
