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
    private int channelIndex;


    public BasicALU(int block,int inputCount,int outputCount,String ID){
        super(block, inputCount,outputCount,ID);

        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
        op_in = SDRDataUtils.createComplexMatrix(inputCount, blockLength, 0, 0);
    }

    public void setOperationParams(SDRData[] data,int index)
    {
        if(data.length != blockLength)
        {
            throw new IllegalArgumentException("Invalid block length");
        }
        this.op_in[index] = data;
    }

    public void setOperationParams(SDRData[] a,SDRData[] b)
    {
        if(a.length != blockLength || b.length != blockLength)
        {
            throw new IllegalArgumentException("Invalid block length");
        }
        this.op_in[0] = a;
        this.op_in[1] = b;
    }

    public void setOperationMode(int mode)
    {
        this.mode = mode;
    }

    public void Calculate(){
        switch(mode){
            case MODE_ADD:
                add();
                break;
            case MODE_SUBTRACT:
                subtract();
                break;
            case MODE_MULTIPLY:
                multiply();
                break;
            case MODE_DIVIDE:
                divide();
                break;
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