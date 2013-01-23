package csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This format is useful to compute distance with clustering algorithms. There is no head, just a semi-matrix. 
 * It's like the orange format but without the first labeled line.
 * 
 * @author Alexandre Hocquard
 *
 */
public class ClusteringFormat implements Format {

	@Override
	public List<String[]> transform(List<String[]> datas) {
		// this format add one lines at the start of the document (+1 in size) 
		// this format don't included the label for each column at the first line (-1 in size)
		List<String[]> result = new ArrayList<String[]>(datas.size());
		// at the first line, we get only labeled + first value (it's a semi-matrix)
		int toIndex = 2;
		// i=1 because we don't need the first line (it's column labels)
		for(int i=1 ; i<datas.size() ; i++){
			result.add(Arrays.asList(datas.get(i)).subList(0, toIndex).toArray(new String[0]));
			// we get one more value to build the semi-matrix
			toIndex++;
		}
		
		return result;
	}

	@Override
	public String getEncoding() {
		return "UTF-8";
	}

	@Override
	public char getSeparator() {
		return '\t';
	}

	@Override
	public String getExtension() {
		return "csv";
	}

	@Override
	public String getName() {
		return "clusteringFormat";
	}

}
