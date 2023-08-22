package app;
import data.*;

public class Main {
   Network network;
   int[] dims = { 2, 5, 2 };
   Data[] dataset;

   public Main() {
      Event.cleanDataset("data/dataset.json").forEach(data -> System.out.println(data.toString()));
   }
}