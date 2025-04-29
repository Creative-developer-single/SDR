package com.example.sdr.Core.ProjectManager.Components.Arithmetic;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class BasicALU extends BaseComponent{

    final int MODE_ADD = 0;
    final int MODE_SUBTRACT = 1;
    final int MODE_MULTIPLY = 2;
    final int MODE_DIVIDE = 3;

    private int mode = MODE_ADD;
    private int channelIndex;

    public BasicALU(int blockLength,int inputCount,int outputCount){
        super(blockLength, inputCount, outputCount);
        this.blockLength = blockLength;

        op_in = new double[inputCount][blockLength];
        ans = new double[outputCount][blockLength];
    }

    public BasicALU(int block,int inputCount,int outputCount,String ID){
        super(block, inputCount,outputCount,ID);

        op_in = new double[inputCount][blockLength];
        ans = new double[outputCount][blockLength];
    }

    public void setOperationParams(double[] data,int index)
    {
        if(data.length != blockLength)
        {
            throw new IllegalArgumentException("Invalid block length");
        }
        this.op_in[index] = data;
    }

    public void setOperationParams(double[] a,double[] b)
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

    public double[] getAns(int index){
        return ans[index];
    }

    public double[] getValue(int index){
        return ans[index];
    }

    public void add()
    {
        for(int i = 0; i < blockLength; i++)
        {
            double tmp = 0;
            for(int j = 0; j < inputCount; j++)
            {
                tmp += op_in[j][i];
            }
            ans[0][i] = tmp;
        }
    }

    public void subtract()
    {
        for(int i = 0; i < blockLength; i++)
        {
            ans[0][i] = op_in[0][i] - op_in[1][i];
        }
    }

    public void multiply()
    {
        for(int i = 0; i < blockLength; i++)
        {
            double tmp = 1;
            for(int j = 0; j < inputCount; j++)
            {
                tmp *= op_in[j][i];
            }
            ans[0][i] = tmp;
        }
    }

    public void divide()
    {
       for(int i = 0; i < blockLength; i++)
        {
            if(op_in[1][i] == 0)
            {
                throw new ArithmeticException("Division by zero");
            }
            ans[0][i] = op_in[0][i] / op_in[1][i];
        }
    }
}