import java.util.ArrayList;


public class BuildTree {

	public TreeNode constructTree(ArrayList<ArrayList<String>> records, TreeNode root, int localDepth){
		int bestAttribute = -1;
		double bestGain = 0;		
		
		root.setEntropy(Entropy.entropyCal(root.getRecords()));
		//debug
		//System.out.println("RootEn: "+root.getEntropy()+  "    size ="+root.getRecords().size());
		
		
		if(root.getEntropy() == 0.0){
			if(root.getRecords().size() > 0)
				root.setLeafAttribute(Integer.parseInt(root.getRecords().get(0).get(ID3.indexOfClassLabel)), Integer.parseInt(root.getRecords().get(0).get(root.getParent().getTestAttribute())));
			else
				System.out.println("0 records classified for :"+root.getParent());
			//debug
			//System.out.println("Leaf Node with parent: "+DataLoader.labels.get(root.getParent().getTestAttribute())+" and Value : "+root.getRecords().get(0).get(root.getParent().getTestAttribute())+" "+root.getRecords().size()+" is : "+root.getLeafAttribute()[root.getTestValue()]);
			//System.out.println("Children : "+root.getChildren().toString());
			return root;	
		}
		int flag = 99;
		for(int i = 0; i < DataLoader.numberOfFeatures; i++) {	
			if(i == ID3.indexOfClassLabel){
				continue;
			}
			if(!ID3.isAttributeUsed(i)) {
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
								
				if(i==flag)
					System.out.println("root.getEntropy() ="+root.getEntropy() +  "      entropies= "+entropies+"      setSizes ="+setSizes+ "   root.getRecords().size() = "+root.getRecords().size());
				double gain = Entropy.calGain(root.getEntropy(), entropies, setSizes, root.getRecords().size());
				if(i==flag){
					System.out.println("Gain : "+ gain);
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
		//System.out.println("Best Attribute:"+bestAttribute);
		//System.out.println("Attribute Name:"+DataLoader.labels.get(bestAttribute));
		
		if(bestAttribute != -1) {
			int setSize = Integer.parseInt(Config.readConfig("setSizeOfChild"));
			root.setTestAttribute(bestAttribute);
			root.children = new TreeNode[setSize];
			//root.setUsed(true);
			ID3.usedAttributes.add(bestAttribute);
			
			for (int j = 0; j< setSize; j++) {
				root.children[j] = new TreeNode();
				root.children[j].setParent(root);
				root.children[j].setRecords(subset(root, bestAttribute, j));
				//debug
				//System.out.println("subset of "+j+"  :"+root.children[j].getRecords().size());
				root.children[j].setTestValue(j);
				//root.children[j].getTestAttribute().setName(Hw1.getLeafNames(bestAttribute, j));
				//root.children[j].setTestAttribute(bestAttribute);
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
}
