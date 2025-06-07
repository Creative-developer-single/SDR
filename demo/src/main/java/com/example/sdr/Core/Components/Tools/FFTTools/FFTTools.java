package com.example.sdr.Core.Components.Tools.FFTTools;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;

public class FFTTools {
    public static SDRData[] fftShift(SDRData[] data) {
        int N = data.length;
        SDRData[] shifted = new SDRData[N];
        int half = N / 2;
    
        // 后半段移到前面
        for (int i = 0; i < half; i++) {
            shifted[i] = data[i + half].getCopy();
        }
    
        // 前半段移到后面
        for (int i = 0; i < half; i++) {
            shifted[i + half] = data[i].getCopy();
        }
    
        return shifted;
    }
}
