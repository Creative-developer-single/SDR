package com.example.sdr.Core.Components.Tools.SpeedTest;

public class FixedPointMulSpeedTest {

    // Q1.15 fixed-point multiplication
    public static short multiplyQ15(short a, short b) {
        int temp = a * b; // 32-bit result
        return (short)(temp >> 15);
    }

    // Q1.31 fixed-point multiplication
    public static int multiplyQ31(int a, int b) {
        long temp = (long) a * (long) b; // 64-bit result
        return (int)(temp >> 31);
    }

    // Q1.63 fixed-point multiplication (emulated)
    public static long multiplyQ63(long a, long b) {
        // In practice, 128-bit would be needed, here we simulate simple shift
        // Note: This is for benchmarking only, not strictly accurate Q63!
        return (a * b) >> 63;
    }

    // Test function
    public static void runTest(String label, Runnable testBody) {
        System.out.println("==== Testing " + label + " ====");

        long startTime = System.nanoTime();

        testBody.run();

        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1e9;

        System.out.printf("%s: Elapsed %.3f seconds\n\n", label, elapsedSeconds);
        System.out.println("GFlops:" + (1e9 / elapsedSeconds) / 1e9); // Assuming 1 billion operations
    }

    public static void main(String[] args) {
        int iterations = 1_000_000_000; // 10 亿次

        // Test Q1.15
        runTest("Q1.15 (16-bit)", () -> {
            short a = (short)(0.75 * (1 << 15));
            short b = (short)(0.5 * (1 << 15));
            short result = 0;
            for (int i = 0; i < iterations; i++) {
                result = multiplyQ15(a, b);
            }
            System.out.println("Result (Q15) = " + result);
        });

        // Test Q1.31
        runTest("Q1.31 (32-bit)", () -> {
            int a = (int)(0.75 * (1L << 31));
            int b = (int)(0.5 * (1L << 31));
            int result = 0;
            for (int i = 0; i < iterations; i++) {
                result = multiplyQ31(a, b);
            }
            System.out.println("Result (Q31) = " + result);
        });

        // Test Q1.63
        runTest("Q1.63 (64-bit)", () -> {
            long a = (long)(0.75 * (1L << 63));
            long b = (long)(0.5 * (1L << 63));
            long result = 0;
            for (int i = 0; i < iterations; i++) {
                result = multiplyQ63(a, b);
            }
            System.out.println("Result (Q63) = " + result);
        });
    }
}
