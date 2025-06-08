package com.example.sdr.Core.ProjectManager.Components.Visual;

import com.example.sdr.Core.Components.DataType.SDRData.SDRData;
import com.example.sdr.Core.Components.DataType.SDRData.SDRDataUtils;
import com.example.sdr.Core.ProjectManager.Components.Base.BaseComponent;

/**
 * 星座图可视化组件 (具有记忆和滚动功能)
 * <p>
 * 该组件用于在二维平面上显示信号的 I/Q 关系。
 * 它维护一个固定长度的内部缓冲区，用于存储星座点。
 * 每次接收到新的数据块时，它会将旧数据点丢弃，并将新数据点追加到缓冲区中，
 * 从而实现星座图点的持续更新。
 * </p>
 */
public class ConstellationDiagram extends BaseComponent {

    /**
     * 星座图内部显示缓冲区的长度（即显示点的数量）
     */
    private int bufferLength;

    public ConstellationDiagram(int blockLength,int inputCount, int outputCount, String ID) {
        // 输入1路，输出1路（输出供前端获取数据）
        super(blockLength, 1, 1, ID);
        
        // 初始化内部缓冲区
        refreshComponent();
    }

    /**
     * 构造函数
     * @param bufferLength 星座图要显示的总点数
     * @param ID 组件的唯一标识符
     */
    public ConstellationDiagram(int bufferLength, String ID) {
        // blockLength 参数在这里可以与 bufferLength 保持一致，以初始化父类
        // 输入1路，输出1路（输出供前端获取数据）
        super(bufferLength, 1, 1, ID);
        this.bufferLength = bufferLength;
        
        // 初始化内部缓冲区
        refreshComponent();
    }

    /**
     * 重写 Calculate 方法，执行滚动缓冲区的更新操作
     */
    @Override
    public void Calculate() {
        // 检查输入数据是否存在
        if (op_in == null || op_in[0] == null || op_in[0].length == 0) {
            return;
        }

        int newDataLength = op_in[0].length;
        int bufferLen = this.bufferLength;
        SDRData[] displayBuffer = this.ans[0]; // 获取显示缓冲区引用

        // 计算需要保留的旧数据点的数量
        int keepCount = bufferLen - newDataLength;

        if (keepCount > 0) {
            // --- 情况1: 新数据块比缓冲区小，进行平移和追加 ---
            
            // 步骤 A: 将缓冲区中需要保留的旧数据点向左平移
            System.arraycopy(displayBuffer, newDataLength, displayBuffer, 0, keepCount);

            // 步骤 B: 将新数据点追加到缓冲区的末尾
            for (int i = 0; i < newDataLength; i++) {
                int destIndex = keepCount + i;
                displayBuffer[destIndex].Copy(op_in[0][i]); // 直接拷贝完整的SDRData对象
            }
        } else {
            // --- 情况2: 新数据块大于或等于缓冲区，直接用新数据的末尾部分覆盖整个缓冲区 ---
            
            // 计算从新数据块的哪个位置开始拷贝
            int startOffset = newDataLength - bufferLen;
            for (int i = 0; i < bufferLen; i++) {
                displayBuffer[i].Copy(op_in[0][startOffset + i]);
            }
        }
    }
    
    /**
     * 当缓冲区长度等参数变化时，重置内部的 ans 数组
     */
    @Override
    public void refreshComponent() {
        // 根据 bufferLength 来创建内部显示缓冲区
        this.ans = SDRDataUtils.createComplexMatrix(1, this.bufferLength, 0, 0);
    }
    
    @Override
    public Boolean ModifyPropertiesByName(String name, Object value) {
        try {
            if ("bufferLength".equals(name)) {
                int newLength = Integer.parseInt(value.toString());
                if (newLength > 0 && this.bufferLength != newLength) {
                    this.bufferLength = newLength;
                    refreshComponent(); // 缓冲区长度变化，需要重建
                }
                return true;
            }
            return super.ModifyPropertiesByName(name, value);
        } catch(Exception e) {
             System.err.println("修改属性失败: " + name + ", 值: " + value + ". 错误: " + e.getMessage());
            return false;
        }
    }
}