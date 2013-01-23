package csv;

import java.util.List;

public class RFormat implements Format {

	@Override
	public List<String[]> transform(List<String[]> datas) {
		return datas;
	}

	@Override
	public String getEncoding() {
		return "UTF-8";
	}

	@Override
	public char getSeparator() {
		return '|';
	}

	@Override
	public String getExtension() {
		return "R";
	}

	@Override
	public String getName() {
		return "R";
	}

	
}

//public static TreeMap<String, List<String>> inversionMatrix(
//		TreeMap<String, List<String>> matrix) {
//	TreeMap<String, List<String>> newMatrix = new TreeMap<String, List<String>>(
//		new Comparator<String>() {
//			@Override
//			public int compare(String o1, String o2) {
//				return -o1.compareTo(o2);
//			}
//		}
//	);
//	newMatrix.putAll(matrix);
//	return newMatrix;
//}