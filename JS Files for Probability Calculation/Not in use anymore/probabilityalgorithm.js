/* Generated from Java with JSweet 2.0.0 - http://www.jsweet.org */
var stems =  (function (){
    $.getJSON( "stems.json", function( data ) {
        var dictionary = ({});
        for(var key in data){
            dictionary([key] = data[key]);
        }
});
var stopwords =  (function (){
    $.getJSON( "stopwords.json", function( data ) {
        var dictionary = ({});
        for(var key in data){
            dictionary([key] = data[key]);
        }
});
var probabilities =  (function (){
    $.getJSON( "text.json", function( data ) {
        var dictionary = data;
        return dictionary;
});


var NaiveBayes = (function () {
    function NaiveBayes() {
    }
    NaiveBayes.isInDanger = function (message, dictionary, stems, stopwords, threshold) {
        var splited = message.replace(new RegExp("[^a-zA-Z ]", 'g'), "").toLowerCase().split(" ");
        var toProcess = (splited.slice(0).slice(0));
        var stemmedWords = ([]);
        for (var index5512 = 0; index5512 < toProcess.length; index5512++) {
            var currWord = toProcess[index5512];
            {
                if (stems.hasOwnProperty(currWord)) {
                    /* add */ (stemmedWords.push(/* get */ (function (m, k) { return m[k] ? m[k] : null; })(stems, currWord)) > 0);
                }
                else {
                    /* add */ (stemmedWords.push(currWord) > 0);
                }
            }
        }    
      
        console.info(stopwords);
      
        var stopwordsNoPunctuation = ([]);
        for (var index5513 = 0; index5513 < stopwords.length; index5513++) {
            var currWord = stopwords[index5513];
            {
                /* add */ (stopwordsNoPunctuation.push(/* replaceAll */ currWord.replace(new RegExp("[^a-zA-Z ]", 'g'), "")) > 0);
            }
        }
      
        console.info(stopwordsNoPunctuation);
      
        /* removeAll */ (function (a, r) { var b = false; for (var i = 0; i < r.length; i++) {
            var ndx = a.indexOf(r[i]);
            if (ndx >= 0) {
                a.splice(ndx, 1);
                b = true;
            }
        } return b; })(stemmedWords, stopwordsNoPunctuation);
        var noDuplicates = (stemmedWords.slice(0));
      
        console.info(noDuplicates); /* debug */
      
        var totalProbability = 1.0;
        for (var index5514 = 0; index5514 < noDuplicates.length; index5514++) {
            var currWord = noDuplicates[index5514];
            {
                if (dictionary.hasOwnProperty(currWord)) {
                    totalProbability *= (function (m, k) { return m[k] ? m[k] : null; })(dictionary, currWord);
                }
            }
        }
        if (totalProbability > threshold) {
            return true;
        }
        return false;
    };
    NaiveBayes.main = function (args) {
        var message = "Car accident on I-95. Gasoline fire. Backup needed.";
        var dictionary = ({});
        /* put */ (dictionary["accident"] = 3);
        /* put */ (dictionary["gasoline"] = 4);
        /* put */ (dictionary["fire"] = 5);
        var stems = ({});
        /* put */ (stems["needed"] = "need");
        var stopwords = ([]);
        /* put */ stopwords.push("on.");
        var threshold = 61;
        console.info(NaiveBayes.isInDanger(message, dictionary, stems, stopwords, threshold));
    };
    return NaiveBayes;
}());
NaiveBayes["__class"] = "NaiveBayes";
NaiveBayes.main(null);