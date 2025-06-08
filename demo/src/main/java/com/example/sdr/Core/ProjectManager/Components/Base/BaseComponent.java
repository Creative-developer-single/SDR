package com.example.sdr.Core.ProjectManager.Components.Base;

import java.lang.reflect.Field;

import org.apache.commons.math3.complex.Complex;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.ComponentManager;

public class BaseComponent {
    protected int inputCount;
    protected int outputCount;
    protected int blockLength;
    protected int SampleRate;
    
    protected String ID;

    //ChannelIndex
    protected int currentInputIndex;
    protected int currentOutputIndex;

    protected SDRData[][] op_in;
    //protected double[][] op_in;
    protected SDRData[][] ans;

    public static String getRootClassName(){
        return ComponentManager.class.getPackageName();
    }

    public BaseComponent(int blockLength,int inputCount,int outputCount){
        this.blockLength = blockLength;
        this.inputCount = inputCount;
        this.outputCount = outputCount;

        currentInputIndex = 0;
        currentOutputIndex = 0;
        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
        op_in = SDRDataUtils.createComplexMatrix(inputCount, blockLength, 0, 0);
    }

    public BaseComponent(int blockLength,int inputCount,int outputCount,String ID){
        this.blockLength = blockLength;
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.ID = ID;

        currentInputIndex = 0;
        currentOutputIndex = 0;
        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
        op_in = SDRDataUtils.createComplexMatrix(inputCount, blockLength, 0, 0);
    }

    public int getInputCount() {
        return inputCount;
    }
    public int getOutputCount() {
        return outputCount;
    }

    public void resetBlockLength(int blockLength){
        this.blockLength = blockLength;
        ans = SDRDataUtils.createComplexMatrix(outputCount, blockLength, 0, 0);
        op_in = SDRDataUtils.createComplexMatrix(inputCount, blockLength, 0, 0);
    }

    public void refreshComponent(){
        // Override this method in child classes if needed
        // This method can be used to refresh the component's state or properties
    }

    public SDRData[] getAns(int index){
        return ans[index];
    }

    public Boolean ModifyPropertiesByName(String name, Object value){
        // Override this method in child classes
        try {
            // 1. 获取类的 Field 对象（包括私有字段）
            Field field = this.getClass().getDeclaredField(name);
            
            // 2. 绕过访问权限检查（即使字段是 private）
            field.setAccessible(true);
            
            // 3. 类型转换 + 设置值
            if (field.getType() == int.class) {
                field.setInt(this, Integer.parseInt(value.toString()));
            } else if (field.getType() == boolean.class) {
                field.setBoolean(this, Boolean.parseBoolean(value.toString()));
            } else {
                field.set(this, value);
            }
            
            return true; // 修改成功
        } catch (NoSuchFieldException e) {
            System.err.println("属性不存在: " + name);
            return false;
        } catch (IllegalAccessException | IllegalArgumentException e) {
            System.err.println("属性类型不匹配: " + name);
            return false;
        }
    }

    public void setOperationParams(SDRData[] data,int index)
    {
        // Override this method in child classes
        if(data.length != blockLength)
        {
            throw new IllegalArgumentException("Invalid block length");
        }
        this.op_in[index] = data;
    }

    public void Calculate(){
        // Override this method in child classes
    }
}
