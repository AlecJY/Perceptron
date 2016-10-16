package com.alebit.perceptron;

/**
 * Created by Alec on 2016/10/14.
 */
public class PerceptronAlgorithm {
    private double[][] rawData;
    private double[][] learningData;
    private double[][] testData;
    private double learningRate;
    private double iterateTimes;
    private double threshold = 1;
    private double oTh = 1;
    private double[] classification = new double[2];
    private boolean trainSucceed = false;
    private OneDMatrix w;

    public PerceptronAlgorithm(double[][] rawData, double learningRate, int iterateTimes) {
        this.rawData = rawData;
        this.learningRate = learningRate;
        this.iterateTimes = iterateTimes;
    }

    public void initialize() {
        /* int num = rawData.length;
        int learningNum = (int) Math.ceil(num * 2 / 3);
        int testNum = num - learningNum;
        learningData = new double[learningNum][];
        testData = new double[testNum][];
        System.arraycopy(rawData, 0, learningData, 0, learningNum);
        System.arraycopy(rawData, learningNum, testData, 0, testNum); */
        classification[0] = rawData[0][rawData[0].length-1];
        for (int i = 0; i < rawData.length; i++) {
            if (rawData[i][rawData[0].length-1] != classification[0]) {
                classification[1] = rawData[i][rawData[0].length-1];
                break;
            }
        }
        if (classification[0] > classification[1]) {
            double tmp = classification[0];
            classification[0] = classification[1];
            classification[1] = tmp;
        }
    }

    public double[] calculate() {
        OneDMatrix w = new OneDMatrix(rawData[0].length - 1);
        for (int i = 0; i < w.size(); i++) {
            w.set(i, 0);
        }
        w = training(w);
        this.w = w;
        System.out.println(threshold + " " + w.toArray()[0] + " " + w.toArray()[1]);
        System.out.println(trainSucceed);
        return w.toArray();
    }

    private OneDMatrix training(OneDMatrix w) {
        for (int i = 0; i < iterateTimes; i++) {
            double total = 0;
            for (double[] data : rawData) {
                OneDMatrix vector = new OneDMatrix(data);
                double e = data[data.length - 1] - out(vector, w);
                System.out.println("e: " + e);
                total += Math.abs(e);
                OneDMatrix dw = new OneDMatrix(w.size());
                threshold += oTh * learningRate * e;
                System.out.println(threshold);
                for (int j = 0; j < dw.size(); j++) {
                    dw.set(j, vector.get(j) * learningRate * e);
                }
                w = w.add(dw);
                System.out.println(w.toArray()[0] + " " + w.toArray()[1]);
            }
            if (total == 0) {
                trainSucceed = true;
                break;
            }
        }
        return w;
    }

    private double out(OneDMatrix vector, OneDMatrix w) {
        double result = vector.multiply(w) + threshold * oTh;
         if (result >= 0) {
            return classification[1];
        } else {
            return classification[0];
        }
    }

    public double getX(double y) {
        double[] wArray = w.toArray();
        return (-1 * threshold * oTh - y*wArray[1])/wArray[0];
    }

    public double getY(double x) {
        double[] wArray = w.toArray();
        return (-1 * threshold * oTh -x*wArray[0])/wArray[1];
    }

    public double getThreshold() {
        return threshold;
    }

    public double validate() {
        double count = 0;
        for (double[] data: rawData) {
            OneDMatrix vector = new OneDMatrix(data);
            if (out(vector, w) != data[data.length-1]) {
                count++;
            }
        }
        return (rawData.length - count) / rawData.length;
    }
}
