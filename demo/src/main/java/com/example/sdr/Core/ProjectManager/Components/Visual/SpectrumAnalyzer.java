package com.example.sdr.Core.ProjectManager.Components.Visual;

import org.jtransforms.fft.DoubleFFT_1D;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.Components.Tools.FFTTools.FFTTools;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class SpectrumAnalyzer extends BaseComponent{
    private int FFTPoint;
    private String SpectrumType;// Amp or Phase or Power

    public SpectrumAnalyzer(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, inputCount, outputCount, ID);
        this.FFTPoint = blockLength;
        this.SpectrumType = "Amp"; // Default to Amplitude spectrum
    }

    public void Calculate() {
        // Do nothing
    }

    public SDRData[] CalculateFFT(SDRData[] data) {
        // 获取SDRData数组转换的一维复数数组
        double[] inputData = SDRDataUtils.toComplexArray(SDRDataUtils.padWithZeros(data, FFTPoint));

        // 创建FFT实例
        DoubleFFT_1D fft = new DoubleFFT_1D(FFTPoint);

        // 执行FFT变换
        fft.complexForward(inputData);

        return SDRDataUtils.fromComplexArray(inputData);
    }

    private void CalculateAmpSpectrum(SDRData[] data){
        SDRData[] fftData = CalculateFFT(data);

        // 计算幅度谱
        for (int i = 0; i < fftData.length; i++){
            fftData[i].abs();
        }

        ans[0] = fftData;
    }

    private void CalculatePhaseSpectrum(SDRData[] data){
        SDRData[] fftData = CalculateFFT(data);

        // 计算相位谱
        for (int i = 0; i < fftData.length; i++){
            fftData[i].phase();
        }

        ans[0] = fftData;
    }

    private void CalculatePowerSpectrum(SDRData[] data){
        SDRData[] fftData = CalculateFFT(data);

        // 计算功率谱
        for (int i = 0; i < fftData.length; i++){
            fftData[i].power();
        }

        ans[0] = fftData;
    }

    // Set operation parameters for the spectrum analyzer
    public void setOperationParams(SDRData[] data,int index){
        if (ans[0].length != FFTPoint) {
            resetBlockLength(FFTPoint);
        }
        
        switch(SpectrumType){
            case "Amp":
                CalculateAmpSpectrum(data);
                break;
            case "Phase":
                CalculatePhaseSpectrum(data);
                break;
            case "Power":
                CalculatePowerSpectrum(data);
                break;
            default:
                throw new IllegalArgumentException("Invalid spectrum type. Use 'Amp', 'Phase', or 'Power'.");
        }

        // 频谱中心化
        ans[0] = FFTTools.fftShift(ans[0]);
    }

}
