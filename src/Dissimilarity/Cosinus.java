package Dissimilarity;
import java.util.List;
import Execution.Word;



public class Cosinus implements Dissimilarity {

	@Override
	public double getValue(List<Word> list1, List<Word> list2){
		double sumSquare1 = 0;
		double sumSquare2 = 0;
		double intersection = 0;
		
		// first element is the concept
		int i = 1;
		int j = 1;

		while(i < list1.size() && j < list2.size()){
			Word word1 = list1.get(i);
			Word word2 = list2.get(j);
			int compare = word1.compareTo(word2);

			if(compare == 0){
				intersection += word1.getWeight()*word2.getWeight();
				sumSquare1 += word1.getWeight()*word1.getWeight();
				sumSquare2 += word2.getWeight()*word2.getWeight();
				i++;
				j++;
			}
			else if(compare < 0){
				sumSquare1 += word1.getWeight()*word1.getWeight();
				i++;
			}
			else{
				sumSquare2 += word2.getWeight()*word2.getWeight();
				j++;
			}
		}
		while(i < list1.size()){
			sumSquare1 += list1.get(i).getWeight()*list1.get(i).getWeight();
			i++;
		}
		while(j < list2.size()){
			sumSquare2 += list2.get(j).getWeight()*list2.get(j).getWeight();
			j++;
		}
//		return (int) (precision - 
//					 	(
//					 		(double)precision
//					 			*
//					 		intersection
//					 			/
//					 		(Math.sqrt(sumSquare1 * sumSquare2))
//					 	)
//					 );
		
		return 1- (intersection / (Math.sqrt(sumSquare1 * sumSquare2)));
	}

}
