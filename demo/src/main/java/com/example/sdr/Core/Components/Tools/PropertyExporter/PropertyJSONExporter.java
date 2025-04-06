package com.example.sdr.Core.Components.Tools.PropertyExporter;

import org.json.JSONObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PropertyJSONExporter {
    /**
     * 将对象属性导出为 JSONObject（包含父类属性）
     * @param obj 需要导出的对象
     * @return 生成的 JSONObject 对象
     */
    public static JSONObject exportToJson(Object obj) {
        JSONObject jsonObject = new JSONObject();
        try {
            // 获取所有字段（包括父类）
            List<Field> allFields = getAllFields(obj.getClass());

            for (Field field : allFields) {
                // 跳过静态字段
                if (Modifier.isStatic(field.getModifiers())) continue;

                Class<?> type = field.getType();
                // 类型过滤：仅处理 String 和数值类型，排除数组/集合
                if (shouldSkipField(type)) continue;

                field.setAccessible(true);
                Object value = field.get(obj);

                // 存储键值对
                jsonObject.put(field.getName(), value);
            }
        } catch (IllegalAccessException e) {
            System.err.println("字段访问错误: " + e.getMessage());
        }
        return jsonObject;
    }

    // 递归获取类及其所有父类的字段
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                fields.add(field);
            }
            clazz = clazz.getSuperclass(); // 获取父类继续处理
        }
        return fields;
    }

    // 类型判断逻辑保持不变
    private static boolean shouldSkipField(Class<?> type) {
        return type.isArray() ||
               Collection.class.isAssignableFrom(type) ||
               !(type == String.class ||
                 type == int.class    || type == Integer.class ||
                 type == float.class || type == Float.class);
    }

    // 测试父类
    public static class ParentClass {
        private String parentField = "parentValue";
        private int parentNumber = 100;
    }

    // 测试子类
    public static class TestClass extends ParentClass {
        private String childField = "childValue";
        private float childNumber = 50.5f;
        private List<String> excludeList = List.of("test");
    }

    public static void main(String[] args) {
        TestClass obj = new TestClass();
        JSONObject json = exportToJson(obj);
        
        // 输出结果应包含父类字段
        System.out.println(json.toString(2));
        /* 输出：
        {
          "childField": "childValue",
          "childNumber": 50.5,
          "parentField": "parentValue",
          "parentNumber": 100
        }
        */
    }
}