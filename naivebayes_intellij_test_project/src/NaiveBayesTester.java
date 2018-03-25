import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class NaiveBayesTester {
	
	private String processMessage(String msg, long threshold, HashMap<String, Long> reference) {
		return null;
	}
	
	public static HashMap<String, String> readStems(String filename) {
		HashMap<String, String> ret = new HashMap<String, String>();
		try {
			File file = new File(filename);
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				String[] words = line.split("\\s+");
				if(words.length != 2) {
                    System.out.println("error: split error");
					continue;
				}
				ret.put(words[1], words[0]);
			}
			return ret;
		} catch (FileNotFoundException e) {
            e.printStackTrace();
		}
		return ret;
	}

	public static ArrayList<String> readStopWords(String filename) {
		ArrayList<String> ret = new ArrayList<String>();
		try {
			File file = new File(filename);
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				ret.add(line);
			}
			return ret;
		} catch (FileNotFoundException e) {
            e.printStackTrace();
		}
		return ret;
	}
	public static HashMap<String, HashMap<String, Long>> readProbabilities(String filename){
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(filename));
			JSONObject jsonObject = (JSONObject) obj;
			return jsonObject;
		} catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
		return null;
	}
	public static void convertStemsToJSON(String filename1, String filename2) {
		JSONObject jsonObject = new JSONObject();
		try {
			File file = new File(filename1);
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				String[] words = line.split("\t");
				if(words.length < 2) {
					continue;
				}
				jsonObject.put(words[1], words[0]);
				
			}
			FileWriter fw = new FileWriter(filename2);
	        fw.write(jsonObject.toJSONString());
	        fw.flush();
		} catch (FileNotFoundException e) {
            e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void convertStopWordsToJSON(String filename1, String filename2) {
		JSONArray jsonArray = new JSONArray();
		try {
			File file = new File(filename1);
			Scanner scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				jsonArray.add(line);
			}
			FileWriter fw = new FileWriter(filename2);
	        fw.write(jsonArray.toJSONString());
	        fw.flush();
		} catch (FileNotFoundException e) {
            e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
        }
	}

	public static boolean isInDanger(String message, HashMap<String, Long> dictionary, HashMap<String, String> stems, ArrayList<String> stopwords, long threshold) {
		// make dictionary first
		String[] splited = message.replaceAll("\\p{P}", "").toLowerCase().split("\\s+"); // makes everything lowercase
		List<String> toProcess = new ArrayList<>(Arrays.asList(splited)); // dump array into list

        List<String> stemmedWords = new ArrayList<>(); // perform stemming first
		for (String currWord : toProcess) {
			if (stems.containsKey(currWord)) {
				stemmedWords.add(stems.get(currWord));
			}
			else {
			    stemmedWords.add(currWord);
            }
		}

        // remove punctuation from stopwords provided
        List<String> stopwordsNoPunctuation = new ArrayList<>();
		for(String currWord : stopwords) {
		    stopwordsNoPunctuation.add(currWord.replaceAll("\\p{P}", ""));
        }

		stemmedWords.removeAll(stopwordsNoPunctuation); // remove stopwords

		Set<String> noDuplicates = new TreeSet<>(stemmedWords); // removes duplicates

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

	public static void main(String[] args) {
		// read all three text files
		HashMap<String, String> stems = readStems("stems.txt");
		ArrayList<String> stopwords = readStopWords("stopwords.txt");
		HashMap<String, HashMap<String, Long>> probabilities = readProbabilities("text.json");
		HashMap<String, Long> probabilitiesPolice = probabilities.get("Police");
        HashMap<String, Long> probabilitiesFire = probabilities.get("Fire");
        HashMap<String, Long> probabilitiesMedical = probabilities.get("Medical");

        String testInput1 = "Car accident on I-95. Gasoline fire. Backup needed.";
        String testInput2 = "My dog sparky has eaten too much food for two days now. He's so fat";
        String testInput3 = "House across the street on fire. Granny stuck inside. Lots of smoke. Severe burns.";
        String testStemming = "shouldn't don't can't won't can't y'all 'cuz";

        System.out.println(isInDanger(testInput3, probabilitiesPolice, stems, stopwords, 100000));
        System.out.println(isInDanger(testInput3, probabilitiesFire, stems, stopwords, 100000));
        System.out.println(isInDanger(testInput3, probabilitiesMedical, stems, stopwords, 100000));
	}
}