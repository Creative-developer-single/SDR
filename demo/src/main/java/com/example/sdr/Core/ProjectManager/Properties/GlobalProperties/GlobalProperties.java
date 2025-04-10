package com.example.sdr.Core.ProjectManager.Properties.GlobalProperties;

import org.json.JSONObject;

public class GlobalProperties {
    public static final int DEFAULT_SAMPLE_RATE = 1000000;
    public static final int DEFAULT_BLOCK_LENGTH = 1024;

    //Default Global Properties
    public int sampleRate;
    public int blockLength;

    public void LoadFromJSON(JSONObject object){
        //Load the Global Properties
        sampleRate = object.getInt("SampleRate");
        blockLength = object.getInt("BlockLength");
    }

    public GlobalProperties() {
        sampleRate = DEFAULT_SAMPLE_RATE;
        blockLength = DEFAULT_BLOCK_LENGTH;
    }
}
