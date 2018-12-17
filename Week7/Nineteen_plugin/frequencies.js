module.exports = {
    func1: function (word_list) {
        var map = new Map();
        for (var i = 0; i < word_list.length; i++) {
            var word = word_list[i];
            if (map.has(word)) map.set(word, map.get(word) + 1);
            else map.set(word, 1);
        }
        return map;
    },
    func2: function (word_list) {
        console.log('frequencies');
    },
};
