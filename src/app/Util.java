package app;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

import data.Data;

public class Util {
   public static String toReadable(double [] array) {
      if (array.length <= 3)
         return Arrays.toString(array);

      String msg = "[";

      msg += (round(array[0], 4) + " \u00B7\u00B7\u00B7 " + round(array[array.length - 1], 4));

      return msg + "]";
   }

   public static double  round(double  value, int places) {
      if (places < 0) throw new IllegalArgumentException();

      BigDecimal bd = new BigDecimal(value+"");
      bd = bd.setScale(places, RoundingMode.HALF_UP);
      return bd.floatValue();
   }

   public static Data[][] convert(Data[] original, int width, int height) {
      Data[][] array = new Data[height][width];
      int x = 0; int y = 0;

      for (int i = 0; i < original.length - 1; i++) {
         x = i % width;
         if (x == 0 && i != 0)
            y += 1;
         array[y][x] = original[i];
      }

      return array;
   }
}