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

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// load arguments
		Dissimilarity dissimilarity = null;
		Format fileFormat = null;
		
		try {
			dissimilarity = DissimilarityFactory.create(args[0]);
			fileFormat = FormatFactory.create(args[3]);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		int precision = Integer.parseInt(args[1]);
		int selected = Integer.parseInt(args[2]);
		
		
		
		// load matrix (concatenation)
		long before = System.currentTimeMillis();
		List<List<Word>> datas = DaoConcept.loadConcepts();
		System.out.println("load done");
		System.out.println((System.currentTimeMillis() - before) / 1000.0);
		
		// compute the matrix dissimilarity
		Matrix conceptMatrix = new Matrix(dissimilarity, precision, selected);
		List<String[]> dissMatrix = conceptMatrix.getMatrix(datas);
		System.out.println("matrix done");
		System.out.println((System.currentTimeMillis() - before) / 1000.0);
		
		// write concepts depending on the format file specified in argument 
		List<String[]> result = fileFormat.transform(dissMatrix);
		DaoConcept.writeConcepts("matrix", result);
		System.out.println("write done");
		
		
		System.out.println((System.currentTimeMillis() - before) / 1000.0);

	}
	
	private int selected;
	private int precision;
	private Dissimilarity dissimilarity;
	

	public Matrix(Dissimilarity dissimilarity, int precision, int selected) {
		this.precision = precision;
		this.dissimilarity = dissimilarity;
		this.selected = selected;
	}

	public Matrix(Dissimilarity dissimilarity, int precision) {
		this(dissimilarity, precision, -1);
	}
	
	public Matrix(Dissimilarity dissimilarity) {
		this(dissimilarity, 1000, -1);
	}

	public Matrix() {
		this(new Jaccard(), 1000, -1);
	}
	
	
	public List<String[]> getMatrix(List<List<Word>> datas){

		String[][] dissimilarities = new String[datas.size()+1][datas.size()+1];
		dissimilarities[0][0] = "";
		
		for(int i=0 ; i < datas.size() ; i++)
			dissimilarities[0][i+1] = datas.get(i).get(0).getValue();
		
		for(int i=0 ; i < datas.size() ; i++){
			dissimilarities[i+1][0] = datas.get(i).get(0).getValue();
			for(int j=0 ; j<i+1 ; j++){
				Integer dissValue = new Integer(dissimilarity.getValue(datas.get(i), datas.get(j), precision));
				dissimilarities[i+1][j+1] = dissValue.toString();
				dissimilarities[j+1][i+1] = dissValue.toString();
			}
		}
		return new ArrayList<String[]>(Arrays.asList(dissimilarities));
	}

	
	public int getPrecision() {
		return precision;
	}


	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public Dissimilarity getDissimilarity() {
		return dissimilarity;
	}

	public void setDissimilarity(Dissimilarity dissimilarity) {
		this.dissimilarity = dissimilarity;
	}






}
