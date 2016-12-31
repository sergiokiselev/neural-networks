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
public class Lab1 extends Application {

    private boolean[][] filled = new boolean[7][7];

    private static double[][] inputs = {
            {0.45, .43},
            {-9,1},
            {-.5,-1},
            {-.6, .6},
            {1, 1},
            {2,2},
            {-2,-2},
            {-9,1},
            {9, -1},
            {1, 9},
            {-1, -9}
    };


    private static double[] X = new double[2];
    private static double[] W =  new double[2];
    private static double T;
    private static double A = 0.1;

    private static double[][] trainInputs = {{-9,1}, {9, -1}, {1, 9}, {-1, -9}};
    private static double[] trainOutputs = {0, 0, 1, 0};

    private static void initVectors() {
        for (int i = 0; i < 2; i++) {
            W[i] = Math.random();
        }
        T = Math.random() / 2;

        System.out.println("W0 = " + W[0]);
        System.out.println("W1 = " + W[1]);
        System.out.println("T = " + T);
    }

    private static double train() {
        double[] results = new double[4];
        int count = 0;
        boolean stop;
            do {
                count++;
                stop = true;
                for (int k = 0; k < 4; k++) {
                    results[k] = getF(trainInputs[k]);
                }
                //System.out.println("res" + result);
                //System.out.println("expec" + expectedResult);

                for (int i = 0; i < trainOutputs.length; i++) {
                    System.out.println("expected: " + trainOutputs[i] + " - actual: " + results[i]);
                    if(results[i] != trainOutputs[i]) {
                        stop = false;
                    }
                    if (count == 10)
                        stop = true;
                }

                for (int k = 0; k < 4; k++) {
                    for (int j = 0; j < W.length; j++) {
                        W[j] = W[j] - A * trainInputs[k][j] * (results[k] - trainOutputs[k]);
                        T = T + A * (results[k] - trainOutputs[k]);
                    }
                }
                System.out.println("Weight");
                System.out.println(W[0]);
                System.out.println(W[1]);
                System.out.println("T: " + T);
            } while (!stop);
        System.out.println("Epochs: " + count);
        return 0;
    }

    private static double getF(double[] input) {
        double s = getS(input);
        if (s > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private static double getS(double[] input) {
        double sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += input[i] * W[i];
        }
        sum -= T;
        return sum;
    }

    public static void main(String[] args) {
        //Application.launch(DrawPict.class);
        initVectors();
        train();
        System.out.println("\n\nResult");
        System.out.println(W[0]);
        System.out.println(W[1]);
        System.out.println(T);

        for (double[] in : inputs) {
            for (double a : in) {
                System.out.print(a + " ");
            }
            System.out.println(" = " + getF(in));
        }

        double k = -W[0] / W[1];
        double b = T / W[1];

        System.out.println(k + " * x + " + b);
        try(PrintWriter writer = new PrintWriter("output.txt")) {
            writer.write("" + k + " " + b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {

        primaryStage.setTitle("Drawing Operations Test");
        final Group root = new Group();
        final Canvas canvas = new Canvas(600, 400);
        final GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.strokeLine(20, 20, 20, 140);
        gc.strokeLine(20, 20, 140, 20);
        gc.strokeLine(140, 20, 140, 140);
        gc.strokeLine(20, 140, 140, 140);
        final Label label2 = new Label();
        label2.setLayoutX(20);
        label2.setLayoutY(200);
        label2.setText("2");
        root.getChildren().add(label2);
        final Label label3 = new Label();
        label3.setLayoutX(20);
        label3.setLayoutY(220);
        label3.setText("3");
        root.getChildren().add(label3);
        final Label label4 = new Label();
        label4.setLayoutX(20);
        label4.setLayoutY(240);
        label4.setText("4");
        root.getChildren().add(label4);
        final Label label5 = new Label();
        label5.setLayoutX(20);
        label5.setLayoutY(260);
        label5.setText("5");
        root.getChildren().add(label5);
        final Label label7 = new Label();
        label7.setLayoutX(20);
        label7.setLayoutY(280);
        label7.setText("7");
        root.getChildren().add(label7);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {

                    public void handle(MouseEvent t) {
                        double startX = Math.ceil(t.getX() / 20);
                        double startY = Math.ceil(t.getY() / 20);
                        if (startX > 1 && startX < 8 && startY > 1 && startY < 8) {
                            if (filled[(int) startY - 1][(int) startX - 1]) {
                                gc.clearRect(startX * 20 - 20, startY * 20 - 20, 20, 20);
                            } else {
                                gc.fillRect(startX * 20 - 20, startY * 20 - 20, 20, 20);
                            }
                            filled[(int) startY - 1][(int) startX - 1] = !filled[(int) startY - 1][(int) startX - 1];
                        }
                    }
                });
        root.getChildren().add(canvas);
        final Button button = new Button("Test it!");
        button.setLayoutX(400);
        button.setLayoutY(100);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                double[] inp = new double[36];
                for (int i = 1; i < 7; i++) {
                    for (int j = 1; j < 7; j++) {
                        if (filled[i][j]) {
                            inp[(i-1) * 6 + j - 1] = 1;
                        } else {
                            inp[(i-1) * 6 + j - 1] = 0;
                        }
                    }
                }
                //double[] aaa = neuralNetwork.getResult(inp);
//                label2.setText("2 " + aaa[0]);
//                label3.setText("3 " + aaa[1]);
//                label4.setText("4 " + aaa[2]);
//                label5.setText("5 " + aaa[3]);
//                label7.setText("7 " + aaa[4]);
            }
        });
        root.getChildren().add(button);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
