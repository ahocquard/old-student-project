package Dissimilarity;


import java.util.List;

import Execution.Word;


public interface Dissimilarity {
	public int getValue(List<Word> list1, List<Word> list2, int precision);
}
