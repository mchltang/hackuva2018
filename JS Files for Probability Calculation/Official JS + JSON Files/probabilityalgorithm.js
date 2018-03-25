var NaiveBayes = (function () {
    function NaiveBayes() {
    }
    NaiveBayes.isInDanger = function (message, dictionaryNumber) {
		var threshold = 100000
		
		var stems = [];
		$.getJSON( "stems.json", function( data){
		stems = data;
		});
		
		var stopwords = [];
		$.getJSON( "stopwords.json", function( data){
		stopwords = data;
		});
		
		var dictionary = [];
		if(dictionaryNumber == 1) {
			$.getJSON( "police.json", function( data){
			policeDictionary = data;
			});
		}
		else if(dictionaryNumber == 2) {
			$.getJSON( "fire.json", function( data){
			fireDictionary = data;
			});
		}
		else if(dictionaryNumber == 3) {
			$.getJSON( "medical.json", function( data){
			medicalDictionary = data;
			});
		}
		
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
      
        var stopwordsNoPunctuation = ([]);
        for (var index5513 = 0; index5513 < stopwords.length; index5513++) {
            var currWord = stopwords[index5513];
            {
                /* add */ (stopwordsNoPunctuation.push(/* replaceAll */ currWord.replace(new RegExp("[^a-zA-Z ]", 'g'), "")) > 0);
            }
        }
      
        /* removeAll */ (function (a, r) { var b = false; for (var i = 0; i < r.length; i++) {
            var ndx = a.indexOf(r[i]);
            if (ndx >= 0) {
                a.splice(ndx, 1);
                b = true;
            }
        } return b; })(stemmedWords, stopwordsNoPunctuation);
        var noDuplicates = (stemmedWords.slice(0));
      
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
		/* 1 = police, 2 = fire, 3 = medical */
        console.info(NaiveBayes.isInDanger(message, 1));
        console.info(NaiveBayes.isInDanger(message, 2));
        console.info(NaiveBayes.isInDanger(message, 3));

    };
    return NaiveBayes;
}());
NaiveBayes["__class"] = "NaiveBayes";
NaiveBayes.main(null);