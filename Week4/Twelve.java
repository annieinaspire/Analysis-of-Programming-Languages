import java.util.*;
import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Twelve {
    // Objects
    private static DataStorageManager data_storage_obj;
    private static StopWordManager stop_words_obj;
    private static WordFrequencyManager word_freqs_obj;
    
    // Functions
    private static List<String> extract_words(String path_to_file) throws IOException {
        FileReader fr = new FileReader(path_to_file);
        BufferedReader br = new BufferedReader(fr);

        String s = "";
        while (br.ready()) {
            s += " " + br.readLine();
        }
        fr.close();
        
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(!Character.isLetterOrDigit(c)) {
                sb.append(' ');
            }
            else {
                sb.append(c);
            }
        }
        
        return Arrays.asList(sb.toString().toLowerCase().trim().split("\\s+"));
    }
    
    private static Set<String> load_stop_words() throws IOException {
        FileReader fr = new FileReader("../stop_words.txt");
        BufferedReader br = new BufferedReader(fr);
            
        Set<String> set = new HashSet<>();
        while (br.ready()) {
            String[] temp = br.readLine().split(",");
            for(String s: temp) {
                set.add(s);
            }
        }
        fr.close();
        
        return set;
    }
    
    private static void increment_count_helper(String word, Map<String, Integer> map) {
        if(!map.containsKey(word)) map.put(word, 0);
        map.put(word, map.get(word) + 1);
    }
    
    // Classes of Objects
    static class DataStorageManager {
        private List<String> words;
         
        DataStorageManager() {
            words = new ArrayList<String>();
        }
        
        // init
        public void init(String path_to_file) throws IOException {
            words = extract_words(path_to_file);
        }
        
        // words
        public List<String> words() throws IOException {
            return words;
        }
    }
    
    static class StopWordManager {
        private static Set<String> stop_words;
        
        StopWordManager() {
            stop_words = new HashSet<>();
        }
        
        // init
        public static void init() throws IOException {
            stop_words = load_stop_words();
        }
        
        // is_stop_words
        public static boolean is_stop_word(String word) {
            return stop_words.contains(word);
        }
    }

    static class WordFrequencyManager {
        private static Map<String, Integer> map;
        private static List<Map.Entry<String, Integer>> list;
        
        WordFrequencyManager() {
            map = new HashMap<>();
            list = new LinkedList<>();
        }
        
        // increment_count
        public static void increment_count(String word) {
            increment_count_helper(word, map);
        }
        
        // top25
        public static void top25() {
            map.remove("s");
            list.addAll(map.entrySet());
            Collections.sort(list, (a, b) -> (b.getValue() - a.getValue()));
            
            for(int i = 1; i <= 25; i++) {
                Map.Entry<String, Integer> entry = list.get(i-1);
                System.out.println(entry.getKey() + "  -  " + entry.getValue());
            }
        }
    }
    
    // The Main Function
    public static void main(String[] args) throws IOException {
	    data_storage_obj = new DataStorageManager();
        stop_words_obj = new StopWordManager();
        word_freqs_obj = new WordFrequencyManager();
        
        data_storage_obj.init(args[0]);
        stop_words_obj.init();
        
        for(String w: data_storage_obj.words()) {
            if(!stop_words_obj.is_stop_word(w)) {
                word_freqs_obj.increment_count(w);
            }
        }
        
        word_freqs_obj.top25();
    }
}