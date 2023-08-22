package data;

import java.util.Random;

public class Point extends Data {
   public Point(double[] inputs) {
      this.inputs = inputs;
      this.values = classify(inputs);
   }

   public static Point[] load(int amount) {
      var dataset = new Point[amount];
      var random = new Random();

      for (int i = 0; i < amount; i ++) {
         double[] inputs = {
            random.nextFloat(-1, 1),
            random.nextFloat(-1, 1),
         }; dataset[i] = new Point(inputs);
      } return dataset;
   }
   private static double [] classify(double [] inputs) {
      double val = func(inputs); double[] vals = {0, 1};

      if (!(val > 0)) vals = new double[] {1, 0};
      return vals;
   }
   private static double func(double[] inputs) {
      double x = inputs[0]; double y = inputs[1]; 
      // return 1 / (2 * x + 0.5 * y);
      // return x / 2 * y;
      // return 1 / ( x + y );
      return 2 * Math.cos(x)*10*Math.sin(y);
   }
}