package com.example.sdr.Core.ProjectManager.Components.Tools;

import java.lang.reflect.Constructor;

import org.json.JSONObject;

import com.example.sdr.Core.Components.Tools.PropertyModifier.AutoPropertyModifier;
import com.example.sdr.Core.ProjectManager.Components.ComponentManager;


public class ComponentCreator {
    private ComponentManager manager;

    public Object createComponentByClassName(JSONObject object){
        try{
            String componentType = object.getString("ComponentType");
            String className = ComponentManager.class.getPackageName() + "." + componentType;
            System.out.println(className);
            Class<?> componentClass = Class.forName(ComponentManager.class.getPackageName() + "." + componentType);
            
            Constructor<?> constructor = componentClass.getConstructor(int.class,int.class,int.class,String.class);

            int BlockLength = object.getInt("BlockLength");
            int InputCount = object.getInt("InputCount");
            int OutputCount = object.getInt("OutputCount");
            String ComponentID = object.getString("ComponentID");
            JSONObject componnentSetting = object.getJSONObject("ComponentSettings");
            
            Object component = constructor.newInstance(BlockLength,InputCount,OutputCount,ComponentID);

            //Modify the Properties
            AutoPropertyModifier.setPropertiesFromJson(component, componnentSetting);
            return component;
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ComponentCreator() {
        this.manager = null;
    }

    public ComponentCreator(ComponentManager manager) {
        this.manager = manager;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ComponentCreator creator = new ComponentCreator(null);
        JSONObject object = new JSONObject();
        object.put("ComponentType", "Arithmetic.BasicALU");
        object.put("BlockLength", 1024);
        object.put("InputCount", 2);
        object.put("OutputCount", 1);
        object.put("ComponentID", "alu1");
        Object ansObject = creator.createComponentByClassName(object);
        String name = ansObject.getClass().getName();
        System.out.println(name);
    }
}
