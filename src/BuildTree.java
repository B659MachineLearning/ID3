import java.util.ArrayList;


public class BuildTree {

	public TreeNode constructTree(ArrayList<ArrayList<String>> records, TreeNode root, int localDepth){
		localDepth--;
		
		int bestAttribute = -1;
		int currBest = -1;
		double bestGain = 0;
		double local_bestGain = 0;
		double gain = 0;
		
		ArrayList<Integer> values = null;
		root.setEntropy(Entropy.entropyCal(root.getRecords()));
		//debug
		System.out.println("RootEn: "+root.getEntropy()+  "    size ="+root.getRecords().size());
		
		
		if(root.getEntropy() == 0.0){
			if(root.getRecords().size() > 0){
				root.setLeaf();
				root.setLeafAttribute(root.getRecords().get(0).get(ID3.indexOfClassLabel), 0);
			}
			else{
				System.out.println("0 records classified for :"+root.getParent());
				
			}
			//debug
			//System.out.println("Leaf Node with parent: "+DataLoader.labels.get(root.getParent().getTestAttribute())+" and Value : "+root.getRecords().get(0).get(root.getParent().getTestAttribute())+" "+root.getRecords().size()+" is : "+root.getLeafAttribute()[root.getTestValue()]);
			//System.out.println("Children : "+root.getChildren().toString());
			return root;	
		}
		
		int flag = 99;
		
		int currVal = 0;
		for(int i = 0; i < DataLoader.numberOfFeatures-2; i++) {
			if(!ID3.isAttributeUsed(i) && i!=ID3.indexOfClassLabel) {
				if(DataLoader.catFeatures.contains(i)){
					values = new ArrayList<Integer>();
					for(int m = 0; m< root.getRecords().size(); m++){
						currVal = Integer.parseInt(root.getRecords().get(m).get(i));
						if(!values.contains(currVal))
							values.add(currVal);
					}
					//Find best one v Rest Split
					for(int k = 0; k < values.size(); k++){
						double l_entropy = 0;
						ArrayList<Double> l_entropies = new ArrayList<Double>();
						ArrayList<Integer> l_setSizes = new ArrayList<Integer>();
						int val = values.get(k);
						
						ArrayList<ArrayList<String>> l_subset = subset(root, i, val);
						l_setSizes.add(l_subset.size());
						if(l_subset.size() != 0) {
							l_entropy = Entropy.entropyCal(l_subset);
							
							if(flag == i)
							System.out.println("Entropy : "+l_entropy);
								
							l_entropies.add(l_entropy);
						}
						else{
							l_entropies.add(0.0);
						}
						
						ArrayList<ArrayList<String>> subset_rest = restSubset(root, i, val);
						l_setSizes.add(subset_rest.size());
						if(subset_rest.size() != 0) {
							l_entropy = Entropy.entropyCal(subset_rest);
							
							if(flag == i)
							System.out.println("Entropy : "+l_entropy);
								
							l_entropies.add(l_entropy);
						}
						else{
							l_entropies.add(0.0);
						}
						
						System.out.println("root.getEntropy() ="+root.getEntropy() +  "      entropies= "+l_entropies+"      setSizes ="+l_setSizes+ "   root.getRecords().size() = "+root.getRecords().size());
						double local_gain = Entropy.calGain(root.getEntropy(), l_entropies, l_setSizes, root.getRecords().size());
						
						if(i==i){
							System.out.println("Gain "+values.get(k)+" value : "+local_gain);
						}
						
						//debug
						//System.out.println("Gain "+k+" value : "+gain);
						if(local_gain > local_bestGain) {
							currBest = values.get(k);
							local_bestGain = local_gain;
						}
						else if(local_bestGain == 0){
							currBest = values.get(k);
						}
					}
					System.out.println("------------------------------");
					System.out.println("Current Best :"+currBest);
					System.out.println("local Best Gain :"+local_bestGain);
					System.out.println("------------------------------");
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
				if(bestGain == 0){
					bestAttribute = i;
				}
			}
        }
		//debug
		System.out.println("=============================================================");
		System.out.println("Best Gain:"+bestGain);
		//System.out.println("Best Attribute:"+ID3.getLableName(bestAttribute));
		//System.out.println("Attribute Name:"+DataLoader.labels.get(bestAttribute));
		System.out.println("Test value = "+currBest);
		System.out.println("=============================================================");
		
		if(bestAttribute != -1) {
			int setSize = Integer.parseInt(Config.readConfig("setSizeOfChild"));
			root.setTestAttribute(bestAttribute);
			root.children = new TreeNode[setSize];
			//root.setUsed(true);
			Boolean isCatAttr = DataLoader.catFeatures.contains(bestAttribute);
			
			if(isCatAttr){
				//if(values.size() == 1)
				//	ID3.usedAttributes.add(bestAttribute);
				root.setTestValue(Integer.toString(currBest), 0);
				root.setTestValue("Rest", 1);
			}
			else{
				ID3.usedAttributes.add(bestAttribute);
				root.setTestValue("0", 0);
				root.setTestValue("1", 1);
			}
			
			for (int j = 0; j< setSize; j++) {
				root.children[j] = new TreeNode();
				root.children[j].setParent(root);
				//root.setTestValue(Integer.toString(currBest));
				if(isCatAttr && j==0){
					root.children[j].setRecords(subset(root, bestAttribute, currBest));
				}
				else if(isCatAttr && j==1){
					root.children[j].setRecords(restSubset(root, bestAttribute, currBest));
				}
				else{
					root.children[j].setRecords(subset(root, bestAttribute, j));
				}
				
				
				//debug
				//System.out.println("subset of "+j+"  :"+root.children[j].getRecords().size());
				//System.out.println("Leaf : "+root.children[j].getTestAttribute());
			}
			
//			if(root.getChildren() != null)
//				System.out.println("*Children 0:"+root.getChildren()[0].getTestAttribute());
//				System.out.println("*Children 1:"+root.getChildren()[1].getTestAttribute());
			
			System.out.println("##############################################################");
			if(root.getParent() != null)
			System.out.println("Parent : "+ID3.getLableName(root.getParent().getTestAttribute()));
			System.out.println("Test Attribute : "+ID3.getLableName(root.getTestAttribute()));
			System.out.println("Left Test : "+root.getTestValue()[0]);
			System.out.println("Right Test : "+root.getTestValue()[1]);
			System.out.println("Left Leaf Attr : "+root.getLeafAttribute()[0]);
			System.out.println("Right Leaf Attr : "+root.getLeafAttribute()[1]);
			System.out.println("##############################################################");
			
			if(localDepth != 0){
				for(int j = 0; j < setSize; j++) {
					constructTree(root.children[j].getRecords(), root.children[j], localDepth);
				}
			}
			else{
				return root;
			}
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
