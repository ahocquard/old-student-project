package Execution;

public class Word implements Comparable<Word>{

	private String value;
	private int weight;


	public Word(String value ){
		this.value = value;
		this.weight = 1;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public int getWeight() {
		return weight;
	}


	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void incrementWeight(){
		weight++;
	}


	@Override
	public boolean equals(Object obj) {
		try {
			Word argWord = ((Word)obj);
			return value.equals(argWord.getValue());
		} catch (ClassCastException e) {
			return false;
		}
	}

	@Override
	public int compareTo(Word word) {
		return this.value.compareTo(word.getValue());
	}


}
