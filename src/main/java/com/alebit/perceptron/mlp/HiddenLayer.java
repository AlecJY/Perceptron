package com.alebit.perceptron.mlp;

import com.alebit.perceptron.OneDMatrix;

import java.util.Random;

/**
 * Created by Alec on 2016/11/19.
 */
public class HiddenLayer {
    protected Neuron[] neurons;
    protected HiddenLayer childHiddenLayer;
    protected HiddenLayer parentHiddenLayer;
    protected double expOut;
    protected double learningRate;
    protected double[] ys;

    public HiddenLayer(int unitNum, int dim, HiddenLayer childHiddenLayerLayer) {
        initializeNeurons(unitNum, dim);
        this.childHiddenLayer = childHiddenLayerLayer;
    }

    protected void initializeNeurons(int unitNum, int dim) {
        neurons = new Neuron[unitNum];
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new Neuron(dim);
        }
    }

    public void setParentHiddenLayer(HiddenLayer parentHiddenLayer) {
        this.parentHiddenLayer = parentHiddenLayer;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public void nextHiddenLayer() {
        double[][] ysi = new double[1][];
        ys = new double[neurons.length + 1];
        ys[ys.length - 1] = expOut;

        for (int i = 0; i < neurons.length; i++) {
            ys[i] = neurons[i].getY();
        }
        ysi[0] = ys;
        childHiddenLayer.setLearningRate(learningRate);
        InputLayer inputLayer = new InputLayer(ysi);
        inputLayer.learning(childHiddenLayer);
    }

    public void testNextHiddenLayer() {
        double[][] ysi = new double[1][];
        ys = new double[neurons.length + 1];
        ys[ys.length - 1] = expOut;

        for (int i = 0; i < neurons.length; i++) {
            ys[i] = neurons[i].getY();
        }
        ysi[0] = ys;
        childHiddenLayer.setLearningRate(learningRate);
        InputLayer inputLayer = new InputLayer(ysi);
        inputLayer.testing(childHiddenLayer);
    }

    public double[] getYs() {
        return ys;
    }

    public void backPropagation() {
        for (int i = 0; i < neurons.length; i++) {
            neurons[i].getDelta(i);
        }
        if (parentHiddenLayer != null) {
            parentHiddenLayer.backPropagation();
        }
        for (Neuron neuron: neurons) {
            neuron.reviseW();
        }
    }

    public void setExpOut(double expOut) {
        this.expOut = expOut;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public boolean isOutputLayer() {
        return false;
    }

    public class Neuron {
        protected OneDMatrix w;
        protected OneDMatrix py;
        protected double y;
        protected double delta;
        protected double[] trainingData;
        public Neuron(int dim) {
            w = new OneDMatrix(dim);
            Random random = new Random();
            for (int i = 0; i < w.size(); i++) {
                double rand = random.nextDouble() + random.nextInt(2) + 0.001;
                if (random.nextBoolean()) {
                    rand *= -1;
                }
                w.set(i ,rand);
            }
        }

        public void setW(OneDMatrix w) {
            this.w = w;
        }

        public void setW(double[] w) {
            OneDMatrix wm = new OneDMatrix(w.length);
            for (int i = 0; i < w.length; i++) {
                wm.set(i, w[i]);
            }
            this.w = wm;
        }

        public double[] getW() {
            return w.toArray();
        }

        public void training(double[] trainingData) {
            this.trainingData = trainingData;
            double expt = w.get(w.size() - 1) * -1;
            for (int i = 0; i < w.size() - 1; i++) {
                expt += w.get(i)*trainingData[i];
            }
            y = 1 / (1 + Math.exp(expt*-1));
        }

        public void getDelta(int index) {
            delta = y * (1 - y);
            double sum = 0;
            for (int i = 0; i < childHiddenLayer.neurons.length; i++) {
                sum += childHiddenLayer.neurons[i].delta * childHiddenLayer.neurons[i].w.get(index);
            }
            delta *= sum;
            setPy();
        }

        protected void setPy() {
            py = new OneDMatrix(w.size());
            for (int i = 0; i < py.size() - 1; i++) {
                py.set(i, trainingData[i]);
            }
            py.set(py.size() - 1, -1);
        }

        public void reviseW() {
            w = w.add(py.multiply(learningRate *delta));
        }

        public double getY() {
            return y;
        }
    }
}
