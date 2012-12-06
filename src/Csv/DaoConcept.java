package Csv;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Execution.Word;
import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class DaoConcept {

	private final static char DEFAULT_SEPARATOR = '|';
	private final static String RESOURCES_PATH = "data/";
//	private final static String CORPUS_FILE_NAME = "cc_sb.csv";
//	private final static String CORPUS_FILE_NAME = "extrait.csv";
	private final static String CORPUS_FILE_NAME = "instances-creme.csv";
//	private final static String CORPUS_FILE_NAME = "test.csv";



	/**
	 * Load every concepts, with their word lists in a csv file. The default separator is "|".
	 * @return A map where the key is the concept name and the value a list of word lists associated to the concept.
	 */
	public static List<List<Word>> loadConcepts(){
		return loadConcepts(DEFAULT_SEPARATOR, 0, false);
	}


	/**
	 * Load every concepts, with their word lists in a csv file. 
	 * @param separator separator in the csv file
	 * @param columnConcept column where the name concept, followed by the context (it's 0-index : it starts at 0)
	 * @param nullWord true if the "null" word should exist, false if we don't take care of this
	 * @return A map where the key is the concept name and the value a list of word strings associated to the concept.
	 */
	public static List<List<Word>> loadConcepts(char separator, int columnConcept, boolean nullWord){

		// faster to create an hashmap and then put all concepts in a arraylist
		Map<String, List<Word>> concepts = new HashMap<String, List<Word>>();

		File file = new File(RESOURCES_PATH + CORPUS_FILE_NAME);
		Reader fr = null;
		CSVReader csvReader = null;

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
						// add the concept if it didn't exist yet 
						// use list and not set because can't get an element with set, and we need to increment weight
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
		
		return result;
	}


	public static void writeConcepts(String filename, List<String[]> datas){
		Writer fw = null;
		CSVWriter csvWriter = null;

		try {
			fw = new BufferedWriter(new OutputStreamWriter(
				    new FileOutputStream(RESOURCES_PATH + CORPUS_FILE_NAME + ".tab"), "UTF-8"));
			//fw = new FileWriter(RESOURCES_PATH + CORPUS_FILE_NAME + ".tab");
			// important : specify null character for quote
			csvWriter = new CSVWriter(fw, '\t', CSVWriter.NO_QUOTE_CHARACTER);
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



}
