package com.example.sdr.Core.Components.Tools.PropertyModifier;

import java.lang.reflect.Field;

public class PropertyModifedByName {
    
    /**
     * 通用属性设置方法
     * @param target    目标对象实例
     * @param fieldName 要修改的字段名称
     * @param value     要设置的新值
     * @throws RuntimeException 当字段不存在或访问失败时抛出
     */
    public static void setProperty(Object target, String fieldName, Object value) {
        try {
            // 获取目标类的声明字段（包括私有字段）
            Field field = target.getClass().getDeclaredField(fieldName);
            
            // 设置字段可访问（突破private限制）
            field.setAccessible(true);
            
            // 处理基本类型自动装箱
            Object convertedValue = convertType(field.getType(), value);
            
            // 设置字段值
            field.set(target, convertedValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set property: " + fieldName, e);
        }
    }

    /**
     * 类型转换处理（支持基本类型转换）
     */
    private static Object convertType(Class<?> targetType, Object value) {
        if (value == null) return null;
        
        // 类型匹配直接返回
        if (targetType.isInstance(value)) {
            return value;
        }

        // 处理基本类型转换
        try {
            if (targetType == int.class || targetType == Integer.class) {
                return Integer.valueOf(value.toString());
            } else if (targetType == long.class || targetType == Long.class) {
                return Long.valueOf(value.toString());
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.valueOf(value.toString());
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.valueOf(value.toString());
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Type conversion failed", e);
        }

        throw new IllegalArgumentException("Unsupported type conversion: " 
            + value.getClass() + " to " + targetType);
    }

    // 示例用法
    public static void main(String[] args) {
        class DemoClass {
            private String name;
            private int count;
            
            @Override
            public String toString() {
                return "DemoClass{name='" + name + "', count=" + count + "}";
            }
        }

        DemoClass demo = new DemoClass();
        
        // 修改私有String字段
        setProperty(demo, "name", "New Value");
        
        // 修改基本类型int字段（自动转换）
        setProperty(demo, "count", "123");  // 字符串转int
        
        System.out.println(demo);  // 输出：DemoClass{name='New Value', count=123}
    }
}