package com.company;

import java.util.Random;

public class Lab3 {

    private static double[][] getTruthTable(int n, double top) {
        double[][] points = new double[32][n];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < n; j++) {
                points[i][j] = (i >> j & 1) * top;
            }
        }
        return points;
    }

    private static double[] getRandomWeights(int n) {
        Random r = new Random();
        double[] w = new double[n];
        for (int i = 0; i < w.length; i++) {
            w[i] = r.nextDouble();
        }
        return w;
    }

    private static double weightedSum(double[] x, double[] w, double t){
        double sum = 0;
        for (int i = 0; i < x.length; i++) {
            sum += x[i] * w[i];
        }
        return sum - t;
    }

    private static double f(double sum){
        return sum > 0 ? 1.0 : 0.0;
    }

    private static double sample(double[] x){
        for (double d : x) {
            if(d <= 0) return 0;
        }
        return 1;
    }

    private static double getNewSpeed(double[] x){
        double newSpeed = 1;
        for (double aX : x) {
            newSpeed += Math.pow(aX, 2);
        }
        return 1/newSpeed;
    }

    private static double[] getNewWeights(double[] x, double[] w, double diff){
        double[] newW = new double[w.length];
        for (int i = 0; i < w.length; i++) {
            newW[i] = w[i] - diff*x[i];
        }
        return newW;
    }

    private static double getNewThreshold(double t, double diff){
        return t + diff;
    }

    public static void main(String[] args) {
        int n = 5;
        double learnigSpeed = 1.0;
        double top = 4;
        double t = new Random().nextDouble() * top - 2;
        double[] w = getRandomWeights(n);
        double[][] points = getTruthTable(n, top);
        for (double[] i : points) {
            for (double j : i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }

        System.out.print("sum =");
        for (int i = 0; i < w.length; i++) {
            System.out.printf(" %6.3f*x%d +", w[i], i + 1);
        }
        System.out.printf("- %4.2f\n", t);

        for (int j = 1;; j++) {
            boolean check = true;
            for (int i = 0; i < 32; i++) {
                double[] x = points[i];
                double sum = weightedSum(x, w, t),
                        err = f(sum) - sample(x),
                        diff = learnigSpeed* err;
                w = getNewWeights(x, w, diff);
                t = getNewThreshold(t, diff);
                learnigSpeed = getNewSpeed(x);
                System.out.printf("alpha = %6.4f, sum = %6.3f, error = %4.1f, t = %4.2f\n", learnigSpeed, sum, err, t);
                check &= err == 0.0;
            }
            System.out.println("========= " + j);
            if (check)
                break;
        }
        System.out.print("sum =");
        for (int i = 0; i < w.length; i++) {
            System.out.printf(" %6.3f*x%d +", w[i], i + 1);
        }
        System.out.printf("- %4.2f\n", t);
        System.out.println(f(weightedSum(new double[] {3.8, 3.8, 3.8, 3.8, 3.8}, w, top)));
    }

}