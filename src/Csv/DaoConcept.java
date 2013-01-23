package csv;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import execution.Word;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class DaoConcept {

	private final static char DEFAULT_SEPARATOR = '|';
	private final static String DATA_PATH = "data/";
	private final static String CORPUS_FILE_NAME = "instances-creme.csv";

	/**
	 * Load every concepts, with their word lists in a csv file. The default separator is "|".
	 * @return A map where the key is the concept name and the value a list of word lists associated to the concept.
	 */
	public static List<List<Word>> loadConcepts(){
		return loadConcepts(DEFAULT_SEPARATOR, 0, false, null);
	}


	/**
	 * Load every concepts, with their word lists in a csv file. 
	 * @param separator separator in the csv file
	 * @param columnConcept column where the name concept, followed by the context (it's 0-index : it starts at 0)
	 * @param nullWord true if the "null" word should exist, false if we don't take care of this. NOT USED.
	 * @param whitelist name of the file of the words allowed to be in the distance matrix. One word per line. This file must be in /data. This argument is OPTIONNAL.
	 * @return A map where the key is the concept name and the value a list of word strings associated to the concept.
	 */
	public static List<List<Word>> loadConcepts(char separator, int columnConcept, boolean nullWord, String whitelist){

		File file = new File(DATA_PATH + CORPUS_FILE_NAME);
		Reader fr = null;
		CSVReader csvReader = null;

		Set<String> wordAllowed = new HashSet<String>();

		// read whitelist file
		// could be cleaner but lack of time
		if(whitelist != null){
			InputStream ips = null; // warning = false positive
			BufferedReader br = null;
			try {
				ips = new FileInputStream(DATA_PATH + whitelist);
				final Reader utfReader = new InputStreamReader(ips, "UTF-8");
				br= new BufferedReader(utfReader);

				String line;
				while ((line=br.readLine())!=null){
					wordAllowed.add(line);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				try {
					if(br != null)
						br.close();
					if(ips != null)
						ips.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// faster to create an hashmap and then put all concepts in an arraylist
		Map<String, List<Word>> concepts = new HashMap<String, List<Word>>();



		try {
			fr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			// important : specify null character for quote
			csvReader = new CSVReader(fr, separator, CSVParser.NULL_CHARACTER);

			String[] nextLine = null;
			while ((nextLine = csvReader.readNext()) != null) {
				// avoid empty line
				if (nextLine.length != 0){
					String start = nextLine[0].trim();
					if (!((start.length() == 0 && nextLine.length == 1) || start.startsWith("#"))) {
						// filter : no white list or check if it's in the whitelist
						if(whitelist == null || wordAllowed.contains(nextLine[columnConcept])){
							// add the concept if it didn't exist yet 
							// use list and not set because can't get an element with set, and we need to increment the weight of each word
							if(!concepts.containsKey(nextLine[columnConcept]) && !nextLine[columnConcept].equals(new String("null")))			
								concepts.put(nextLine[columnConcept], new ArrayList<Word>());

							// add a new word list for a concept
							for(int i = columnConcept+1 ; i < nextLine.length ; i++){
								Word word = new Word(nextLine[i]);
								if(!word.equals(new String("null")) || nullWord){
									int index = concepts.get(nextLine[columnConcept]).indexOf(word);
									if(index >= 0){
										concepts.get(nextLine[columnConcept]).get(index).incrementWeight();
									}
									else{
										concepts.get(nextLine[columnConcept]).add(word);
									}
								}
							}
						}
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(fr != null){
					fr.close();
				}
				if(csvReader != null){
					csvReader.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<List<Word>> result = new ArrayList<List<Word>>(concepts.size());
		for(Map.Entry<String, List<Word>> concept : concepts.entrySet()){
			Collections.sort(concept.getValue());
			concept.getValue().add(0, new Word(concept.getKey()));
			result.add(concept.getValue());
		}

		return result; // warning : false positive
	}


	public static void writeFile(List<String[]> datas, char separator, String path){
		Writer fw = null;
		CSVWriter csvWriter = null;
		try {
			fw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path), "UTF-8"));
			// no quote surround the value
			csvWriter = new CSVWriter(fw, separator, CSVWriter.NO_QUOTE_CHARACTER);
			csvWriter.writeAll(datas);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(fw != null)
					fw.close();
				if(csvWriter != null)
					csvWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static void writeConcepts(List<String[]> datas, char separator, String extension, String measure, boolean whitelist){

		String wordsAllowed = getWordsAllowed(whitelist);
		String path = DATA_PATH + measure + "-" + wordsAllowed + "-" + CORPUS_FILE_NAME + "." + extension ;
		DaoConcept.writeFile(datas, separator, path);
		
	}

	public static void writeClusters(List<String[]> datas, char separator, String extension, String measure, boolean whitelist, String step){
		String wordsAllowed = getWordsAllowed(whitelist);
		
		String stepTroncate = step;
		if(step.length() > 5){
			stepTroncate = step.substring(0, 5);
		}
		
		String path = DATA_PATH + measure + "-"+ stepTroncate + "-" + wordsAllowed + "-" + CORPUS_FILE_NAME + "." + extension ;
		DaoConcept.writeFile(datas, separator, path);
	}
	
	private static String getWordsAllowed(boolean whitelist){
		String wordsAllowed = "allwords";
		if(whitelist)
			wordsAllowed = "whitelist";
		return wordsAllowed;
	}
}
