package com.example.sdr.Core.ProjectManager.Components.Arithmetic;

import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class BasicALU extends BaseComponent{

    final int MODE_ADD = 0;
    final int MODE_SUBTRACT = 1;
    final int MODE_MULTIPLY = 2;
    final int MODE_DIVIDE = 3;

    private int mode = MODE_ADD;

    public BasicALU(int block){
        super(block,2);
        blockLength = block;

        op_in[0] = new double[blockLength];
        op_in[1] = new double[blockLength];
        ans = new double[blockLength];
    }

    public BasicALU(int block,String ID){
        super(block, 2,ID);

        op_in[0] = new double[blockLength];
        op_in[1] = new double[blockLength];
        ans = new double[blockLength];
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

    public double[] getAns(){
        return ans;
    }

    public double[] getValue(){
        return ans;
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
            ans[i] = tmp;
        }
    }

    public void subtract()
    {
        for(int i = 0; i < blockLength; i++)
        {
            ans[i] = op_in[0][i] - op_in[1][i];
        }
    }

    public void multiply()
    {
        for(int i = 0; i < blockLength; i++)
           for(int j = 0; j < inputCount; j++)
            {
                ans[i] *= op_in[j][i];
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
            ans[i] = op_in[0][i] / op_in[1][i];
        }
    }
}