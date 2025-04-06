package com.example.sdr.Core.ProjectManager.Components.Base;

import java.lang.reflect.Field;

import com.example.sdr.Core.Components.Tools.PropertyModifier.AutoPropertyModifier;

public class BaseComponent {
    protected int inputCount;
    protected int blockLength;
    protected int sampleRate;
    
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

    public void resetBlockLength(int blockLength){
        this.blockLength = blockLength;
        ans = new double[blockLength];
        op_in = new double[inputCount][blockLength];
    }

    public double[] getAns(){
        return ans;
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

    public void setOperationParams(double[] data,int index)
    {
        // Override this method in child classes
    }

    public void Calculate(){
        // Override this method in child classes
    }
}
