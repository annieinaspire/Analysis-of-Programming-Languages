import java.util.*;
import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThirtyOne {
    //
    // Functions for map reduce
    //
    public static List<String> partition(String s) {
        List<String> result = new ArrayList<>();
        String[] chunks = s.split("\n");
        for(String str: chunks) {
            result.add(str);
        }
        return result;
    }
    
    public static List<String> split_words(List<String> chunks) throws IOException {
        FileReader fr = new FileReader("../stop_words.txt");
        BufferedReader br = new BufferedReader(fr);
            
        Set<String> stop_words = new HashSet<>();
        while (br.ready()) {
            String[] temp = br.readLine().split(",");
            for(String s: temp) {
                stop_words.add(s);
            }
        }
        fr.close();

        List<String> result = new ArrayList<>();
        for (String chunk: chunks) {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < chunk.length(); i++) {
                char c = chunk.charAt(i);
                if(!Character.isLetterOrDigit(c)) {
                    sb.append(' ');
                }
                else {
                    sb.append(c);
                }
            }
            String[] words = sb.toString().toLowerCase().trim().split("\\s+");

            for (String word : words) {
                if (!stop_words.contains(word)) {
                    result.add(word);
                }
            }
        }
        
        return result;
    }
    
    public static List<List<String>> regroup(List<String> words) {
        List<List<String>> result = new ArrayList<>();
        List<String> aj = new ArrayList<>();
        List<String> kt = new ArrayList<>();
        List<String> tz = new ArrayList<>();

        for (String word: words) {
            if (word.length() == 0) continue;
            
            char c = word.charAt(0);
            if(c >= 'a' && c <= 'j') aj.add(word);
            else if(c >= 'k' && c <= 't') kt.add(word);
            else tz.add(word);
        }

        result.add(aj);
        result.add(kt);
        result.add(tz);
        return result;
    }
    
    public static Map<String, Integer> count_words(List<List<String>> split_per_word) {
        Map<String, Integer> map = new HashMap<>();
        for (List<String> group : split_per_word) {
            for (String word : group) {
                map.put(word, map.getOrDefault(word, 0) + 1);
            }
        }
        return map;
    }
    
    //
    // Auxiliary functions
    //
    public static String read_file(String path_to_file) throws IOException {        
        FileReader fr = new FileReader(path_to_file);
        BufferedReader br = new BufferedReader(fr);
        String s = "";
        while (br.ready()) {
            s += " " + br.readLine();
        }
        fr.close();
        return s;
    }
    
    public static Map<String, Integer> sort(Map<String, Integer> map) {        
        SortMapByValue smbv = new SortMapByValue(map);
        TreeMap<String, Integer> sorted = new TreeMap<String, Integer>(smbv);
        sorted.putAll(map);
        return sorted;
    }
    
    //
    // The main function
    //
    public static void main(String[] args) throws IOException {
        String s = read_file(args[0]);
        List<String> chunks = partition(s);
        List<String> words = split_words(chunks); 
        List<List<String>> split_per_word = regroup(words);
        Map<String, Integer> word_freqs = count_words(split_per_word);
        Map<String, Integer> sorted_freqs = sort(word_freqs);

        int i = 0;
        for(Map.Entry<String, Integer> entry: sorted_freqs.entrySet()) {
            if(entry.getKey().equals("s")) continue;
            System.out.println(entry.getKey() + "  -  " + entry.getValue());
            if(++i == 25) break;
        }
    }
}

class SortMapByValue implements Comparator<String> {
    HashMap<String, Integer> map = new HashMap<String, Integer>();
 
    public SortMapByValue(Map<String, Integer> map){
        this.map.putAll(map);
    }
    
    public int compare(String s1, String s2) {
        return map.get(s2) - map.get(s1);
    }
}
