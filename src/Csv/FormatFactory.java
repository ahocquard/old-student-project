package Csv;

import Dissimilarity.Cosinus;
import Dissimilarity.Dissimilarity;
import Dissimilarity.Jaccard;

public class FormatFactory {

	public static Format create(String name) throws Exception
	{
		if("orange".equals(name))
			return new OrangeFormat();

		if ("R".equals(name))
			return new RFormat();

		throw new Exception("Impossible de créer un " + name);
	}

}
