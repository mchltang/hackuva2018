import java.util.*;

public class NaiveBayes {

    public boolean isInDanger(String message, HashMap<String, Double> dictionary, HashMap<String, String> stems, ArrayList<String> stopwords, long threshold) {
        // make dictionary first
        String[] splited = message.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+"); // removes duplicates, makes everything lowercase
        List<String> toProcess = new ArrayList<>(Arrays.asList(splited)); // dump array into list
        for (String currWord : toProcess) {
            if (stems.containsKey(currWord)) { // perform stemming first
                currWord = stems.get(currWord);
            }
        }
        toProcess.removeAll(stopwords); // remove stopwords
        Set<String> noDuplicates = new TreeSet<>(toProcess); // removes duplicates
        Double totalProbability = 1.0;
        for(String currWord : noDuplicates) { // get a probability for each word, multiply into totalProbability
            if(dictionary.containsKey(currWord)) {
                totalProbability *= dictionary.get(currWord);
            }
        }
        if(totalProbability > threshold) {
            return true;
        }
        return false;
    }

}