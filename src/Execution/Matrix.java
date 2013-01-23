package execution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import clustering.StepClustering;
import csv.ClusteringFormat;
import csv.DaoConcept;
import csv.Format;
import csv.FormatFactory;
import dissimilarity.Dissimilarity;
import dissimilarity.DissimilarityFactory;



public class Matrix {

	/**
	 * @param args 
	 * args[0] is the name of the measure of dissimilarity selected. It could be "jaccard" or "cosinus", for the moment.
	 * args[1] is the format of output. It could be "orange" or "R", for the moment.
	 * args[2] is the column where is the concept. It's based on 0-index (0 equals "the first column is the concept). Following words are the context. Previous words are ignored.
	 * It is useful if you have a tag for this context.
	 * args[3] is a boolean to specify whether or not it considers "null" words in the context (not really implemented... !). 
	 * args[4] is the name of the white list of words you want use in your HAC and clustering. This file must be in /data. One word per line.
	 */
	public static void main(String[] args) {

		// load arguments
		Dissimilarity dissimilarity = null;
		Format fileFormat = null;
		boolean isWhitelistUsed = false;

		try {
			dissimilarity = DissimilarityFactory.create(args[0]);
			fileFormat = FormatFactory.create(args[1]);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		int columnConcept = Integer.parseInt(args[2]);
		boolean nullWord = Boolean.parseBoolean(args[3]);
		
		String whitelist = null;
		if(args.length > 4){
			whitelist = args[4];
			isWhitelistUsed = true;
		}
		
		// load matrix (concatenation)
		long before = System.currentTimeMillis();
		List<List<Word>> datas = DaoConcept.loadConcepts('|', columnConcept, nullWord, whitelist );
		System.out.println("load done");
		System.out.println((System.currentTimeMillis() - before) / 1000.0);

		// compute the matrix dissimilarity
		Matrix conceptMatrix = new Matrix(dissimilarity);
		List<String[]> dissMatrix = conceptMatrix.getMatrix(datas);
		System.out.println("matrix done");
		System.out.println((System.currentTimeMillis() - before) / 1000.0);

		List<String[]> result = null;
		// write concepts depending on the format file specified in argument
		if(fileFormat != null && dissimilarity != null){
			result = fileFormat.transform(dissMatrix);
			DaoConcept.writeConcepts(result, fileFormat.getSeparator(), fileFormat.getExtension(), dissimilarity.getName(), isWhitelistUsed);
		}
		
		// STEP CLUSTERING
		// should be an option in argument : not clean (lack of time)
		StepClustering neighboorWord = new StepClustering();
		Format clusFormat = new ClusteringFormat();
		Map<String,List<String[]>> files = neighboorWord.getClusters(clusFormat.transform(dissMatrix));
		for(Map.Entry<String, List<String[]>> file : files.entrySet()){
			DaoConcept.writeClusters(file.getValue(), clusFormat.getSeparator(), clusFormat.getExtension(), dissimilarity.getName(), isWhitelistUsed, file.getKey() );
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
		
		// first line = labels
		for(int i=0 ; i < datas.size() ; i++)
			dissimilarities[0][i+1] = datas.get(i).get(0).getValue();

		// a complete matrix (not a semi-matrix)
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
