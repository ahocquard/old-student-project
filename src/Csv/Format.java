package csv;

import java.util.List;

public interface Format {
	public List<String[]> transform(List<String[]> datas);
	public String getEncoding();
	public char getSeparator();
	public String getExtension();
	public String getName();
}
