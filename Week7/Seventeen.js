function extract_stop_words() {
    var fs = require('fs');
    var content = fs.readFileSync('../stop_words.txt', 'utf8');
    
    var stop_words = content.toString().split(",");
    stop_words.push("s");
    return stop_words;
}

var stops = extract_stop_words();

if(process.argv.length > 2) {
    // return non_stop_word_list
    var extract_words_func =
    "function extract_words_func(name) {" +
    "    var fs = require('fs');" +
    "    var content = fs.readFileSync(name, 'utf8');" +
    
    "    content = content.toString();" +
    "    sb = \"\"; " +
    "    for(var i = 1; i < content.length; i++) {" +
    "        var c = content.charAt(i);" +
    "        if((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) sb += \"\" + c;" +
    "        else sb += \" \";" +
    "    }" +
    "    word_list = sb.toLowerCase().split(\" \");" +
      
    "    var list = [];" +
    "    for(var i = 0; i < word_list.length; i++) {" +
    "        if(stops.indexOf(word_list[i]) === -1) {" +
    "            list.push(word_list[i]);" +
    "        }" +
    "    }" +
        
    "    return list;" +
    "}";
    
    // return word_freqs
    var frequencies_func =
    "function frequencies_func(word_list) {" +
    "    var map = new Map();" +
    "    for (var i = 0; i < word_list.length; i++) {" +
    "        var word = word_list[i];" +
    "        if (map.has(word)) map.set(word, map.get(word) + 1);" +
    "        else map.set(word, 1);" +
    "    }" +
    "    return map;" +
    "}";
    
    
    // return sorted_word_freqs
    var sort_func =
    "function sort_func(word_freqs) {" +
    "    var wf_sorted = new Map([...word_freqs.entries()].sort((a, b) => b[1] - a[1]));" +
    "    return wf_sorted;" +
    "}";
    
    var filename = process.argv[2];
}
else {
    // return non_stop_word_list
    var extract_words_func =
    "function extract_words_func(name) {" +
    "    return name;" +
    "}";
    
    // return word_freqs
    var frequencies_func =
    "function frequencies_func(word_list) {" +
    "    return word_list;" +
    "}";
    
    // return sorted_word_freqs
    var sort_func =
    "function sort_func(word_freqs) {" +
    "    return word_freqs;" +
    "}";
    
    var filename = "";
}

// The main function

var extract_words = eval("extract_words = " + extract_words_func);
var frequencies = eval("frequencies = " + frequencies_func);
var sort = eval("sort = " + sort_func);

var word_freqs = sort(frequencies(extract_words(filename)));

var i = 1;
for (let [k, v] of word_freqs) {
    console.log(k + "  -  " + v);
    if(i === 25) break;
    i++;
}
