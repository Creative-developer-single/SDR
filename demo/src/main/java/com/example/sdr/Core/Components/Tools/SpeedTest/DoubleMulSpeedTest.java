package com.example.sdr.Core.Components.Tools.SpeedTest;

public class DoubleMulSpeedTest {

    public static void singleThreadTest(int iterations) {
        double a = 1.000001;
        double b = 1.000002;

        // 用 4 个寄存器独立累积，消除依赖，便于 SIMD
        double r1 = 1.0, r2 = 1.0, r3 = 1.0, r4 = 1.0;

        long startTime = System.nanoTime();

        for (int i = 0; i < iterations; i += 4) {
            r1 *= a * b;
            r2 *= a * b;
            r3 *= a * b;
            r4 *= a * b;
        }

        long endTime = System.nanoTime();

        double elapsedSeconds = (endTime - startTime) / 1e9;
        double totalMulCount = (double) iterations * 2;

        double mulsPerSecond = totalMulCount / elapsedSeconds;

        System.out.printf("Single-thread: %.2f billion mul/s%n", mulsPerSecond / 1e9);

        // 防止优化
        double dummy = r1 + r2 + r3 + r4;
        System.out.println("Dummy result = " + dummy);
    }

    public static void main(String[] args) {
        int iterations = 1_000_000_000;

        System.out.println("======== Double Mul Speed Test V2 ========");

        singleThreadTest(iterations);
    }
}
