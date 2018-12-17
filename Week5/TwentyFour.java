import java.util.*;
import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TFQuarantine {
    List<String> _funcs;
    List<String> word_list;
    List<Map.Entry<String, Integer>> word_freqs;
    
    String[] args;
    String path_to_file;
    String top25;
    
    // This is __init__
    TFQuarantine(String[] input) {
        args = input;
        _funcs = new ArrayList<>();
    }
    
    public void bind(String func) {
        _funcs.add(func);
    }
    
    public void execute() throws IOException {
        for(String func: _funcs) {
            if(func.equals("get_input")) {
                get_input _get_input = new get_input(args);
                path_to_file = _get_input._f();
            }
            else if(func.equals("extract_words")) {
                extract_words _extract_words = new extract_words(path_to_file);
                word_list = _extract_words._f();
                
                //System.out.println(word_list);
            }
            else if(func.equals("remove_stop_words")) {
                remove_stop_words _remove_stop_words = new remove_stop_words(word_list);
                word_list = _remove_stop_words._f();
                //System.out.println(word_list);
            }
            else if(func.equals("frequencies")) {
                frequencies _frequencies = new frequencies(word_list);
                word_freqs = _frequencies.word_freqs;
                //System.out.println(word_freqs);
            }
            else if(func.equals("sort")) {
                sort _sort = new sort(word_freqs);
                word_freqs = sort.sorted_word_freqs;
                //System.out.println(word_freqs);
            }
            else if(func.equals("top25_freqs")) {
                top25_freqs _top25_freqs = new top25_freqs(word_freqs);
                top25 = _top25_freqs.top25;
                System.out.println(top25);
            }
        }
    }
}

class get_input {
    String[] args;
    
    get_input(String[] input) {
        args = input;
    }
    
    public String _f() {
        //System.out.println("A");
        return args[0];
    }
}

class extract_words {
    List<String> word_list;
    
    extract_words(String path_to_file) throws IOException {
        word_list = new ArrayList<>();
        
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
        
        word_list = Arrays.asList(sb.toString().toLowerCase().trim().split("\\s+"));
    }
    
    public List<String> _f() {
        //System.out.println("B");
        return word_list;
    }
}

class remove_stop_words {
    List<String> noStopWords_word_list;
    
    remove_stop_words(List<String> word_list) throws IOException {
        noStopWords_word_list = new ArrayList<>();
        
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
        
        for(int i = 0; i < word_list.size(); i++) {
            String curr = word_list.get(i);
            if(!set.contains(word_list.get(i))) {
                noStopWords_word_list.add(curr);
            }
        }
    }
    
    public List<String> _f() {
        //System.out.println("C");
        return noStopWords_word_list;
    }
}

class frequencies {
    List<Map.Entry<String, Integer>> word_freqs;
    
    frequencies(List<String> word_list) {
        //System.out.println("D");
        
        HashMap<String, Integer> map = new HashMap<>();
        for(String word: word_list) {
            if(!map.containsKey(word)) map.put(word, 0);
            map.put(word, map.get(word) + 1);
        }
        map.remove("s");
        
        word_freqs = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
    }
}

class sort {
    static List<Map.Entry<String, Integer>> sorted_word_freqs;
    
    sort(List<Map.Entry<String, Integer>> word_freqs) {
        //System.out.println("E");
        
        Collections.sort(word_freqs, (a, b) -> (b.getValue() - a.getValue()));
        sorted_word_freqs = new LinkedList<>(word_freqs);
    }
}

class top25_freqs {
    String top25;
    
    top25_freqs(List<Map.Entry<String, Integer>> sorted_word_freqs) {
        //System.out.println("F");
        
        top25 = "";
        for(int i = 0; i < 25; i++) {
            Map.Entry<String, Integer> entry = sorted_word_freqs.get(i);
            top25 += entry.getKey() + "  -  " + String.valueOf(entry.getValue()) + "\n";
        }
    }
}

public class TwentyFour {    
    public static void main(String[] args) throws IOException {
        TFQuarantine tbq = new TFQuarantine(args);
        
        tbq.bind("get_input");
        tbq.bind("extract_words");
        tbq.bind("remove_stop_words");
        tbq.bind("frequencies");
        tbq.bind("sort");
        tbq.bind("top25_freqs");
        
        tbq.execute();
    }
}