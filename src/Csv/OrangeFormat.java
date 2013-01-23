package csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrangeFormat implements Format{

	
	@Override
	public List<String[]> transform(List<String[]> datas) {
		// this format add one lines at the start of the document (+1 in size) 
		// this format don't included the label for each column at the first line (-1 in size)
		List<String[]> result = new ArrayList<String[]>(datas.size());
		// return just the matrix without any column label and head
		result.addAll(new ClusteringFormat().transform(datas));
		
		// add the head of the orange format
		String[] firstLine = {new Integer(datas.size()-1).toString(), "labeled"};
		result.add(0,firstLine);
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
		return "tab";
	}

	@Override
	public String getName() {
		return "orange";
	}
	
	
}
