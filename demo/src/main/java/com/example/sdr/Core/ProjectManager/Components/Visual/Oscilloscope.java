package com.example.sdr.Core.ProjectManager.Components.Visual;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

/**
 * 示波器组件 (具有记忆和滚动功能)
 * <p>
 * 该组件用于可视化信号。它维护一个固定长度的内部缓冲区。
 * 每次接收到新的数据块时，它会将旧数据向左平移，并将新数据追加到末尾，
 * 从而产生数据在示波器上持续滚动的视觉效果。
 * </p>
 */
public class Oscilloscope extends BaseComponent {

    /**
     * 示波器内部显示缓冲区的长度
     */
    private int bufferLength;

    /**
     * 控制显示的数据类型:
     * "Real" - 显示实部
     * "Imag" - 显示虚部
     * "Abs"  - 显示幅度 (Magnitude)
     */
    private String DataType;

    public Oscilloscope(int blockLength,int inputCount, int outputCount, String ID) {
        // 示波器是1输入，0输出的终端组件
        super(blockLength, inputCount, outputCount, ID);
        this.bufferLength = blockLength; // 使用 blockLength 作为缓冲区长度
        this.DataType = "Real"; // 默认显示实部数据
        
        // 初始化内部缓冲区
        refreshComponent();
    }

    public Oscilloscope(int bufferLength, String ID) {
        // 示波器是1输入，0输出的终端组件
        super(bufferLength, 1, 0, ID);
        this.bufferLength = bufferLength;
        this.DataType = "Real"; // 默认显示实部数据
        
        // 初始化内部缓冲区
        refreshComponent();
    }

    /**
     * 示波器是终端组件，不进行计算
     */
    @Override
    public void Calculate() {
        // Do nothing
    }

    /**
     * 当缓冲区长度等参数变化时，重置内部的 ans 数组
     */
    @Override
    public void refreshComponent() {
        // 根据 bufferLength (而不是 blockLength) 来创建内部显示缓冲区
        this.ans = SDRDataUtils.createComplexMatrix(1, this.bufferLength, 0, 0);
    }

    /**
     * 接收数据并更新示波器显示缓冲区的核心方法
     * @param data 新传入的数据块
     * @param index 输入端口索引 (默认为0)
     */
    @Override
    public void setOperationParams(SDRData[] data, int index) {
        if (data == null || data.length == 0) {
            return; // 没有新数据，直接返回
        }

        int newDataLength = data.length;
        int bufferLen = this.bufferLength;
        SDRData[] displayBuffer = this.ans[0]; // 获取显示缓冲区引用

        // 计算需要保留的旧数据点的数量
        int keepCount = bufferLen - newDataLength;

        if (keepCount > 0) {
            // --- 情况1: 新数据块比缓冲区小，进行平移和追加 ---
            
            // 步骤 A: 将缓冲区中需要保留的旧数据向左平移
            // 源: displayBuffer, 起始位置: newDataLength
            // 目标: displayBuffer, 起始位置: 0
            // 长度: keepCount
            System.arraycopy(displayBuffer, newDataLength, displayBuffer, 0, keepCount);

            // 步骤 B: 将新数据追加到缓冲区的末尾
            for (int i = 0; i < newDataLength; i++) {
                int destIndex = keepCount + i;
                copyValue(data[i], displayBuffer[destIndex]);
            }
        } else {
            // --- 情况2: 新数据块大于或等于缓冲区，直接用新数据的末尾部分覆盖整个缓冲区 ---
            
            // 计算从新数据块的哪个位置开始拷贝
            int startOffset = newDataLength - bufferLen;
            for (int i = 0; i < bufferLen; i++) {
                copyValue(data[startOffset + i], displayBuffer[i]);
            }
        }
    }
    
    /**
     * 根据设置的 DataType，从源 SDRData 提取数值并赋给目标 SDRData
     * @param source 源数据
     * @param dest 目标数据（在显示缓冲区中）
     */
    private void copyValue(SDRData source, SDRData dest) {
        switch (this.DataType) {
            case "Imag":
                dest.fromDouble(source.getImag());
                break;
            case "Abs":
                SDRData temp = source.getCopy();
                temp.abs(); // 计算幅度
                dest.fromDouble(temp.getReal());
                break;
            case "Real":
            default:
                dest.fromDouble(source.getReal());
                break;
        }
    }

    @Override
    public Boolean ModifyPropertiesByName(String name, Object value) {
        try {
            switch(name) {
                case "bufferLength":
                    int newLength = Integer.parseInt(value.toString());
                    if (newLength > 0 && this.bufferLength != newLength) {
                        this.bufferLength = newLength;
                        refreshComponent(); // 缓冲区长度变化，需要重建
                    }
                    return true;
                case "DataType":
                    this.DataType = value.toString();
                    return true;
                default:
                    return super.ModifyPropertiesByName(name, value);
            }
        } catch(Exception e) {
             System.err.println("修改属性失败: " + name + ", 值: " + value + ". 错误: " + e.getMessage());
            return false;
        }
    }
}