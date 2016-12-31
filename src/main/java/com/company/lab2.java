package com.company;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * User: NotePad.by
 * Date: 5/23/2016.
 */
public class lab2 {

    private static double[] W =  new double[3];
    private static double T;
    private static double A = 0.001;
    private static final int m = 20;
    private static final int n = 50;
    private static final double delta = 0.5;

    private static double[][] trainInputs = new double[m][3];
    private static double[] trainOutputs = new double[m];
    private static double[] etalonOutputs = new double[n];

    public static void main(String[] args) {
        getTrainVector();
        initVectors();
        train();
        System.out.println("\n\nResult");
        System.out.println(W[0]);
        System.out.println(W[1]);
        System.out.println(W[2]);
        System.out.println(T);

        double[] expect = new double[n + 3];
        expect[0] = trainInputs[0][0];
        expect[1] = trainInputs[0][1];
        expect[2] = trainInputs[0][2];
        for (int i = 0; i < n; i++) {
            double predicted = getS(new double[] {expect[i], expect[i+ 1], expect[i + 2]});
            expect[i + 3] = predicted;
            System.out.print(predicted + ", ");
        }
    }

    private static double getEtalonFunc(double x) {
        double a = 2;
        double b = 1;
        double d = 0;
        return a * Math.sin(b * x) + d;
    }

    private static void getTrainVector() {
        double x0 = 0;
        for (int i = 0; i < n; i++) {
            etalonOutputs[i] = getEtalonFunc(x0);
            x0 += delta;
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 3; j++) {
                trainInputs[i][j] = etalonOutputs[i + j];
                System.out.print(trainInputs[i][j] + " ");
            }
            System.out.println();
            trainOutputs[i] = etalonOutputs[i + 3];
            System.out.println("Outputs " + trainOutputs[i]);
        }
    }

    private static void initVectors() {
        for (int i = 0; i < 3; i++) {
            W[i] = Math.random();
        }
        T = Math.random() / 2;

        System.out.println("W0 = " + W[0]);
        System.out.println("W1 = " + W[1]);
        System.out.println("W2 = " + W[2]);
        System.out.println("T = " + T);
    }

    private static double train() {
        double[] results = new double[m];
        int count = 0;
        boolean stop;
        do {
            count++;
            stop = true;
//                            double res = 0;
//                for (int i = 0; i < m; i++) {
//                    res += Math.pow(trainOutputs[i], 2);
//                }
//                A = 1 / (1 + res);
//            System.out.println("A: " + A);
            double currError = 0;
            for (int k = 0; k < m; k++) {
                double y = 0;
                for (int j = 0; j < W.length; j++) {
                    W[j] = W[j] - A * trainInputs[k][j] * (results[k] - trainOutputs[k]);
                    y += W[j] * trainInputs[k][j];
                }
                y -= T;
                T += A * (results[k] - trainOutputs[k]);

                currError += Math.pow(y - trainOutputs[k], 2) / 2;
            }
            if (currError < 0.0000001){
                System.out.println("Learning ended after " + (count + 1) + " epoches with MSE=" + currError);
                break;
            }

            for (int k = 0; k < m; k++) {
                results[k] = getF(trainInputs[k]);
            }

            for (int i = 0; i < trainOutputs.length; i++) {
                System.out.println("======================================");
                System.out.println(" - expected: " + trainOutputs[i] +
                        "\n - actual: " + results[i] +
                "\n - error: " + (0.5 * Math.pow(results[i] - trainOutputs[i], 2)));
                System.out.println("======================================");
                if(0.5 * Math.pow(results[i] - trainOutputs[i], 2) > 0.0000001) {
                    stop = false;
                }
            }

            System.out.println("Weights: " + W[0] + " " + W[1] + " " + W[2] + " T: " + T);
        } while (!stop);
        System.out.println("Epochs: " + count);
        return 0;
    }

    private static double getF(double[] input) {
        return getS(input);
    }

    private static double getS(double[] input) {
        double sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += input[i] * W[i];
        }
        sum -= T;
        return sum;
    }
}
