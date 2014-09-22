import java.util.ArrayList;


public class Entropy {
	public static double entropyCal(ArrayList<ArrayList<String>> records){
		double entropy = 0;
		
		if(records.size() == 0) {
			// nothing to do
			return 0;
		}
		for(int i = 1; i < ID3.setSize("type"); i++) {
			int count = 0;
			for(int j = 0; j < records.size(); j++) {
				ArrayList<String> record = records.get(j);
				
				if(Integer.parseInt(record.get(ID3.indexOfClassLabel)) == i) {
					count++;
				}
			}
				
			double probability = count / (double)records.size();
			//if(i == 1)
				//System.out.println("count="+ count + "      Recordsize="+records.size());
			if(count > 0) {
				entropy += -probability * (Math.log(probability) / Math.log(2));
			}
		}
		
		return entropy;
	}
	
	public static double calGain(double rootEntropy, ArrayList<Double> subEntropies, ArrayList<Integer> setSizes, int data) {
		double gain = rootEntropy; 
		
		for(int i = 0; i < subEntropies.size(); i++) {
			gain += -((setSizes.get(i) / (double)data) * subEntropies.get(i));
		}
		
		return gain;
	}
		
}
