package app;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;

import data.Data;

public class Network {
   Layer[] layers;
   Data[] dataset;
   int[] dimensions;

   private class Display extends JPanel {
      JFrame window; int nodeRad = 50;
      public Display(Network network) {
         this.window = new JFrame("Neural Network");

         this.configureDisplay();
         window.setVisible(true);
      }

      private void configureDisplay() {
         window.setSize(1600, 900);
         window.add(this);
         window.setDefaultCloseOperation(3);
         window.setLocationRelativeTo(null);
         window.setBackground(new Color(59, 58, 58));
      }

      @Override public void paint(Graphics g) {
         final int width = getWidth();
         final int height = getHeight();
         Graphics2D g2 = (Graphics2D) g;
         g2.setStroke(new BasicStroke(5));

         ArrayList<int[]> points = new ArrayList<>();
         g.setColor(new Color(255, 255, 255));

         for (int i = 0; i < layers[0].i_nodes; i ++) {
            int x = width  / (2 + layers.length);
            int y = height * (1 + i) / (1 + layers[0].i_nodes);

            points.add(new int[] { x, y - (nodeRad / 2)});
         }

         for (int line = 0; line < layers.length; line ++) {
            for (int col = 0; col < layers[line].o_nodes; col ++) {
               int x = width * (2 + line) / (2 + layers.length);
               int y = (height * (1 + col) / (1 + layers[line].o_nodes));
               points.add(new int[] { x, y - (nodeRad / 2) });

               for (int a = 0; a < layers[line].i_nodes; a ++) {
                  int prev_x = width * (1 + line) / (2 + layers.length);
                  int prev_y = height * (1 + a) / (1 + layers[line].i_nodes);

                  g2.drawLine(x + (nodeRad / 2), y, prev_x + (nodeRad / 2), prev_y);
               }
            }
         }

         for (int[] point: points) {
            g.setColor(new Color(59, 58, 58));
            g.fillOval(point[0], point[1], nodeRad, nodeRad);
            g.setColor(new Color(255, 255, 255));
            g.drawOval(point[0], point[1], nodeRad, nodeRad);
         }
      }
   }

   public void display() {
      new Display(this);
   }
   public Network(int[] dimensions, Data[] dataset) {
      this.layers = new Layer[dimensions.length - 1];
      this.dimensions = dimensions;
      this.dataset = dataset;

      for (int i = 0; i < layers.length; i ++)
         layers[i] = new Layer(dimensions[i], dimensions[i + 1]);

      System.out.println(this);
   }
   @Override public String toString() {
      String msg = "Network"+Arrays.toString(dimensions)+":\n";
      msg += "  - inputs: double[" + layers[0].i_nodes + "] -> layer[0]\n\n";

      for (int i = 0; i < layers.length; i ++)
         msg += "  - layer[" + i + "] -> { in: " + layers[i].i_nodes + ", out: " + layers[i].o_nodes + " }\n";

      return msg; 
   }

   double[] forward(double[] inputs) {
      for (Layer layer: layers)
         inputs = layer.evaluate(inputs);

      return inputs;
   }
   private double cost(Data data) {
      double[] outputs = this.forward(data.inputs);
      Layer layer = layers[layers.length - 1];
      double cost = 0;

      for (int o = 0; o < outputs.length; o ++)
         cost += layer.cost(outputs[o], data.values[o]);

      return cost;
   }
   double cost() {
      double cost = 0;
      for (Data data: dataset)
         cost += cost(data);

      return cost / dataset.length;
   }

   double update(Data[] dataset, double rate) {
      final double step = 0.001;
      final double cost = cost();

      for (Layer layer: layers) {
         for (int i = 0 ;i < layer.i_nodes; i ++) 
         for (int o = 0; o < layer.o_nodes; o ++) {
            layer.weights[i][o] += step;
            double deltaCost = cost() - cost;
            layer.weights[i][o] -= step;

            layer.weightGradients[i][o] = deltaCost / step;
         }
         for (int o = 0; o < layer.o_nodes; o ++) {
            layer.biases[o] += step;
            double deltaCost = cost() - cost;
            layer.biases[o] -= step;

            layer.biasGradients[o] = deltaCost / step;
         }
      }
      for (Layer layer: layers) 
         layer.update(rate); 
      return cost;
   }

   void train(double rate, int iterations, boolean debug) {
      if (!debug) {
         for (int i = 0; i < iterations; i ++) update(dataset, rate);
      return; } double cost = 0;
      
      for (int i = 0; i < iterations; i ++) {
         cost = update(dataset, rate);

         if (i % 100 == 0)
            debugPrint(i, cost);
      } debugPrint(iterations, cost);
   }
   void train(double rate, double threshhold, boolean debug) {
      double cost = cost();
      if (!debug) {
         while (cost >= threshhold) update(dataset, rate);
      return; }

      for (int i = 0; cost >= threshhold; i ++) {
         cost = update(dataset, rate);

         if (i % 100 == 0) debugPrint(i, cost);
      }
   }
   double[] train(double rate, int size, int iters, boolean debug) {
      Data[][] batches = Util.convert(dataset, size, dataset.length / size);
      double cost = 0; int idx = 0; double[] costs = new double[iters];

      for (int i = 0; i < iters; i ++) {
         idx = i; if (i >= batches.length - 1) idx = 0;

         costs[i] = cost = update(batches[idx], rate);
         if (i % 100 == 0 && debug) 
            debugPrint(i, cost);
      } return costs;
   }

   private void debugPrint(int iter, double cost) {
      System.out.printf("iter[%d] -> cost: %s\n", iter, colorize(cost));
   }
   private static String colorize(double x) {
      String v = String.format("%.5f", x);
      if (x <= 0.05) return color(color(v, 32), 4);
      if (x <= 0.1) return color(v, 32);
      if (x <= 0.2) return color(v, 33);

      return color(v, 31);
   }
   private static<T> String color(T message, int color) {
      return "\u001B["+color+"m"+message.toString()+"\u001B[0m";
   }
}