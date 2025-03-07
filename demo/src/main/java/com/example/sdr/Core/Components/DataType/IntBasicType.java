package com.example.sdr.Core.Components.DataType;

public class IntBasicType {
    private int value;

    public IntBasicType(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public int add(IntBasicType other){
        return value + other.getValue();
    }

    public int subtract(IntBasicType other){
        return value - other.getValue();
    }

    public int multiply(IntBasicType other){
        return value * other.getValue();
    }

    public int divide(IntBasicType other){
        if(other.getValue() == 0){
            throw new ArithmeticException("Division by zero");
        }
        return value / other.getValue();
    }

}
