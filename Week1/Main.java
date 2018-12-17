import java.util.*;
import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
	    // read file stop_words.txt
	    FileReader fr1 = new FileReader("../stop_words.txt");
        BufferedReader br1 = new BufferedReader(fr1);

        String s1 = "";
        while (br1.ready()) {
            s1 += br1.readLine();
        }
        fr1.close();
        
        String temp1[] = s1.split(",");
        HashSet<String> set = new HashSet<>();
        for(int i = 0; i < temp1.length; i++) {
            set.add(temp1[i]);
        }
        
        HashMap<String, Integer> map = new HashMap<>();
        
        // read file pride_and_prejudice.txt
        String filename = "../" + args[0];
        FileReader fr2 = new FileReader(filename);
        BufferedReader br2 = new BufferedReader(fr2);

        while (br2.ready()) {
            String s2 = br2.readLine().toLowerCase();
            //System.out.println(s2);
            
            Pattern pattern = Pattern.compile("[a-z]{2,}");
            Matcher matcher = pattern.matcher(s2);
            while (matcher.find()) {
                String key = matcher.group(0);
                //System.out.println(key);
    
                if(!set.contains(key)) {
                    if(!map.containsKey(key)) map.put(key, 0);
                    map.put(key, map.get(key) + 1);
                }
            }
        }
        
//        for(String str: map.keySet()) {
//            System.out.println(str + ": " + map.get(str));
//        }
        
        fr2.close();
        
        PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<>((a,b) -> (b.getValue() - a.getValue()));
        for(Map.Entry<String, Integer> entry: map.entrySet()) {
            pq.offer(entry);
        }

        // write to file test.txt
        FileWriter fw = new FileWriter("./Result1.txt");
        for(int i = 1; i <= 25; i++) {
            Map.Entry<String, Integer> entry = pq.poll();
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