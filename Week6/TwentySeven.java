import java.util.*;
import java.io.*;

public class TwentySeven {
    private static List<String> lines(String filename) throws IOException {
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);

        List<String> lines = new ArrayList<>();
        while (br.ready()) {
            lines.add(br.readLine());
        }
        fr.close();
        return lines;
    }
    
    private static List<String> all_words(String filename) throws IOException {
        List<String> words = new ArrayList<>();
        for(String line: lines(filename)) {    
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if(!Character.isLetterOrDigit(c)) sb.append(' ');
                else sb.append(c);
            }
            
            String[] tempWords = sb.toString().split(" ");
            for(String word: tempWords) words.add(word.toLowerCase());
        }
        return words;
    }
    
    private static List<String> non_stop_words(String filename) throws IOException {
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
        set.add("s");
        
        List<String> non_stop_words = new ArrayList<>();
        for(String w: all_words(filename)) {
            if(!set.contains(w)) {
                non_stop_words.add(w);
            }      
        }
        return non_stop_words;
    }
    
    private static List<Map.Entry<String, Integer>> count_and_sort(String filename) throws IOException {
        HashMap<String, Integer> map = new HashMap<>();
        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        List<Map.Entry<String, Integer>> tempList;
        
        int i = 1;
        for(String word: non_stop_words(filename)) {
            if(!map.containsKey(word)) map.put(word, 0);
            map.put(word, map.get(word) + 1);

            if(i % 5000 == 0) {
                tempList = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
                Collections.sort(tempList, (a, b) -> (b.getValue() - a.getValue()));
                list = tempList;
            }
            i++;
        }
        
        tempList = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(tempList, (a, b) -> (b.getValue() - a.getValue()));
        list = tempList;
        return list;
    }
    
    public static void main(String[] args) throws IOException {
        List<Map.Entry<String, Integer>> word_freqs = count_and_sort(args[0]);
        for(int i = 0; i < word_freqs.size(); i++) {
            if(i == 25) break;
            Map.Entry<String, Integer> entry = word_freqs.get(i);
            System.out.println(entry.getKey() + "  -  " + entry.getValue());
        }
    }
}