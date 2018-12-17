import java.util.*;
import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Four {
    public static void main(String[] args) throws IOException {
        String content;
        List<String> words;
        List<Map.Entry<String, Integer>> freq;
        
        content = read_file("../" + args[0]);
	    content = filter_chars_and_normalize(content);
	    words = scan(content);
	    words = remove_stop_words(words);
	    freq = frequencies(words);
	    freq = sort(freq);
	    
        FileWriter fw = new FileWriter("./Result2-Four.txt");
        for(int i = 1; i <= 25; i++) {
            Map.Entry<String, Integer> entry = freq.get(i-1);
            if(i == 25) {
                fw.write(entry.getKey() + "  -  " + entry.getValue());
            }
            else {
                fw.write(entry.getKey() + "  -  " + entry.getValue() + "\n");
            }
            fw.flush();
        }
        fw.close();
    }
    
    private static String read_file(String path_to_file) throws IOException {
        FileReader fr = new FileReader(path_to_file);
        BufferedReader br = new BufferedReader(fr);

        String s = "";
        while (br.ready()) {
            s += " " + br.readLine();
        }
        fr.close();
        
        return s;
    }
    
    private static String filter_chars_and_normalize(String s) {
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
    
    private static List<String> scan(String s) throws IOException {
        String[] words = s.trim().split("\\s+");
        return Arrays.asList(words);
    }
    
    private static List<String> remove_stop_words(List<String> words) throws IOException {
        FileReader fr = new FileReader("../stop_words.txt");
        BufferedReader br = new BufferedReader(fr);

        HashSet<String> set = new HashSet<>();
        while (br.ready()) {
            String[] temp = br.readLine().split(",");
            for(String s: temp) {
                set.add(s);
            }
        }
        fr.close();
        
        List<String> newWords = new ArrayList<>();
        for(int i = 0; i < words.size(); i++) {
            String curr = words.get(i);
            if(!set.contains(words.get(i))) {
                newWords.add(curr);
            }
        }
        
        return newWords;
    }
    
    private static List<Map.Entry<String, Integer>> frequencies(List<String> words) {
        HashMap<String, Integer> map = new HashMap<>();
        for(String word: words) {
            if(!map.containsKey(word)) map.put(word, 0);
            map.put(word, map.get(word) + 1);
        }
        
        map.remove("s");
        
        List<Map.Entry<String, Integer>> list = 
            new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
        
        return list;
    }
    
    private static List<Map.Entry<String, Integer>> sort(List<Map.Entry<String, Integer>> list) {
        Collections.sort(list, (a, b) -> (b.getValue() - a.getValue()));
        return list;
    }
}