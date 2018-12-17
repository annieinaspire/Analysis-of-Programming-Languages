import java.util.*;
import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TwentyNine {
    public static void main(String[] args) throws IOException {
        Queue<String> word_space = new LinkedList<>();
        Queue<Map<String, Integer>> freq_space = new LinkedList<>();
        Set<String> stop_words = get_stop_words();
        
        FileReader fr = new FileReader(args[0]);
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
        String[] words = sb.toString().toLowerCase().trim().split("\\s+");
        
        for (String word : words) {
            word_space.offer(word);
        }

        List<Worker> workers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (i <  4) workers.add(new Worker("process_words", freq_space, word_space, stop_words));
            if (i == 4) workers.add(new Worker("merge_word_frequency", freq_space, word_space, stop_words));
        }

        for (Worker worker: workers) {
            worker.start();
        }

        for (Worker worker: workers) {
            try {
                worker.join(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        TreeMap<String, Integer> sorted_freqs = Worker.get_sorted_freqs();
        int i = 0;
        for(Map.Entry<String, Integer> entry: sorted_freqs.entrySet()) {
            if(entry.getKey().equals("s")) continue;
            System.out.println(entry.getKey() + "  -  " + entry.getValue());
            if(++i == 25) break;
        }     
    }

    public static void process_words(Queue<Map<String, Integer>> freq_space, Queue<String> word_space, Set<String> stop_words) {
        Map<String, Integer> word_freqs = new HashMap<>();
        while (!word_space.isEmpty()) {
            String word = word_space.poll();
            if (stop_words.contains(word)) continue;
            word_freqs.put(word, word_freqs.getOrDefault(word, 0) + 1);
        }
        freq_space.offer(word_freqs);
        return;
    }

    public static Set<String> get_stop_words() throws IOException {
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

class Worker extends Thread {
    private static TreeMap<String, Integer> sorted_freqs;
    private Thread t;
    
    private String method;
    private Queue<Map<String, Integer>> freq_space; 
    private Queue<String> word_space;
    private Set<String> stop_words;

    public Worker(String method, Queue<Map<String, Integer>> freq_space, Queue<String> word_space, Set<String> stop_words) {
        this.method = method;
        this.freq_space = freq_space;
        this.word_space = word_space;
        this.stop_words = stop_words;
    }

    public static TreeMap<String, Integer> get_sorted_freqs() {
        return sorted_freqs;
    }

    public void run() {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                   sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }  
            }
        });

        thread.start();
        try {
            thread.join(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    } 

    public void start() {
        if (t == null) {
            t = new Thread (this, method);
            t.start();
        }
        if (method.equals("merge_word_frequency")) {
            process_words(freq_space, word_space, stop_words);
        }
    }

    public void process_words(Queue<Map<String, Integer>> freq_space, Queue<String> word_space, Set<String> stop_words) {
        Map<String, Integer> word_freqs = new HashMap<>();
        while (!word_space.isEmpty()) {
            String word = word_space.poll();
            if (stop_words.contains(word)) continue;
            word_freqs.put(word, word_freqs.getOrDefault(word, 0) + 1);
        }
        freq_space.offer(word_freqs);
        sorted(word_freqs);
        return;
    }

    public void sorted(Map<String, Integer> map) {
        SortMapByValue smbv = new SortMapByValue(map);
        sorted_freqs = new TreeMap<String, Integer>(smbv);
        sorted_freqs.putAll(map);
    }
}