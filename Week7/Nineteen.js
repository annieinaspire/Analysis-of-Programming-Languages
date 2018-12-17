const fs = require('fs');
const util = require('./Nineteen_util.js');
const words = require('./Nineteen_plugin/words.js');
const frequencies = require('./Nineteen_plugin/frequencies.js');

let stops, config, list, freq, result;

config = util.read_config(fs);
stops = util.extract_stop_words(fs);

if (config['words'] === 1)
    list = words.func1(fs, stops, '../pride-and-prejudice.txt');
else 
    list = words.func2(fs, stops, '../pride-and-prejudice.txt');

if (config['frequencies'] === 1)
    freq = frequencies.func1(list);
else 
    freq = frequencies.func2(list);

result = util.sort(freq);

var i = 1;
for (let [k, v] of result) {
    console.log(k + "  -  " + v);
    if(i === 25) break;
    i++;
}