package Csv;

import java.util.List;

public interface Format {
	public List<String[]> transform(List<String[]> datas);
	public String encoding();
}
