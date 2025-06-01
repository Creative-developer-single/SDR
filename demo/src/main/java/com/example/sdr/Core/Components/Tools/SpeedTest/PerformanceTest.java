package com.example.sdr.Core.Components.Tools.SpeedTest;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import java.util.Random;
// import java.util.concurrent.TimeUnit; // Not strictly needed for System.nanoTime output formatting

public class PerformanceTest {

    private static final int ARRAY_SIZE = 1_000_000; // 数组大小
    private static final int ITERATIONS = 1000;     // 测试迭代次数
    private static final double MAX_DOUBLE_VALUE = 1000.0; // 用于生成随机数的最大绝对值
    private static final long FIXED_POINT_SCALE_FACTOR = 1L << 20; // 定点数缩放因子

    private static double[] doubleArray1;
    private static double[] doubleArray2;
    private static double[] doubleArray3; // For FMA's third argument

    private static long[] fixedPointArray1;
    private static long[] fixedPointArray2;

    // 使用volatile防止死代码消除
    private static volatile double doubleResultSink = 0.0;
    private static volatile long longResultSink = 0L;

    public static void setup() {
        Random random = new Random();

        doubleArray1 = new double[ARRAY_SIZE];
        doubleArray2 = new double[ARRAY_SIZE];
        doubleArray3 = new double[ARRAY_SIZE];

        fixedPointArray1 = new long[ARRAY_SIZE];
        fixedPointArray2 = new long[ARRAY_SIZE];

        for (int i = 0; i < ARRAY_SIZE; i++) {
            doubleArray1[i] = (random.nextDouble() - 0.5) * 2 * MAX_DOUBLE_VALUE;
            doubleArray2[i] = (random.nextDouble() - 0.5) * 2 * MAX_DOUBLE_VALUE;
            doubleArray3[i] = (random.nextDouble() - 0.5) * 2 * MAX_DOUBLE_VALUE;

            fixedPointArray1[i] = (long) (doubleArray1[i] * FIXED_POINT_SCALE_FACTOR);
            fixedPointArray2[i] = (long) (doubleArray2[i] * FIXED_POINT_SCALE_FACTOR);
        }
    }

    public static void benchmarkDoubleMultiply() {
        double sum = 0.0;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            sum += doubleArray1[i] * doubleArray2[i]; // 1 FLOP
        }
        doubleResultSink += sum;
    }

    public static void benchmarkMathFMA() {
        double sum = 0.0;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            sum += Math.fma(doubleArray1[i], doubleArray2[i], doubleArray3[i]); // 2 FLOPs
        }
        doubleResultSink += sum;
    }

    public static void benchmarkFixedPointMultiply() {
        long sum = 0L;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            sum += fixedPointArray1[i] * fixedPointArray2[i]; // 1 Integer Operation
        }
        longResultSink += sum;
    }

    public static void main(String[] args) {
        System.out.println("Setting up test data...");
        setup();
        System.out.println("Test data ready. Array size: " + ARRAY_SIZE + ", Iterations per test: " + ITERATIONS);
        System.out.println("Fixed-point scale factor: 2^" + Long.numberOfTrailingZeros(FIXED_POINT_SCALE_FACTOR) + " (" + FIXED_POINT_SCALE_FACTOR + ")");
        System.out.println("Max absolute double value for data generation: " + MAX_DOUBLE_VALUE);
        System.out.println("---------------------------------------------------------");

        long totalTimeDoubleMultiply = 0;
        long totalTimeMathFMA = 0;
        long totalTimeFixedPointMultiply = 0;

        System.out.println("Warming up JIT...");
        int warmupIterations = ITERATIONS / 2 > 0 ? ITERATIONS / 2 : 1; // Ensure at least 1 warmup iteration
        for (int i = 0; i < warmupIterations; i++) {
            benchmarkDoubleMultiply();
            benchmarkMathFMA();
            benchmarkFixedPointMultiply();
        }
        doubleResultSink = 0.0;
        longResultSink = 0L;
        System.out.println("Warm-up complete.");
        System.out.println("---------------------------------------------------------");
        System.out.println("Running benchmarks...");

        // Test Double Multiply
        for (int i = 0; i < ITERATIONS; i++) {
            long startTime = System.nanoTime();
            benchmarkDoubleMultiply();
            totalTimeDoubleMultiply += (System.nanoTime() - startTime);
        }
        double gflopsDoubleMultiply = (1.0 * ARRAY_SIZE * ITERATIONS) / totalTimeDoubleMultiply;
        System.out.printf("1. Double Multiply (x * y):         %.3f GFLOPS%n", gflopsDoubleMultiply);

        // Test Math.fma
        for (int i = 0; i < ITERATIONS; i++) {
            long startTime = System.nanoTime();
            benchmarkMathFMA();
            totalTimeMathFMA += (System.nanoTime() - startTime);
        }
        // Math.fma is 2 FLOPs per element (multiply + add)
        double gflopsMathFMA = (2.0 * ARRAY_SIZE * ITERATIONS) / totalTimeMathFMA;
        System.out.printf("2. Math.fma(x, y, z):             %.3f GFLOPS%n", gflopsMathFMA);

        // Test Fixed-Point Multiply
        for (int i = 0; i < ITERATIONS; i++) {
            long startTime = System.nanoTime();
            benchmarkFixedPointMultiply();
            totalTimeFixedPointMultiply += (System.nanoTime() - startTime);
        }
        // Counting each long multiplication as 1 "operation" for GigaOps comparison
        double gigaOpsFixedPoint = (1.0 * ARRAY_SIZE * ITERATIONS) / totalTimeFixedPointMultiply;
        System.out.printf("3. Fixed-Point Multiply (x * y):  %.3f GigaOps/s (Integer Ops)%n", gigaOpsFixedPoint);
        
        System.out.println("---------------------------------------------------------");
        System.out.println("Note: For Fixed-Point Multiply, GigaOps/s represents Giga Integer Operations Per Second.");
        System.out.println("Result sink (double): " + String.format("%.5e", doubleResultSink)); // Using scientific notation for potentially large sums
        System.out.println("Result sink (long): " + longResultSink);
        System.out.println("Benchmarking complete.");
    }
}