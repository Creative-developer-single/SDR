package com.example.sdr.Core.ProjectManager.Components.FIR;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

public class FIRHighPass extends BaseComponent{
    private int CutOffFrequency;
    private int WindowLength;
    private String WindowType; // 可以是 "Hamming", "Hanning", "Blackman","Rect" 等
    private SDRData[] filterCoefficients;
    // 记忆缓冲区，跨块连续滤波用
    private SDRData[] historyBuffer;

    public FIRHighPass(int blockLength, int inputCount, int outputCount, String ID) {
        super(blockLength, inputCount, outputCount, ID);
        this.CutOffFrequency = 1000; // Default cutoff frequency
        this.WindowLength = 64; // Default window length
        this.filterCoefficients = SDRDataUtils.createComplexArray(WindowLength,0,0);
        // 初始化历史缓冲区
        this.historyBuffer = SDRDataUtils.createComplexArray(WindowLength - 1, 0, 0);
    }

    // 计算滤波器系数
    public void CalculateFilterCoefficients(){
        // 这里可以使用窗函数方法生成FIR滤波器系数
        // 例如使用Hamming窗或Hanning窗等
        // 具体实现取决于所需的滤波器特性
        double tar = (WindowLength - 1) / 2.0;
        for(int i=1;i<=WindowLength;i++){
            double wc = 2 * Math.PI * CutOffFrequency / SampleRate; // 归一化截止频率
            filterCoefficients[i-1].setReal((Math.sin((i-tar)*Math.PI) - Math.sin((i-tar)*wc)) / (Math.PI * (i-tar)));
            filterCoefficients[i-1].setImag(0); // 假设滤波器系数为实数
        }

        switch(WindowType){
            case "Rect":
                // Rectangular window does not modify coefficients
                break;
            case "Hamming":
                for (int i = 1; i <= WindowLength; i++) {
                    filterCoefficients[i].setReal(filterCoefficients[i].getReal() * (0.54 - 0.46 * Math.cos(2 * Math.PI * i / (WindowLength))));
                }
                break;
            case "Hanning":
                for (int i = 1; i <= WindowLength; i++) {
                    filterCoefficients[i].setReal(filterCoefficients[i].getReal() * (0.5 - 0.5 * Math.cos(2 * Math.PI * i / (WindowLength))));
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported window type: " + WindowType);
        }
    }

    public void refreshComponent() {
        // 重新计算滤波器系数
        this.filterCoefficients = SDRDataUtils.createComplexArray(WindowLength, 0, 0);
        CalculateFilterCoefficients();

        // 清空历史缓冲区，保证仿真重启时状态正确
        this.historyBuffer = SDRDataUtils.createComplexArray(WindowLength - 1, 0, 0);
    }

    @Override
    public void setOperationParams(SDRData[] data, int index) {
        // 自适应输入速率
        if (data.length != blockLength) {
            resetBlockLength(data.length);
        }
        op_in[index] = data;
    }

    public void Calculate() {
        // 拼接历史 + 当前块
        SDRData[] extendedInput = new SDRData[WindowLength - 1 + blockLength];

        // 填入历史
        for (int i = 0; i < WindowLength - 1; i++) {
            extendedInput[i] = historyBuffer[i].getCopy();
        }

        // 填入当前块
        for (int i = 0; i < blockLength; i++) {
            extendedInput[WindowLength - 1 + i] = op_in[0][i].getCopy();
        }

        // 执行卷积
        for (int i = 0; i < blockLength; i++) {
            SDRData acc = new SDRData(0, 0);
            for (int j = 0; j < WindowLength; j++) {
                int idx = i + (WindowLength - 1) - j;
                SDRData temp = extendedInput[idx].getCopy();
                temp.multiply(filterCoefficients[j]);
                acc.add(temp);
            }
            ans[0][i].Copy(acc);
        }

        // 更新历史缓冲区，保留当前块最后 WindowLength-1 个输入
        for (int i = 0; i < WindowLength - 1; i++) {
            historyBuffer[i] = op_in[0][blockLength - (WindowLength - 1) + i].getCopy();
        }
    }
}
