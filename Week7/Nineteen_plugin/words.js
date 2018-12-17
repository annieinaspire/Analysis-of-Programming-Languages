module.exports = {
    func1: function (fs, stops, name) {
        var content = fs.readFileSync(name, 'utf8');
        content = content.toString();
        var sb = ""; 
        for(var i = 1; i < content.length; i++) {
            var c = content.charAt(i);
            if( (c >= 'A' && c <= 'Z') 
                || (c >= 'a' && c <= 'z') 
                || (c >= '0' && c <= '9'))
                sb += "" + c;
            else 
                sb += " ";
        }
        var word_list = sb.toLowerCase().split(" ");
      
        var list = [];
        for(var i = 0; i < word_list.length; i++) {
            if(stops.indexOf(word_list[i]) === -1) {
                list.push(word_list[i]);
            }
        }
        
        return list;
    },
    func2: function () {
        console.log('words2');
    }
};