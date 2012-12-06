package Csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrangeFormat implements Format{

	@Override
	public List<String[]> transform(List<String[]> datas) {
		// this format add one lines at the start of the document (+1 in size) 
		// this format don't included the label for each column at the first line (-1 in size)
		List<String[]> result = new ArrayList<String[]>(datas.size());
		String[] firstLine = {new Integer(datas.size()-1).toString(), "labeled"};
		result.add(firstLine);
		
		// line length
		int toIndex = 2;
		for(int i=1 ; i<datas.size() ; i++){
			result.add(Arrays.asList(datas.get(i)).subList(0, toIndex).toArray(new String[0]));
			toIndex++;
		}
		
		return result;
	}

	@Override
	public String encoding() {
		return "UTF-8";
	}
	
	
}
