package com.example.sdr.Core.ProjectManager.Components.Base;

public class BaseComponent {
    protected int inputCount;
    protected int blockLength;
    
    protected String ID;

    protected double[][] op_in;
    protected double[] ans;

    public BaseComponent(int blockLength,int inputCount){
        this.blockLength = blockLength;
        this.inputCount = inputCount;
        ans = new double[blockLength];
        op_in = new double[inputCount][blockLength];
    }

    public BaseComponent(int blockLength,int inputCount,String ID){
        this.blockLength = blockLength;
        this.inputCount = inputCount;
        this.ID = ID;
        ans = new double[blockLength];
        op_in = new double[inputCount][blockLength];
    }

    public double[] getAns(){
        return ans;
    }

    public void setOperationParams(double[] data,int index)
    {
        // Override this method in child classes
    }

    public void Calculate(){
        // Override this method in child classes
    }
}
