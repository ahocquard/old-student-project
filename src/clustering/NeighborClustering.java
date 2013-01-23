package clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * LACK OF TIME : not well included in th eproject with args and main.
 * Find clusters with neighborhood distance.
 * 
 * @author hoki
 *
 */
public class NeighborClustering  {

	public Map<String,List<String[]>> getClusters(List<String[]> matrix) {
		Double jump = 0.001;
		Map<String, List<String[]>> result = new HashMap<String, List<String[]>>();
		List<String[]> previousClusters = null;

		for(Double step = 0.001; step <= 1 ; step += jump){
			int lineIndex = matrix.size()-1;
			// don't forget, first column is label
			int columnIndexMax = matrix.size();

			// set for contains in O(1)
			List<Set<String>> clusters = new ArrayList<Set<String>>();

			do {
				int columnIndex = columnIndexMax;
				// get the numero of the cluster
				int numCluster = getNumCluster(matrix.get(lineIndex)[0], clusters);
				// if not in any cluster, add a cluster
				if(numCluster < 0){
					clusters.add(new HashSet<String>());
					numCluster = clusters.size()-1;
					clusters.get(numCluster).add(matrix.get(lineIndex)[0]);
				}
				do {
					// not clean at all, should be double without parsing but lack of time
					if(Double.parseDouble(matrix.get(lineIndex)[columnIndex]) < step){
						// -1 because label
						//clusters.get(numCluster).add(matrix.get(columnIndex-1)[0]);
						
						int numCLusterNeighbor = getNumCluster(matrix.get(columnIndex-1)[0], clusters);
						
						// add this concept in the cluster
						if(numCLusterNeighbor < 0 ){
							clusters.get(numCluster).add(matrix.get(columnIndex-1)[0]);
						}
						// join the two clusters
						else if (numCLusterNeighbor != numCluster){
							// join cluster because 1 neighbor in common
							clusters.get(numCLusterNeighbor).addAll(clusters.get(numCluster));
							clusters.remove(numCluster);
							
							// update numCluster
							// warning : remove shift index or not
							if(numCluster < numCLusterNeighbor)
								numCluster = numCLusterNeighbor-1;
							else
								numCluster = numCLusterNeighbor;
						}
					}
					columnIndex--;
				} while (columnIndex >= 1);
				lineIndex--;
				columnIndexMax--;
			} while (lineIndex >=0);

			// transform set in arraylist
			List<String[]> clustersToArray = new ArrayList<String[]>();
			for(Set<String> clust : clusters){
				clustersToArray.add(clust.toArray(new String[0]));
			}
			// avoid some round error like 0,4999999
			String[] head = {new Double(step/step*step).toString()};
			clustersToArray.add(0, head);
			
			if(!sameClusters(clustersToArray, previousClusters)){
				result.put(step.toString(), clustersToArray);
				previousClusters = clustersToArray ;
			}
		}
		return result;
	}

	private boolean sameClusters(List<String[]> clusters, List<String[]> previousClusters){
		boolean sameLength = true;
		if(previousClusters == null){
			sameLength = false;
		}
		else if(clusters.size() != previousClusters.size()){
			sameLength = false;
		}
		else{
			int i = 0;
			while(i<clusters.size() && sameLength){
				if(clusters.get(i).length != previousClusters.get(i).length)
					sameLength = false;
				i++;
			}
		}
		return sameLength;
	}


	private int getNumCluster(String word,List<Set<String>> clusters) {
		int indexCluster = 0;
		boolean find = false;
		while(!find && indexCluster < clusters.size()){
			if(clusters.get(indexCluster).contains(word))
				find = true;
			else
				indexCluster++;
		}
		return find ? indexCluster : -1;
	}

}
