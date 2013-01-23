package dissimilarity;


import java.util.List;

import execution.Word;



public interface Dissimilarity {
	public double getValue(List<Word> list1, List<Word> list2);
	public String getName();
}
