import java.util.*;
import java.io.*;

public class TwentySix {
    private static List<List<String>> all_words;
    private static List<List<String>> stop_words;
    private static List<List<String>> non_stop_words;
    private static List<List<String>> unique_words;
    private static List<List<String>> counts;
    private static List<List<String>> sorted_data;
    private static List<List<List<String>>> all_columns;
    
    private static List<String> _all_words(String filename) throws IOException {
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);

        List<String> all_words = new ArrayList<>();
        while (br.ready()) {
            String line = br.readLine();
            
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if(!Character.isLetterOrDigit(c)) sb.append(' ');
                else sb.append(c);
            }
            
            String[] tempWords = sb.toString().split(" ");
            for(String word: tempWords) all_words.add(word.toLowerCase());    
        }
        fr.close();
        
        return all_words;
    }
    
    private static List<String> _stop_words() throws IOException {
        FileReader fr = new FileReader("../stop_words.txt");
        BufferedReader br = new BufferedReader(fr);

        List<String> stop_words = new ArrayList<>();
        while (br.ready()) {
            String[] temp = br.readLine().split(",");
            for(String s: temp) {
                if(!stop_words.contains(s)) stop_words.add(s);
            }
        }
        fr.close();
        
        stop_words.add("s");
        return stop_words;
    }
    
    private static List<String> _non_stop_words(List<String> all_words, List<String> stop_words) {
        List<String> non_stop_words = new ArrayList<>();
        for(String w: all_words) {
            if(!stop_words.contains(w)) {
                non_stop_words.add(w);
            }      
        }
        return non_stop_words;
    }

    private static List<String> _unique_words(List<String> non_stop_words) {
        List<String> unique_words = new ArrayList<>();
        for(String w: non_stop_words) {
            if(!w.equals("")) {
                unique_words.add(w);
            }      
        }
        return unique_words;
    }

    private static List<String> _counts(List<String> unique_words) {
        Map<String, Integer> map = new HashMap<>();
        for(String w: unique_words) {
            map.put(w, map.getOrDefault(w, 0) + 1);
        }
        
        List<String> counts = new ArrayList<>();
        for(Map.Entry<String, Integer> entry: map.entrySet()) {
            counts.add(entry.getKey() + ":" + String.valueOf(entry.getValue()));
        }
        return counts;
    }

    private static List<String> _sorted_data(List<String> counts) {
        Map<String, Integer> map = new HashMap<>();
        for(String s: counts) {
            String[] key_and_value = s.split(":");
            map.put(key_and_value[0], Integer.valueOf(key_and_value[1]));
        }
        
        List<Map.Entry<String, Integer>> tempList = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(tempList, (a, b) -> (b.getValue() - a.getValue()));
        
        List<String> sorted_data = new ArrayList<>();
        for(int i = 0; i < tempList.size(); i++) {
            Map.Entry<String, Integer> entry = tempList.get(i);
            sorted_data.add(entry.getKey() + ":" + String.valueOf(entry.getValue()));
        }
        return sorted_data;
    }
    
    private static void update() {
        int i = 1;
        for(List<List<String>> c: all_columns) {
            if(i == 3)      c.set(1, _non_stop_words(all_words.get(0), stop_words.get(0)));
            else if(i == 4) c.set(1, _unique_words(non_stop_words.get(0)));
            else if(i == 5) c.set(1, _counts(unique_words.get(0)));
            else if(i == 6) c.set(1, _sorted_data(counts.get(0)));
            i++;
            
            if(c.get(1).size() != 0) {
                c.set(0, c.get(1));
            } 
        }
    }

    public static void main(String[] args) throws IOException {
	    all_words      = new ArrayList<>();
	    stop_words     = new ArrayList<>();
	    non_stop_words = new ArrayList<>();
	    unique_words   = new ArrayList<>();
	    counts         = new ArrayList<>();
	    sorted_data    = new ArrayList<>();
	    all_columns    = new ArrayList<>();
	    
	    for(int i = 0; i< 2; i++) {
	        all_words.add(new ArrayList<String>());
	        stop_words.add(new ArrayList<String>());
	        non_stop_words.add(new ArrayList<String>());
	        unique_words.add(new ArrayList<String>());
	        counts.add(new ArrayList<String>());
	        sorted_data.add(new ArrayList<String>());
	    }
	    
	    all_words.set(0, _all_words(args[0]));
	    stop_words.set(0, _stop_words());
	    
	    all_columns.add(all_words);
	    all_columns.add(stop_words);
	    all_columns.add(non_stop_words);
	    all_columns.add(unique_words);
	    all_columns.add(counts);
	    all_columns.add(sorted_data);
	    
	    update();
	    
	    for(int i = 0; i < 25; i++) {
	        String s = sorted_data.get(0).get(i);
	        String[] key_and_value = s.split(":");
	        System.out.println(key_and_value[0] + "  -  " + key_and_value[1]);
	    }
    }
}