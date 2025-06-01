package com.example.sdr.Core.Components.Tools.SpeedTest;

import java.util.Random;

public class PerformanceTestGFLOPSWithFloat {

    private static final int ARRAY_SIZE = 1_000_000; // 数组大小
    private static final int ITERATIONS = 100;     // 测试迭代次数
    private static final double MAX_VALUE_FOR_GENERATION = 1000.0; // 用于生成随机数的最大绝对值 (double 和 float 共用)
    private static final long FIXED_POINT_SCALE_FACTOR = 1L << 20; // 定点数缩放因子

    private static double[] doubleArray1;
    private static double[] doubleArray2;
    private static double[] doubleArray3; // For Double FMA's third argument

    private static float[] floatArray1;
    private static float[] floatArray2;
    private static float[] floatArray3;  // For Float Multiply-Add's third argument

    private static long[] fixedPointArray1;
    private static long[] fixedPointArray2;

    // 使用volatile防止死代码消除
    private static volatile double doubleResultSink = 0.0; // Also for float results (cast to double)
    private static volatile long longResultSink = 0L;

    public static void setup() {
        Random random = new Random();

        doubleArray1 = new double[ARRAY_SIZE];
        doubleArray2 = new double[ARRAY_SIZE];
        doubleArray3 = new double[ARRAY_SIZE];

        floatArray1 = new float[ARRAY_SIZE];
        floatArray2 = new float[ARRAY_SIZE];
        floatArray3 = new float[ARRAY_SIZE];

        fixedPointArray1 = new long[ARRAY_SIZE];
        fixedPointArray2 = new long[ARRAY_SIZE];

        for (int i = 0; i < ARRAY_SIZE; i++) {
            // Generate doubles first
            double d1 = (random.nextDouble() - 0.5) * 2 * MAX_VALUE_FOR_GENERATION;
            double d2 = (random.nextDouble() - 0.5) * 2 * MAX_VALUE_FOR_GENERATION;
            double d3 = (random.nextDouble() - 0.5) * 2 * MAX_VALUE_FOR_GENERATION;

            doubleArray1[i] = d1;
            doubleArray2[i] = d2;
            doubleArray3[i] = d3;

            // Cast to float for float arrays
            floatArray1[i] = (float) d1;
            floatArray2[i] = (float) d2;
            floatArray3[i] = (float) d3;

            fixedPointArray1[i] = (long) (d1 * FIXED_POINT_SCALE_FACTOR);
            fixedPointArray2[i] = (long) (d2 * FIXED_POINT_SCALE_FACTOR);
        }
    }

    // --- Double Precision Benchmarks ---
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

    // --- Single Precision (Float) Benchmarks ---
    public static void benchmarkFloatMultiply() {
        float sum = 0.0f;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            sum += floatArray1[i] * floatArray2[i]; // 1 FLOP
        }
        doubleResultSink += sum; // Promote to double for sink
    }

    public static void benchmarkFloatMultiplyAdd() {
        float sum = 0.0f;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            sum += floatArray1[i] * floatArray2[i] + floatArray3[i]; // 2 FLOPs (1 mult, 1 add)
        }
        doubleResultSink += sum; // Promote to double for sink
    }


    // --- Fixed-Point Benchmark ---
    public static void benchmarkFixedPointMultiply() {
        long sum = 0L;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            sum += fixedPointArray1[i] * fixedPointArray2[i]; // 1 Integer Operation
        }
        longResultSink += sum;
    }

    public static void main(String[] args) {
        System.out.println("Data Range Info:");
        System.out.printf("  Float:  Size=%d bits, MinNormal=%.2e, MaxValue=%.2e, Precision ~6-9 digits%n",
                Float.SIZE, Float.MIN_NORMAL, Float.MAX_VALUE);
        System.out.printf("  Double: Size=%d bits, MinNormal=%.2e, MaxValue=%.2e, Precision ~15-17 digits%n",
                Double.SIZE, Double.MIN_NORMAL, Double.MAX_VALUE);
        System.out.println("---------------------------------------------------------");

        System.out.println("Setting up test data...");
        setup();
        System.out.println("Test data ready. Array size: " + ARRAY_SIZE + ", Iterations per test: " + ITERATIONS);
        System.out.println("Max absolute value for data generation: " + MAX_VALUE_FOR_GENERATION);
        System.out.println("---------------------------------------------------------");

        long totalTimeDoubleMultiply = 0;
        long totalTimeMathFMA = 0;
        long totalTimeFloatMultiply = 0;
        long totalTimeFloatMultiplyAdd = 0;
        long totalTimeFixedPointMultiply = 0;

        System.out.println("Warming up JIT...");
        int warmupIterations = ITERATIONS / 2 > 0 ? ITERATIONS / 2 : 1;
        for (int i = 0; i < warmupIterations; i++) {
            benchmarkDoubleMultiply();
            benchmarkMathFMA();
            benchmarkFloatMultiply();
            benchmarkFloatMultiplyAdd();
            benchmarkFixedPointMultiply();
        }
        doubleResultSink = 0.0;
        longResultSink = 0L;
        System.out.println("Warm-up complete.");
        System.out.println("---------------------------------------------------------");
        System.out.println("Running benchmarks (GFLOPS/GigaOps calculations are per iteration total time):");

        // Test Double Multiply
        for (int i = 0; i < ITERATIONS; i++) {
            long startTime = System.nanoTime();
            benchmarkDoubleMultiply();
            totalTimeDoubleMultiply += (System.nanoTime() - startTime);
        }
        double gflopsDoubleMultiply = (1.0 * ARRAY_SIZE * ITERATIONS) / totalTimeDoubleMultiply;
        System.out.printf("1. Double Multiply (x * y):         %.3f GFLOPS%n", gflopsDoubleMultiply);

        // Test Math.fma (Double)
        for (int i = 0; i < ITERATIONS; i++) {
            long startTime = System.nanoTime();
            benchmarkMathFMA();
            totalTimeMathFMA += (System.nanoTime() - startTime);
        }
        double gflopsMathFMA = (2.0 * ARRAY_SIZE * ITERATIONS) / totalTimeMathFMA;
        System.out.printf("2. Double FMA (Math.fma(x,y,z)):    %.3f GFLOPS%n", gflopsMathFMA);

        // Test Float Multiply
        for (int i = 0; i < ITERATIONS; i++) {
            long startTime = System.nanoTime();
            benchmarkFloatMultiply();
            totalTimeFloatMultiply += (System.nanoTime() - startTime);
        }
        double gflopsFloatMultiply = (1.0 * ARRAY_SIZE * ITERATIONS) / totalTimeFloatMultiply;
        System.out.printf("3. Float Multiply (x * y):          %.3f GFLOPS%n", gflopsFloatMultiply);

        // Test Float Multiply-Add
        for (int i = 0; i < ITERATIONS; i++) {
            long startTime = System.nanoTime();
            benchmarkFloatMultiplyAdd();
            totalTimeFloatMultiplyAdd += (System.nanoTime() - startTime);
        }
        double gflopsFloatMultiplyAdd = (2.0 * ARRAY_SIZE * ITERATIONS) / totalTimeFloatMultiplyAdd; // 2 FLOPs
        System.out.printf("4. Float Multiply-Add (x*y + z):    %.3f GFLOPS%n", gflopsFloatMultiplyAdd);

        // Test Fixed-Point Multiply
        for (int i = 0; i < ITERATIONS; i++) {
            long startTime = System.nanoTime();
            benchmarkFixedPointMultiply();
            totalTimeFixedPointMultiply += (System.nanoTime() - startTime);
        }
        double gigaOpsFixedPoint = (1.0 * ARRAY_SIZE * ITERATIONS) / totalTimeFixedPointMultiply;
        System.out.printf("5. Fixed-Point Multiply (x * y):  %.3f GigaOps/s (Integer Ops)%n", gigaOpsFixedPoint);
        
        System.out.println("---------------------------------------------------------");
        System.out.println("Note: For Fixed-Point Multiply, GigaOps/s represents Giga Integer Operations Per Second.");
        System.out.println("Result sink (double): " + String.format("%.5e", doubleResultSink));
        System.out.println("Result sink (long): " + longResultSink);
        System.out.println("Benchmarking complete.");
    }
}
