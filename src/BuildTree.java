import java.util.ArrayList;


public class BuildTree {

	public TreeNode constructTree(ArrayList<ArrayList<String>> records, TreeNode root, int localDepth){
		int bestAttribute = -1;
		int currBest = -1;
		double bestGain = 0;
		double local_bestGain = 0;
		
		ArrayList<Integer> values = new ArrayList<Integer>(){{
		    add(0);
		    add(2);
		    add(4);
		    add(5);
		    add(6);
		    add(8);
		}};
		
		root.setEntropy(Entropy.entropyCal(root.getRecords()));
		//debug
		//System.out.println("RootEn: "+root.getEntropy()+  "    size ="+root.getRecords().size());
		
		
		if(root.getEntropy() == 0.0){
<<<<<<< HEAD
			if(root.getRecords().size() > 0)
				root.setLeafAttribute(Integer.parseInt(root.getRecords().get(0).get(ID3.indexOfClassLabel)), Integer.parseInt(root.getRecords().get(0).get(root.getParent().getTestAttribute())));
=======
			if(root.getRecords().size() > 0){
				if(root.getTestValue().equalsIgnoreCase("Rest"))
					root.setLeafAttribute(Integer.parseInt(root.getRecords().get(0).get(16)), 1);
				else
					root.setLeafAttribute(Integer.parseInt(root.getRecords().get(0).get(16)), Integer.parseInt(root.getRecords().get(0).get(root.getParent().getTestAttribute())));
			}
>>>>>>> master
			else
				System.out.println("0 records classified for :"+root.getParent());
			//debug
			//System.out.println("Leaf Node with parent: "+DataLoader.labels.get(root.getParent().getTestAttribute())+" and Value : "+root.getRecords().get(0).get(root.getParent().getTestAttribute())+" "+root.getRecords().size()+" is : "+root.getLeafAttribute()[root.getTestValue()]);
			//System.out.println("Children : "+root.getChildren().toString());
			return root;	
		}
		int flag = 99;
<<<<<<< HEAD
		for(int i = 0; i < ID3.indexOfClassLabel; i++) {		
=======
		double gain = 0;
		for(int i = 0; i < ID3.NUM_ATTRS - 2; i++) {		
>>>>>>> master
			if(!ID3.isAttributeUsed(i)) {
				if(DataLoader.catFeatures.contains(i)){
					for(int k = 0; k < values.size(); k++){
						double entropy = 0;
						ArrayList<Double> entropies = new ArrayList<Double>();
						ArrayList<Integer> setSizes = new ArrayList<Integer>();
						int val = values.get(k);
						
						ArrayList<ArrayList<String>> subset = subset(root, i, val);
						setSizes.add(subset.size());
						if(subset.size() != 0) {
							entropy = Entropy.entropyCal(subset);
							
							if(flag == i)
							System.out.println("Entropy : "+entropy);
								
							entropies.add(entropy);
						}
						else{
							entropies.add(0.0);
						}
						
						ArrayList<ArrayList<String>> subset_rest = restSubset(root, i, val);
						setSizes.add(subset_rest.size());
						if(subset_rest.size() != 0) {
							entropy = Entropy.entropyCal(subset_rest);
							
							if(flag == i)
							System.out.println("Entropy : "+entropy);
								
							entropies.add(entropy);
						}
						else{
							entropies.add(0.0);
						}
						
						System.out.println("root.getEntropy() ="+root.getEntropy() +  "      entropies= "+entropies+"      setSizes ="+setSizes+ "   root.getRecords().size() = "+root.getRecords().size());
						double local_gain = Entropy.calGain(root.getEntropy(), entropies, setSizes, root.getRecords().size());
						
						if(i==i){
							System.out.println("Gain "+values.get(k)+" value : "+local_gain);
						}
						
						//debug
						//System.out.println("Gain "+k+" value : "+gain);
						if(local_gain > local_bestGain) {
							currBest = values.get(k);
							local_bestGain = local_gain;
						}
					}
					System.out.println("Current Best :"+currBest);
					gain = local_bestGain;
				}
				else{
					double entropy = 0;
					ArrayList<Double> entropies = new ArrayList<Double>();
					ArrayList<Integer> setSizes = new ArrayList<Integer>();
					for(int j = 0; j < 2; j++) {
						ArrayList<ArrayList<String>> subset = subset(root, i, j);
						setSizes.add(subset.size());
						
						if(i == flag)
						System.out.println("SIZE ="+ subset.size());
						
						if(subset.size() != 0) {
							entropy = Entropy.entropyCal(subset);
							
							if(flag == i)
							System.out.println("Entropy : "+entropy);
								
							entropies.add(entropy);
						}
						else{
							entropies.add(0.0);
						}
					}
				
								
					if(i==i)
						System.out.println("root.getEntropy() ="+root.getEntropy() +  "      entropies= "+entropies+"      setSizes ="+setSizes+ "   root.getRecords().size() = "+root.getRecords().size());
					
					gain = Entropy.calGain(root.getEntropy(), entropies, setSizes, root.getRecords().size());
					
					if(i==i){
						System.out.println("Gain "+i+" value : "+gain);
					}
				}
				
				//debug
				//System.out.println("Gain "+i+" value : "+gain);
				if(gain > bestGain) {
					bestAttribute = i;
					bestGain = gain;
				}
			}
        }
		//debug
		//System.out.println("Best Gain:"+bestGain);
		System.out.println("Best Attribute:"+bestAttribute);
		System.out.println("Attribute Name:"+DataLoader.labels.get(bestAttribute));
		System.out.println("=============================================================");
		
		if(bestAttribute != -1) {
			int setSize = Integer.parseInt(Config.readConfig("setSizeOfChild"));
			root.setTestAttribute(bestAttribute);
			root.children = new TreeNode[setSize];
			//root.setUsed(true);
			if(DataLoader.catFeatures.contains(bestAttribute)){
				values.remove(values.indexOf(currBest));
				if(values.isEmpty())
					ID3.usedAttributes.add(bestAttribute);
			}
			else{
				currBest = -1;
				ID3.usedAttributes.add(bestAttribute);
			}
			
			for (int j = 0; j< setSize; j++) {
				root.children[j] = new TreeNode();
				root.children[j].setParent(root);
				if(currBest != -1 && j==0){
					root.children[j].setRecords(subset(root, bestAttribute, currBest));
					root.children[j].setTestValue(Integer.toString(currBest));
				}
				else if(currBest != -1 && j==1){
					root.children[j].setRecords(restSubset(root, bestAttribute, currBest));
					root.children[j].setTestValue("Rest");
				}
				else{
					root.children[j].setRecords(subset(root, bestAttribute, j));
					root.children[j].setTestValue(Integer.toString(j));
				}
				
				
				//debug
				//System.out.println("subset of "+j+"  :"+root.children[j].getRecords().size());
				//System.out.println("Leaf : "+root.children[j].getTestAttribute());
			}
			
//			if(root.getChildren() != null)
//				System.out.println("*Children 0:"+root.getChildren()[0].getTestAttribute());
//				System.out.println("*Children 1:"+root.getChildren()[1].getTestAttribute());
			if(localDepth != 0){
				localDepth--;
				for(int j = 0; j < setSize; j++) {
					constructTree(root.children[j].getRecords(), root.children[j], localDepth);
				}
			}
			root.setRecords(null);
		} 
		else {
			return root;
		}
		
		return root;
		
	}
	
	public ArrayList<ArrayList<String>> subset(TreeNode root, int attr, int value) {
		ArrayList<ArrayList<String>> subset = new ArrayList<ArrayList<String>>();
		
		for(int i = 0; i < root.getRecords().size(); i++) {
			ArrayList<String> record = root.getRecords().get(i);
			if(Integer.parseInt(record.get(attr)) == value)
				subset.add(record);
		}
		return subset;
	}
	
	public ArrayList<ArrayList<String>> restSubset(TreeNode root, int attr, int value) {
		ArrayList<ArrayList<String>> subset = new ArrayList<ArrayList<String>>();
		
		for(int i = 0; i < root.getRecords().size(); i++) {
			ArrayList<String> record = root.getRecords().get(i);
			if(Integer.parseInt(record.get(attr)) != value)
				subset.add(record);
		}
		return subset;
	}
	

}
