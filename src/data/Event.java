package data;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Event extends Data {
   // public Event(JSONObject ev) {
   //    this.inputs = new double[4];
   //    try {
   //       inputs[0] = ev.getInt("time");
   //       inputs[1] = ev.getInt("prog");
   //       inputs[2] = ev.getInt("osVer");
   //       inputs[3] = ev.getInt("devID");

   //       this.values = new double[1];
   //       if (ev.getInt("risk") != 0)
   //          values[0] = ev.getInt("risk");
   //    } catch(Exception e) { e.printStackTrace(); }
   // }
   private static Map<String, Integer> encode(String value) {
      Map<String, Integer> data = new HashMap<>();

      int encode = 0;
      String[] keys = { "event", "time", "date", "prog", "mach", "comd", "os_v", "risk" };
      String[] lines = value.split("\n");

      for (String key : keys) {
         for (String line : lines) {
            if (line.contains("\"" + key + "\":")) {
               String val = line.trim().split(":")[1].trim();
               val = val.replaceAll("\"", "").replaceAll("\\\\", "");

               if (!data.containsKey(val))
                  data.put(val, encode ++);
            }
         }
      } return data;
   }
   public static ArrayList<Map<String, Integer>> cleanDataset(String path) {
      JSONParser parser = new JSONParser();
      ArrayList<Map<String, Integer>> dataset = new ArrayList<>();

      try (FileReader reader = new FileReader(path)) {
         var array = (JSONArray) parser.parse(reader);
         for (Object obj: array) {
            var data = (JSONObject) obj;

            dataset.add(encode(data.toString()));
         }
      } catch(Exception e) { e.printStackTrace(); }
      return dataset;
   }
}