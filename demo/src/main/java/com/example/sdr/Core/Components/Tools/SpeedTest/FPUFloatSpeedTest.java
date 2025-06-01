package com.example.sdr.Core.Components.Tools.SpeedTest;

import java.util.Random;

public class FPUFloatSpeedTest {
    public static void main(String[] args) {
        final int iterations = 1_000_000_0; // 10 亿次
        final int runs = 3; // 3 次独立测试

        System.out.println("==== FLOAT MULTIPLY TEST ====");
        runFloatMultiplyTest(iterations, runs);

        System.out.println("==== MATH.FMA TEST ====");
        runMathFmaTest(iterations, runs);

        System.out.println("==== FIXED POINT TEST ====");
        runFixedPointTest(iterations, runs);
    }

    // 1️⃣ 浮点乘法
    static void runFloatMultiplyTest(int iterations, int runs) {
        for (int run = 1; run <= runs; run++) {
            System.out.printf("-- Run %d --%n", run);

            double[] x = new double[iterations];
            double[] y = new double[iterations];
            Random rand = new Random(run * 1000);

            // 生成随机数组
            for (int i = 0; i < iterations; i++) {
                x[i] = rand.nextDouble() * 10.0;
                y[i] = rand.nextDouble() * 10.0;
            }

            // Benchmark
            double sum = 0;
            double z = 0;

            long start = System.nanoTime();

            for (int i = 0; i < iterations; i++) {
                z = x[i] * y[i];
                //sum += z * 1e-6;
            }

            long end = System.nanoTime();
            printResult(iterations, start, end, sum);
        }
    }

    // 2️⃣ Math.fma
    static void runMathFmaTest(int iterations, int runs) {
        for (int run = 1; run <= runs; run++) {
            System.out.printf("-- Run %d --%n", run);

            double[] x = new double[iterations];
            double[] y = new double[iterations];
            Random rand = new Random(run * 2000);

            for (int i = 0; i < iterations; i++) {
                x[i] = rand.nextDouble() * 10.0;
                y[i] = rand.nextDouble() * 10.0;
            }

            double sum = 0;
            double z = 0;

            long start = System.nanoTime();

            for (int i = 0; i < iterations; i++) {
                z = Math.fma(x[i], y[i], sum);
                //sum += z * 1e-6;
            }

            long end = System.nanoTime();
            printResult(iterations, start, end, sum);
        }
    }

    // 3️⃣ 定点数乘法
    static void runFixedPointTest(int iterations, int runs) {
        final int decimalPlaces = 6;
        final long scale = (long) Math.pow(10, decimalPlaces);

        for (int run = 1; run <= runs; run++) {
            System.out.printf("-- Run %d --%n", run);

            long[] x_raw = new long[iterations];
            long[] y_raw = new long[iterations];
            Random rand = new Random(run * 3000);

            for (int i = 0; i < iterations; i++) {
                double x_real = rand.nextDouble() * 10.0;
                double y_real = rand.nextDouble() * 10.0;

                x_raw[i] = Math.round(x_real * scale);
                y_raw[i] = Math.round(y_real * scale);
            }

            long sum = 0;
            long z = 0;

            long start = System.nanoTime();

            for (int i = 0; i < iterations; i++) {
                z = (x_raw[i] * y_raw[i]);
                //sum += z;
            }

            long end = System.nanoTime();
            printResult(iterations, start, end, sum);
        }
    }

    // 通用结果打印
    static void printResult(int iterations, long start, long end, double sum) {
        double elapsedSeconds = (end - start) / 1_000_000_000.0;
        double billionOpsPerSec = (iterations / 1_000_000_000.0) / elapsedSeconds;

        System.out.printf("Elapsed time: %.3f seconds%n", elapsedSeconds);
        System.out.printf("Throughput: %.3f billion ops/sec%n", billionOpsPerSec);
        System.out.println("Final accumulated result: " + sum);
    }

    // Overload for long sum (fixed point)
    static void printResult(int iterations, long start, long end, long sum) {
        double elapsedSeconds = (end - start) / 1_000_000_000.0;
        double billionOpsPerSec = (iterations / 1_000_000_000.0) / elapsedSeconds;

        System.out.printf("Elapsed time: %.3f seconds%n", elapsedSeconds);
        System.out.printf("Throughput: %.3f billion ops/sec%n", billionOpsPerSec);
        System.out.println("Final accumulated result: " + sum);
    }
}
