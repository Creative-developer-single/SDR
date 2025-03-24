package com.example.sdr.Core.Components.Tools.PropertyModifier;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.json.JSONObject;

public class AutoPropertyModifier {
    public static void setPropertiesFromJson(Object obj, JSONObject json) {
        Class<?> clazz = obj.getClass(); // 获取实际对象的类
    
        // 遍历 JSON 的键值对
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = json.get(key);
    
            try {
                // 递归查找字段（包括父类）
                Field field = getFieldFromHierarchy(clazz, key);
                if (field != null) {
                    field.setAccessible(true);
                    field.set(obj, convertValue(field.getType(), value)); // 类型转换后设置值
                }
            } catch (Exception e) {
                System.out.println("无法修改属性：" + key + "，错误：" + e.getMessage());
            }
        }
    }
    
    // 递归查找字段，包括父类
    private static Field getFieldFromHierarchy(Class<?> clazz, String fieldName) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass(); // 向上查找
            }
        }
        return null;
    }
    
    // 处理类型转换
    private static Object convertValue(Class<?> fieldType, Object value) {
        if (value == null) return null;
    
        if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(value.toString());
        } else if (fieldType == double.class || fieldType == Double.class) {
            return Double.parseDouble(value.toString());
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return Boolean.parseBoolean(value.toString());
        } else if (fieldType == String.class) {
            return value.toString();
        } else {
            return value; // 直接返回对象（如数组、List）
        }
    }
}
