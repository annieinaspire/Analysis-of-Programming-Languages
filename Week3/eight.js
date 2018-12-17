function read_file(path_to_file, func) {
    var fs = require("fs");
    fs.readFile(path_to_file, function (err, data) {
       if (err) {
          return console.error(err);
       }
       func(data.toString(), normalize);
    });
}

function filter_chars(str_data, func) {
    var regex = /[\W_]+/g;
    func(str_data.replace(regex, ' '), scan);
}

function normalize(str_data, func) {
    func(str_data.toLowerCase(), remove_stop_words);
}

function scan(str_data, func) {
    func(str_data.split(" "), frequencies);
}

function remove_stop_words(word_list, func) {
    var fs = require("fs");
    fs.readFile("../stop_words.txt", function (err, text) {
       if (err) {
          return console.error(err);
       }
       
       var stop_words;
       stop_words = text.toString().split(",");
       stop_words.push("s");
       //console.log(stop_words);
       
       var list = [];
       for(var i = 0; i < word_list.length; i++) {
           if(stop_words.indexOf(word_list[i]) === -1) {
               list.push(word_list[i]);
           }
       }
       
       func(list, sort);
    });
}

function frequencies(word_list, func) {
    var map = new Map();
    for(var i = 0; i < word_list.length; i++) {
        var word = word_list[i];
        if(map.has(word)) {
            map.set(word, map.get(word) + 1);
        }
        else {
            map.set(word, 1);
        }
    }
    func(map, print_text);
}

function sort(wf, func) {
    var wf_sorted = new Map([...wf.entries()].sort((a, b) => b[1] - a[1]));
    func(wf_sorted, no_op);
}

function print_text(word_freqs, func) {
    var i = 1;
    for (let [k, v] of word_freqs) {
        console.log(k + "  -  " + v);
        if(i === 25) break;
        i++;
    }
    func();
}

function no_op() {
    return;
}

read_file(process.argv[2], filter_chars);
