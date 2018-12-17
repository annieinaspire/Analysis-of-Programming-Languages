module.exports = {
    read_config: function (fs) {
        return JSON.parse(fs.readFileSync('../config.json'));   
    },
    extract_stop_words: function (fs) {
        var content = fs.readFileSync('../stop_words.txt', 'utf8');
        
        var stop_words = content.toString().split(",");
        stop_words.push("s");
        return stop_words;
    },
    sort: function (word_freqs) {
        var wf_sorted = new Map([...word_freqs.entries()].sort((a, b) => b[1] - a[1]));
        return wf_sorted;
    }
}