package Execution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Csv.DaoConcept;
import Csv.Format;
import Csv.FormatFactory;
import Dissimilarity.Dissimilarity;
import Dissimilarity.DissimilarityFactory;
import Dissimilarity.Jaccard;


public class Matrix {

	// TODO : corriger les phrases 
	/**
	 * @param args 
	 * args[0] is the name of the dissimilarity selected. It could be "jaccard" or "cosinus".
	 * args[1] is the format of output. It could be "orange" or "R".
	 * args[2] is the column where is the concept. It's based on 0-index (0 equals "the first column is the concept). Following words are the context. Previous words are ignored.
	 * args[3] is a boolean to specify whether or not it considers "null" words in the context. 
	 * 
	 */
	public static void main(String[] args) {

		// load arguments
		Dissimilarity dissimilarity = null;
		Format fileFormat = null;

		try {
			dissimilarity = DissimilarityFactory.create(args[0]);
			fileFormat = FormatFactory.create(args[1]);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		int columnConcept = Integer.parseInt(args[2]);
		boolean nullWord = Boolean.parseBoolean(args[3]);


		// load matrix (concatenation)
		long before = System.currentTimeMillis();
		List<List<Word>> datas = DaoConcept.loadConcepts('|', columnConcept, nullWord );
		System.out.println("load done");
		System.out.println((System.currentTimeMillis() - before) / 1000.0);

		// compute the matrix dissimilarity
		Matrix conceptMatrix = new Matrix(dissimilarity);
		List<String[]> dissMatrix = conceptMatrix.getMatrix(datas);
		System.out.println("matrix done");
		System.out.println((System.currentTimeMillis() - before) / 1000.0);

		// write concepts depending on the format file specified in argument
		if(fileFormat != null){
			List<String[]> result = fileFormat.transform(dissMatrix);
			DaoConcept.writeConcepts("matrix", result);
		}
		System.out.println("write done");


		System.out.println((System.currentTimeMillis() - before) / 1000.0);

	}

	private Dissimilarity dissimilarity;


	public Matrix(Dissimilarity dissimilarity) {
		this.dissimilarity = dissimilarity;
	}


	public List<String[]> getMatrix(List<List<Word>> datas){

		String[][] dissimilarities = new String[datas.size()+1][datas.size()+1];
		dissimilarities[0][0] = "";

		for(int i=0 ; i < datas.size() ; i++)
			dissimilarities[0][i+1] = datas.get(i).get(0).getValue();

		for(int i=0 ; i < datas.size() ; i++){
			dissimilarities[i+1][0] = datas.get(i).get(0).getValue();
			for(int j=0 ; j<i+1 ; j++){
				Double dissValue = new Double(dissimilarity.getValue(datas.get(i), datas.get(j)));
				dissimilarities[i+1][j+1] = dissValue.toString();
				dissimilarities[j+1][i+1] = dissValue.toString();
			}
		}
		return new ArrayList<String[]>(Arrays.asList(dissimilarities));
	}



	public Dissimilarity getDissimilarity() {
		return dissimilarity;
	}

	public void setDissimilarity(Dissimilarity dissimilarity) {
		this.dissimilarity = dissimilarity;
	}






}
