package com.example.sdr.Core.Components.DataType;

public class ByteBasicType {
    private byte value;

    public ByteBasicType(byte value){
        this.value = value;
    }

    public byte getValue(){
        return value;
    }

    public byte add(ByteBasicType other){
        return (byte) (value + other.getValue());
    }

    public byte subtract(ByteBasicType other){
        return (byte) (value - other.getValue());
    }

    public byte multiply(ByteBasicType other){
        return (byte) (value * other.getValue());
    }

    public byte divide(ByteBasicType other){
        if(other.getValue() == 0){
            throw new ArithmeticException("Division by zero");
        }
        return (byte) (value / other.getValue());
    }
}
