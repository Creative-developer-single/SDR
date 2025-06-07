package com.example.sdr.Core.ProjectManager.Components.Converter;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class DataConverter extends BaseComponent{
    private String ConvertType; // Real,Complex,Imag

    public DataConverter(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, inputCount, outputCount, ID);
        this.ConvertType = "Real"; // 默认转换类型为 Real
    }

    @Override
    public void refreshComponent() {
        // 重置转换类型为默认值
        this.ConvertType = "Real";
    }

    @Override
    public void Calculate(){
        switch(ConvertType){
            case "Real":
                for(int i=0;i<blockLength;i++){
                    ans[0][i].Copy(op_in[0][i]);
                    ans[0][i].setComputeMode(SDRData.ComputeMode.REAL);
                }
                break;
            case "Imag":
                for(int i=0;i<blockLength;i++){
                    ans[0][i].setReal(op_in[0][i].getImag());
                    ans[0][i].setComputeMode(SDRData.ComputeMode.REAL);
                }
                break;
            case "Complex":
                for(int i=0;i<blockLength;i++){
                    ans[0][i].Copy(op_in[0][i]);
                    ans[0][i].setComputeMode(SDRData.ComputeMode.COMPLEX);
                }
            break;
        default:
            break;
        }
    }
}
