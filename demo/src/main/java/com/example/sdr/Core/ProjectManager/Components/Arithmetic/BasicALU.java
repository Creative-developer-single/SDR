package com.example.sdr.Core.ProjectManager.Components.Arithmetic;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class BasicALU extends BaseComponent{

    final int MODE_ADD = 0;
    final int MODE_SUBTRACT = 1;
    final int MODE_MULTIPLY = 2;
    final int MODE_DIVIDE = 3;

    private int mode = MODE_ADD;
    private String OperationMode = "Add";
    private int channelIndex;


    public BasicALU(int block,int inputCount,int outputCount,String ID){
        super(block, inputCount,outputCount,ID);

        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
        op_in = SDRDataUtils.createComplexMatrix(inputCount, blockLength, 0, 0);
    }

    public void setOperationParams(SDRData[] data,int index)
    {
        // 自适应速率
        if(data.length != blockLength) {
            resetBlockLength(data.length);
        }
        this.op_in[index] = data;
    }

    @Override
    public void refreshComponent() {
        // Reset output array length to blockLength
        if (ans[0] == null || ans[0].length != blockLength) {
            resetBlockLength(blockLength);
        }
    }

    public void setOperationMode(int mode)
    {
        this.mode = mode;
    }

    public void Calculate(){
        switch(OperationMode){
            case "Add":
                add();
                break;
            case "Subtract":
                subtract();
                break;
            case "Multiply":
                multiply();
                break;
            case "Divide":
                divide();
                break;
            default:
                throw new IllegalArgumentException("Invalid operation mode: " + OperationMode);
        }
    }

    public SDRData[] getAns(int index){
        return ans[index];
    }

    public SDRData[] getValue(int index){
        return ans[index];
    }

    public void add()
    {
        for(int i = 0; i < blockLength; i++)
        {
            SDRData tmp = new SDRData(0,0);
            for(int j = 0; j < inputCount; j++)
            {
                tmp.add(op_in[j][i]);
            }
            ans[0][i].Copy(tmp);
        }
    }

    public void subtract()
    {
        SDRData tmp = new SDRData(0,0);
        for(int i = 0; i < blockLength; i++)
        {
            tmp.Copy(op_in[0][i]); // Start with the first operand
            tmp.subtract(op_in[1][i]); // Subtract the second operand
            op_in[0][i].Copy(tmp); // Store the result in the first operand
        }
    }

    public void multiply()
    {
        for(int i = 0; i < blockLength; i++)
        {
            SDRData tmp = new SDRData(1,0); // Start with 1 for multiplication
            for(int j = 0; j < inputCount; j++)
            {
                tmp.multiply(op_in[j][i]);
            }
            ans[0][i].Copy(tmp); // Store the result in the first output
        }
    }

    public void divide()
    {
        SDRData tmp = new SDRData(1,0); // Start with 1 for division
        for(int i = 0; i < blockLength; i++)
        {
            if(op_in[1][i].equals(0))
            {
                throw new ArithmeticException("Division by zero");
            }
            tmp.Copy(op_in[0][i]); // Copy the first operand
            tmp.divide(op_in[1][i]);

            ans[0][i].Copy(tmp);
        }
    }
}