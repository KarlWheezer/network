package app;

import java.util.Random;

public class Layer {
   double[] values;
   double[] biases, biasGradients;
   double[][] weights, weightGradients;

   int i_nodes, o_nodes;

   public Layer(int inputs, int outputs) {
      i_nodes = inputs; o_nodes = outputs;

      this.values = new double[o_nodes];
      this.biases = randomizeBiases();
      this.weights = randomizeWeights();
   }

   private double[][] randomizeWeights() {
      Random random = new Random();
      double[][] weights = new double[i_nodes][o_nodes];
      weightGradients = new double[i_nodes][o_nodes];

      for (int o = 0; o < o_nodes; o ++) for (int i = 0; i < i_nodes; i++) {
         weights[i][o] = random.nextDouble(-1, 1) / Math.sqrt(i_nodes);
      }

      return weights;
   }
   private double[] randomizeBiases() {
      Random random = new Random();
      double[] biases = new double[o_nodes];
      biasGradients = new double[o_nodes];

      for (int o = 0; o < o_nodes; o ++)
         biases[o] = random.nextDouble(-1, 1) / Math.sqrt(i_nodes);

      return biases;
   }

   double cost(double expected, double predicted) {
      double error = expected - predicted;
      return error * error;
   } 
   double cost(double expected, double predicted, boolean deriv) {
      if (deriv) 
         { return 2 * (expected - predicted); } 
      else 
         { return cost(expected, predicted); }
   }
   
   private double squish(double value) {
      return 1 / ( 1 + Math.exp(-value));
   }
   double squish(double value, boolean deriv) {
      double val = squish(value);
      return val * (1  - val);
   }

   double[] evaluate(double[] inputs) {
      double[] outputs = new double[o_nodes];

      for (int o = 0; o < o_nodes; o ++) {
         double output = biases[o];
         for (int i = 0; i < i_nodes; i ++)
            output += weights[i][o] * inputs[i];
         
         outputs[o] = squish(output);
      }  values = outputs; return outputs;
   }
   double[] backpropagateErrors(double[] nextLayerErrors) {
      double[] currentLayerErrors = new double[i_nodes];

      for (int i = 0; i < i_nodes; i++) {
          double errorSum = 0.0;
          for (int o = 0; o < o_nodes; o++) {
              errorSum += nextLayerErrors[o] * weights[i][o];
          }
          currentLayerErrors[i] = errorSum * squish(values[i], true);
      }

      return currentLayerErrors;
  }
   void calculateGradients(double[] prevLayerOutputs, double[] errorGradients) {
      for (int o = 0; o < o_nodes; o++) {
         double outputGradient = errorGradients[o] * squish(values[o], true);

         biasGradients[o] += outputGradient;

         for (int i = 0; i < i_nodes; i++) {
            weightGradients[i][o] += outputGradient * prevLayerOutputs[i];
         }
      }
   } 
  void update(double rate) {
      for (int o = 0; o < o_nodes; o ++) {
         biases[o] -= biasGradients[o] * rate;
         for (int i = 0; i < i_nodes; i ++)
            weights[i][o] -= weightGradients[i][o] * rate;
      }
   }
}