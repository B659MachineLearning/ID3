/*
 * Authors : Aniket Bhosale and Mayur Tare
 * 
 * Description :
 * Class to Calculate Entropy and Information gain required for feature selection in ID3.
 */

import java.util.ArrayList;

public class Entropy {
	
	//Calculate Entropy
	public static double entropyCal(ArrayList<ArrayList<String>> records){
		double entropy = 0;
		
		if(records.size() == 0) {
			return 0;
		}
		for(int i = 0; i < ID3.classVals; i++) {
			int count = 0;
			for(int j = 0; j < records.size(); j++) {
				ArrayList<String> record = records.get(j);
				if(record.get(ID3.indexOfClassLabel).equalsIgnoreCase(ID3.possClasses.get(i))) {
					count++;
				}
			}
				
			double probability = count / (double)records.size();
			if(count > 0) {
				entropy += -probability * (Math.log(probability) / Math.log(2));
			}
		}
		
		return entropy;
	}
	
	//Calculate Information Gain
	public static double calGain(double rootEntropy, ArrayList<Double> subEntropies, ArrayList<Integer> setSizes, int data) {
		double gain = rootEntropy; 
		
		for(int i = 0; i < subEntropies.size(); i++) {
			gain += -((setSizes.get(i) / (double)data) * subEntropies.get(i));
		}
		
		return gain;
	}
		
}
