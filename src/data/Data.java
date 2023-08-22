package data;

import java.util.Arrays;

import app.Util;

public abstract class Data {
   public double[] inputs, values;

   @Override public String toString() {
      return getClass().getSimpleName() + " {\n  inputs: " + Util.toReadable(inputs) + ",\n  values: "+Arrays.toString(values)+" -> "+findValue()+" \n}";
   }

   public int findValue() {
      for (int i = 0; i < values.length; i ++)
         if (values[i] == 1) return i;
      
      return -1;
   }
}