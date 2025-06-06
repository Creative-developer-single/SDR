package com.example.sdr.Core.Components.DataType.SDRData;

public class SDRDataUtilsTest {

    public static void main(String[] args) {
        // 1️⃣ 生成一组 SDRData（复杂数）
        int size = 5;
        SDRData[] originalData = SDRDataUtils.createComplexArray(size, 1.23, 4.56);

        // 额外赋值一些不同数据
        for (int i = 0; i < size; i++) {
            originalData[i].setReal(1.0 * i + 0.5);
            originalData[i].setImag(2.0 * i + 0.25);
        }

        System.out.println("=== 原始数据 ===");
        for (SDRData d : originalData) {
            System.out.println(d);
        }

        // 2️⃣ 转换成二进制数组
        byte[] byteArray = SDRDataUtils.toByteArray(originalData);
        System.out.println("\n=== 转换为 ByteArray，长度 = " + byteArray.length + " ===");

        // 3️⃣ 从二进制数组还原
        SDRData[] recoveredData = SDRDataUtils.fromByteArray(byteArray);

        System.out.println("\n=== 还原后的数据 ===");
        for (SDRData d : recoveredData) {
            System.out.println(d);
        }

        // 4️⃣ 验证一致性（逐个比较）
        boolean success = true;
        double eps = 1e-12;  // 允许误差范围，浮点比较需要 tolerance

        for (int i = 0; i < size; i++) {
            double realDiff = Math.abs(originalData[i].getReal() - recoveredData[i].getReal());
            double imagDiff = Math.abs(originalData[i].getImag() - recoveredData[i].getImag());
            if (realDiff > eps || imagDiff > eps) {
                System.out.printf("Mismatch at index %d: realDiff=%.12f, imagDiff=%.12f\n", i, realDiff, imagDiff);
                success = false;
            }
        }

        // 5️⃣ 输出结果
        if (success) {
            System.out.println("\n✅ 测试通过，数据一致！");
        } else {
            System.out.println("\n❌ 测试失败，数据不一致！");
        }
    }
}

