package dissimilarity;

import java.util.List;

import execution.Word;




public class Jaccard implements Dissimilarity {

	@Override
	public double getValue(List<Word> list1, List<Word> list2){
		double total = 0;
		double intersection = 0;
		
		// first element is the concept
		int i = 1;
		int j = 1;

		while(i < list1.size() && j < list2.size()){
			Word word1 = list1.get(i);
			Word word2 = list2.get(j);
			int compare = word1.compareTo(word2);

			if(compare == 0){
				intersection += (word1.getWeight() <= word2.getWeight() ? word1.getWeight() : word2.getWeight());
				total += (word1.getWeight() >= word2.getWeight() ? word1.getWeight() : word2.getWeight());
				i++;
				j++;
			}
			else if(compare < 0){
				total += word1.getWeight();
				i++;
			}
			else{
				total += word2.getWeight();
				j++;
			}
		}
		while(i < list1.size()){
			total += list1.get(i).getWeight();
			i++;
		}
		while(j < list2.size()){
			total += list2.get(j).getWeight();
			j++;
		}
		return 1-intersection/total;
	}

	@Override
	public String getName() {
		return "jaccard";
	}
	
	
}
