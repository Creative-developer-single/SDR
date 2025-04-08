package com.example.sdr.Core.Components.Tools.ArrayConverter;

import org.json.JSONArray;

public class JSONToDouble {
    public static double[] getDoubleFromJSONString(String JSONStr){
        try{
            JSONArray jsonArray = new JSONArray(JSONStr);
            double[] doubleArray = new double[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                doubleArray[i] = jsonArray.getDouble(i);
            }

            return doubleArray;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String jsonStr = "[1.5, 2.3, 3.7, 4.0]";
        
        JSONArray jsonArray = new JSONArray(jsonStr);
        double[] doubleArray = new double[jsonArray.length()];
        
        for (int i = 0; i < jsonArray.length(); i++) {
            doubleArray[i] = jsonArray.getDouble(i);
        }
        
        // 验证输出
        for (double num : doubleArray) {
            System.out.print(num + " "); // 1.5 2.3 3.7 4.0 
        }
    }
}
