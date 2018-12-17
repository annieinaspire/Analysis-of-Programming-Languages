import java.util.*;
import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Five {
    public static void main(String[] args) throws IOException {
	    print_all(
	    sort(
	    frequencies(
	    remove_stop_words(
	    scan(
	    filter_chars_and_normalize(
	    read_file(args[0] + "," + args[1])
	    ))))));
    }
    
    private static String read_file(String files) throws IOException {
        String[] path_to_file = files.split(",");
        
        FileReader fr = new FileReader("../" + path_to_file[0]);
        BufferedReader br = new BufferedReader(fr);

        String s = "";
        while (br.ready()) {
            s += " " + br.readLine();
        }
        fr.close();
        
        s += " " + path_to_file[1];
        
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
    
    private static List<String> scan(String s) {
        String[] words = s.trim().split("\\s+");
        return Arrays.asList(words);
    }
    
    private static List<String> remove_stop_words(List<String> words) throws IOException {
        String path_to_file = "../";
        int idx = words.size() - 3;
        
        path_to_file += words.get(idx) + "_";
        path_to_file += words.get(idx+1) + ".";
        path_to_file += words.get(idx+2);
        //System.out.println(path_to_file);
        
        FileReader fr = new FileReader(path_to_file);
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
    
    private static void print_all(List<Map.Entry<String, Integer>> list) throws IOException {
        FileWriter fw = new FileWriter("./Result2-Five.txt");
        for(int i = 1; i <= 25; i++) {
            Map.Entry<String, Integer> entry = list.get(i-1);
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
}