package Dissimilarity;


public class DissimilarityFactory {

	  public static Dissimilarity create(String name) throws Exception
	  {
	    if("jaccard".equals(name))
	      return new Jaccard();
	 
	    if ("cosinus".equals(name))
	      return new Cosinus();
	 
	    throw new Exception("Impossible de créer un " + name);
	  }

}
