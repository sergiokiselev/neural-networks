package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Lab4 {

    private static int INPUT_LAYER_SIZE = 5;
    private static int HIDDEN_LAYER_SIZE = 7;
    private static int OUTPUT_LAYER_SIZE = 1;

    private static final int TRAIN_INPUTS_COUNT = (int) Math.pow(2, INPUT_LAYER_SIZE);
    private static final double EPSILON = 0.0001;

    private static double[][] Wij = new double[INPUT_LAYER_SIZE][HIDDEN_LAYER_SIZE];
    private static double[][] Wjk = new double[HIDDEN_LAYER_SIZE][OUTPUT_LAYER_SIZE];
    private static double[] Ti = new double[HIDDEN_LAYER_SIZE];
    private static double[] Tj = new double[OUTPUT_LAYER_SIZE];
    private static List<List<Double>> inputs = new ArrayList<>();
    private static List<List<Double>> outputs = new ArrayList<>();


    public static void createInputs(int index, List<Double> vector, int val) {
        if (index < INPUT_LAYER_SIZE) {
            index++;
            vector.add((double) 0);
            List<Double> list = new ArrayList<>();
            list.addAll(vector);
            createInputs(index, list, val);
            vector.remove(vector.size() - 1);
            vector.add((double) val);
            list = new ArrayList<>();
            list.addAll(vector);
            createInputs(index, list, val);
        } else {
            List<Double> list = new ArrayList<>();
            list.addAll(vector);
            inputs.add(list);
        }
    }

    public static void main(String[] args) throws IOException {
        createInputs(0, new ArrayList<Double>(), 1);
        for (int j = 0; j < TRAIN_INPUTS_COUNT; j++) {
            outputs.add(getXor(inputs.get(j)));
            System.out.println("input " + inputs.get(j) + " out " + outputs.get(j).get(0));
        }
        initHiddenLayerWeights();
        initOutputLayerWeights();
        trainNetwork();
        testNetwork();
    }

    private static void testNetwork() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter your inputs:");
            try {
                List<Double> inputVector = new ArrayList<>();
                for (int i = 0; i < INPUT_LAYER_SIZE; i++) {
                    inputVector.add(sc.nextDouble());
                }
                System.out.println("Result: " + activate(Tj, Wjk, activate(Ti, Wij, inputVector, INPUT_LAYER_SIZE, HIDDEN_LAYER_SIZE), HIDDEN_LAYER_SIZE, OUTPUT_LAYER_SIZE).get(0));
            } catch (InputMismatchException ex) {
                break;
            }
        }
    }

    private static void trainNetwork() {
        int epoch = 0;
        double error = Double.MAX_VALUE;
        while (error > EPSILON) {
            epoch++;
            error = 0;
            for (int t = 0; t < TRAIN_INPUTS_COUNT; t++) {
                double alpha = getAlpha(inputs.get(t));
                List<Double> G = activate(Ti, Wij, inputs.get(t), INPUT_LAYER_SIZE, HIDDEN_LAYER_SIZE);
                List<Double> Y = activate(Tj, Wjk, G, HIDDEN_LAYER_SIZE, OUTPUT_LAYER_SIZE);

                List<Double> outputErrors = new ArrayList<>();
                List<Double> hiddenErrors = new ArrayList<>();
                for (int k = 0; k < OUTPUT_LAYER_SIZE; k++) {
                    double dk = Y.get(k) - outputs.get(t).get(k);
                    outputErrors.add(dk);
                    error += (Math.pow(dk, 2) / 2);
                }
                for (int k = 0; k < HIDDEN_LAYER_SIZE; k++) {
                    double ej = 0;
                    for (int j = 0; j < OUTPUT_LAYER_SIZE; j++) {
                        ej += outputErrors.get(j) * Y.get(j) * (1 - Y.get(j)) * Wjk[k][j];
                    }
                    hiddenErrors.add(ej);
                }
                for (int k = 0; k < OUTPUT_LAYER_SIZE; k++) {
                    for (int j = 0; j < HIDDEN_LAYER_SIZE; j++) {

                        Wjk[j][k] = Wjk[j][k] - alpha * outputErrors.get(k) * Y.get(k) * (1 - Y.get(k)) * G.get(j);
                    }
                    Tj[k] += alpha * outputErrors.get(k) * Y.get(k) * (1 - Y.get(k));
                }

                for (int j = 0; j < HIDDEN_LAYER_SIZE; j++) {
                    double distance = hiddenErrors.get(j);
                    for (int i = 0; i < INPUT_LAYER_SIZE; i++) {

                        Wij[i][j] = Wij[i][j] - alpha * distance * G.get(j) * (1 - G.get(j)) * inputs.get(t).get(i);
                    }
                    Ti[j] += alpha * distance * G.get(j) * (1 - G.get(j));
                }
            }
            if (epoch % 1000 == 0) {
                System.out.println("Error = " + error + ", epoch = " + epoch);
            }
        }
        System.out.println("Error = " + error + ", epoch = " + epoch);
    }

    private static void initOutputLayerWeights() {
        Tj[0] = (2 * Math.random() - 1);
        for (int i = 0; i < HIDDEN_LAYER_SIZE; i++) {
            Wjk[i][0] = (2 * Math.random() - 1);
        }
    }

    private static void initHiddenLayerWeights() {
        for (int i = 0; i < HIDDEN_LAYER_SIZE; i++) {
            Ti[i] = (2 * Math.random() - 1);
            for (int k = 0; k < INPUT_LAYER_SIZE; k++) {
                if (Wij[k] == null) {
                    Wij[k] = new double[i];
                }
                Wij[k][i] = (2 * Math.random() - 1);
            }
        }
    }

    public static List<Double> activate(double[] T, double[][] weights, List<Double> inputs, int firstLength, int secondLength) {
        List<Double> sList = new ArrayList<>();
        for (int i = 0; i < secondLength; i++) {
            double S = -T[i];
            for (int j = 0; j < firstLength; j++) {
                S += weights[j][i] * inputs.get(j);
            }
            sList.add(actFunSigm(S));
        }
        return sList;
    }

    public static Double actFunSigm(Double S) {
        return 1.0 / (1.0 + Math.exp(-S));
    }

    public static double getAlpha(List<Double> inputs) {
        double sum = 0.0;
        for (Double input : inputs) {
            sum += input * input;
        }
        return 1.0 / (1.0 + sum);
    }

    private static List<Double> getXor(List<Double> input) {
        long bits = Double.doubleToRawLongBits(input.get(0));
        for (int i = 1; i < INPUT_LAYER_SIZE; i++) {
            bits ^= Double.doubleToRawLongBits(input.get(i));
        }
        List<Double> res = new ArrayList<>();
        res.add(Double.longBitsToDouble(bits));
        return res;
    }

}