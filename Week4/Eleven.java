import java.util.*;
import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DataStorageManager {
    private static String content;
    private static List<String> words;
     
    DataStorageManager() {
        content = "";
        words = new ArrayList<String>();
    }
    
    public static List<String> dispatch(String message, String path_to_file) throws IOException {
        if(message.equals("init")) {
            content = _init(path_to_file);
        }
        else if(message.equals("words")) {
            words = _words();
        }
        return words;
    }
    
    private static String _init(String path_to_file) throws IOException {
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
        
        return sb.toString().toLowerCase();
    }
    
    private static List<String> _words() throws IOException {
        words = Arrays.asList(content.trim().split("\\s+"));
        return words;
    }
}

class StopWordManager {
    private static Set<String> _stop_words;
    
    StopWordManager() {
        _stop_words = new HashSet<>();
    }
    
    public static boolean dispatch(String message, String word) throws IOException {
        if(message.equals("init")) {
            init();
            return true;
        }
        else if(message.equals("is_stop_word")) {
            return is_stop_word(word);
        }
        else{
            return true;
        }
    }
    
    private static void init() throws IOException {
        FileReader fr = new FileReader("../stop_words.txt");
        BufferedReader br = new BufferedReader(fr);
            
        while (br.ready()) {
            String[] temp = br.readLine().split(",");
            for(String s: temp) {
                _stop_words.add(s);
            }
        }
        fr.close();
    }
    
    private static boolean is_stop_word(String word) {
        return _stop_words.contains(word);
    }
}

class WordFrequencyManager {
    private static Map<String, Integer> map;
    private static List<Map.Entry<String, Integer>> list;
    
    WordFrequencyManager() {
        map = new HashMap<>();
        list = new LinkedList<>();
    }
    
    public static List<Map.Entry<String, Integer>> dispatch(String message, String word) {
        if(message.equals("increment_count")) {
            increment_count(word);
        }
        else if(message.equals("sorted")) {
            sorted();
        }
        
        return list;
    }
    
    private static void increment_count(String word) {
        if(!map.containsKey(word)) map.put(word, 0);
        map.put(word, map.get(word) + 1);
    }
    
    private static void sorted() {
        map.remove("s");
        list.addAll(map.entrySet());
        Collections.sort(list, (a, b) -> (b.getValue() - a.getValue()));
    }
}

class WordFrequencyController {
    public static DataStorageManager _storage_manager;
    public static StopWordManager _stop_word_manager;
    public static WordFrequencyManager _word_freq_manager;
    
    WordFrequencyController() {
        /* DO NOTHING */
    }
    
    public static void dispatch(String message, String path_to_file) throws IOException {
        if(message.equals("init")) {
            init(path_to_file);
        }
        else if(message.equals("run")) {
            run(path_to_file);
        }
    }
    
    private static void init(String path_to_file) throws IOException {
        _storage_manager = new DataStorageManager();
        _stop_word_manager = new StopWordManager();
        _word_freq_manager = new WordFrequencyManager();
        
        _storage_manager.dispatch("init", path_to_file);
        _stop_word_manager.dispatch("init", "void");
    }
    
    private static void run(String path_to_file) {
        try {
            for(String w: _storage_manager.dispatch("words", path_to_file)) {
                if(!_stop_word_manager.dispatch("is_stop_word", w)) {
                    _word_freq_manager.dispatch("increment_count", w);
                }
            }
            
            List<Map.Entry<String, Integer>> word_freqs = 
                _word_freq_manager.dispatch("sorted", "void");
                        
            for(int i = 1; i <= 25; i++) {
                Map.Entry<String, Integer> entry = word_freqs.get(i-1);
                System.out.println(entry.getKey() + "  -  " + entry.getValue());
            }
        } catch(Exception e) {
            
        }
    }
}

public class Eleven {
    public static WordFrequencyController wfcontroller;
    
    public static void main(String[] args) throws IOException {
	    wfcontroller = new WordFrequencyController();
	    wfcontroller.dispatch("init", args[0]);
	    wfcontroller.dispatch("run", args[0]);
    }
}