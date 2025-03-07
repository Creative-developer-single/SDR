package com.example.sdr.Core.Components.DataType;

public class DoubleBasicType {
    private double value;

    public DoubleBasicType(double value)
    {
        this.value = value;
    }

    public double getValue()
    {
        return value;
    }

    public double add(DoubleBasicType other)
    {
        return value + other.getValue();
    }

    public double subtract(DoubleBasicType other)
    {
        return value - other.getValue();
    }

    public double multiply(DoubleBasicType other)
    {
        return value * other.getValue();
    }

    public double divide(DoubleBasicType other)
    {
        if(other.getValue() == 0)
        {
            throw new ArithmeticException("Division by zero");
        }
        return value / other.getValue();
    }

}