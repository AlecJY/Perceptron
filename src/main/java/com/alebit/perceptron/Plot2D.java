package com.alebit.perceptron;

import org.math.plot.canvas.Plot2DCanvas;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Alec on 2016/10/14.
 */
public class Plot2D {
    protected JPanel graphPanel;
    protected JFrame frame;
    protected ArrayList<Color> colors = new ArrayList<>(Arrays.asList(Color.RED, Color.BLUE, Color.BLACK, Color.GREEN, Color.CYAN));
    private Plot2DCanvas plot2DCanvas = new Plot2DCanvas();
    protected int lineCount = 0;

    public Plot2D(JPanel graphPanel, JFrame frame) {
        this.graphPanel = graphPanel;
        this.frame = frame;
    }

    public void drawPlot(HashMap<Double, ArrayList<double[]>> dotData) {
        int colorCount = 0;
        for (double key: dotData.keySet()) {
            plot2DCanvas.addScatterPlot("Group " + (int) key, colors.get(colorCount%colors.size()), arrayListToArray(dotData.get(key)));
            colorCount++;
        }

        graphPanel.setLayout(new GridLayout());
        graphPanel.add(plot2DCanvas);
        frame.pack();
        frame.revalidate();
    }

    protected double[][] arrayListToArray(ArrayList<double[]> arrayList) {
        double[][] array = new double[arrayList.size()][];
        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    protected void addLine(PerceptronAlgorithm perceptron, double[][] rawData) {
        double[][] lineDots = new double[2][2];
        double maxX = rawData[0][0];
        double minX = rawData[0][0];
        double maxY = rawData[0][1];
        double minY = rawData[0][1];
        for (double[] data: rawData) {
            if (data[0] > maxX) {
                maxX = data[0];
            }
            if (data[0] < minX) {
                minX = data[0];
            }
            if (data[1] > maxY) {
                maxY = data[1];
            }
            if (data[1] < minY) {
                minY = data[1];
            }
        }
        double maxX_Y = perceptron.getY(maxX);
        double maxY_X = perceptron.getX(maxY);
        if (Double.isInfinite(maxX_Y) || Double.isNaN(maxX_Y)) {
            lineDots[1][0] = maxY_X;
            lineDots[1][1] = maxY;
        } else if (Double.isInfinite(maxY_X) || Double.isNaN(maxY_X)) {
            lineDots[1][0] = maxX;
            lineDots[1][1] = maxX_Y;
        } else {
            if (maxX_Y > maxY && Math.abs(maxX_Y - maxY) < Math.abs(maxY_X - maxX)) {
                lineDots[1][0] = maxX;
                lineDots[1][1] = maxX_Y;
            } else {
                lineDots[1][0] = maxY_X;
                lineDots[1][1] = maxY;
            }
        }
        double minX_Y = perceptron.getY(minX);
        double minY_X = perceptron.getX(minY);
        if (Double.isInfinite(minX_Y) || Double.isNaN(minX_Y)) {
            lineDots[0][0] = minY_X;
            lineDots[0][1] = minY;
        } else if (Double.isInfinite(minY_X) || Double.isNaN(minY_X)) {
            lineDots[0][0] = minX;
            lineDots[0][1] = minX_Y;
        } else {
            if (minX_Y < minY && Math.abs(minX_Y - minY) < Math.abs(minY_X - minY)) {
                lineDots[0][0] = minX;
                lineDots[0][1] = minX_Y;
            } else {
                lineDots[0][0] = minY_X;
                lineDots[0][1] = minY;
            }
        }
        plot2DCanvas.addLinePlot("w", colors.get(lineCount % colors.size()), lineDots);
        lineCount++;
    }
}
