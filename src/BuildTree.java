/*
 * Authors : Aniket Bhosale and Mayur Tare
 * 
 * Description :
 * Class to Build a Decision tree using ID3 algorithm.
 * Concept of Entropy and Informarion gain is used to select Attributes at each level
 */

import java.util.ArrayList;

public class BuildTree {
	
	//Function to build tree.
	public TreeNode constructTree(ArrayList<ArrayList<String>> records, TreeNode root, int localDepth){
		localDepth--;
		
		int bestAttribute = -1;
		int currBest = -1;
		double bestGain = 0;
		double local_bestGain = 0;
		double gain = 0;
		
		ArrayList<Integer> values = null;
		
		//Calculate Root Entropy for the subset of Tree node passed
		root.setEntropy(Entropy.entropyCal(root.getRecords()));		
		
		//If Root Entropy is 0 then assign leaf attribute to the node.
		if(root.getEntropy() == 0.0){
			if(root.getRecords().size() > 0){
				root.setLeaf();
				root.setLeafAttribute(root.getRecords().get(0).get(ID3.indexOfClassLabel), 0);
			}
			else{
				System.out.println("0 records classified for :"+root.getParent());	
			}
			return root;	
		}
		int currVal = 0;
		//Loop over all features
		for(int i = 0; i < DataLoader.numberOfFeatures-2; i++) {
			//Check if the attribute is used in the Tree or if it a Target Class.
			//if Yes then Discard
			if(!ID3.isAttributeUsed(i) && i!=ID3.indexOfClassLabel) {
				//Check if Feature is a categorical feature or not.
				if(DataLoader.catFeatures.contains(i)){
					values = new ArrayList<Integer>();
					//Get all possible unique values of the feature
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
						
						//Calculate Entropy for subset of records having attribute value equal to the current feature value
						ArrayList<ArrayList<String>> l_subset = subset(root, i, val, "equalto");
						l_setSizes.add(l_subset.size());
						if(l_subset.size() != 0) {
							l_entropy = Entropy.entropyCal(l_subset);								
							l_entropies.add(l_entropy);
						}
						else{
							l_entropies.add(0.0);
						}
						
						ArrayList<ArrayList<String>> subset_rest = subset(root, i, val, "rest");
						l_setSizes.add(subset_rest.size());
						if(subset_rest.size() != 0) {
							l_entropy = Entropy.entropyCal(subset_rest);								
							l_entropies.add(l_entropy);
						}
						else{
							l_entropies.add(0.0);
						}
						
						//Calculate gain
						double local_gain = Entropy.calGain(root.getEntropy(), l_entropies, l_setSizes, root.getRecords().size());
						
						//Find the local best feature value for categorical attribute
						if(local_gain > local_bestGain) {
							currBest = values.get(k);
							local_bestGain = local_gain;
						}
						else if(local_bestGain == 0){
							currBest = values.get(k);
						}
					}
					gain = local_bestGain;
				}
				else{
					double entropy = 0;
					ArrayList<Double> entropies = new ArrayList<Double>();
					ArrayList<Integer> setSizes = new ArrayList<Integer>();
					for(int j = 0; j < 2; j++) {
						ArrayList<ArrayList<String>> subset = subset(root, i, j, "equalto");
						setSizes.add(subset.size());
						
						if(subset.size() != 0) {
							entropy = Entropy.entropyCal(subset);
								
							entropies.add(entropy);
						}
						else{
							entropies.add(0.0);
						}
					}
					gain = Entropy.calGain(root.getEntropy(), entropies, setSizes, root.getRecords().size());
				}
				//Find the Attribute having best gain value
				if(gain > bestGain) {
					bestAttribute = i;
					bestGain = gain;
				}
				//if all attribute have same gain then select the last one
				if(bestGain == 0){
					bestAttribute = i;
				}
			}
        }
		
		if(bestAttribute != -1) {
			int setSize = Integer.parseInt(Config.readConfig("setSizeOfChild"));
			root.setTestAttribute(bestAttribute);
			root.children = new TreeNode[setSize];
			Boolean isCatAttr = DataLoader.catFeatures.contains(bestAttribute);
			
			if(isCatAttr){
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
				if(isCatAttr && j==0){
					root.children[j].setRecords(subset(root, bestAttribute, currBest, "equalto"));
				}
				else if(isCatAttr && j==1){
					root.children[j].setRecords(subset(root, bestAttribute, currBest, "rest"));
				}
				else{
					root.children[j].setRecords(subset(root, bestAttribute, j, "equalto"));
				}
			}
			
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
	
	//for type = "equalto" : Find subset of records having value of Attribute(attr) passed equal to the value passed.
	//for type = "rest" : Find subset of records having value of Attribute(attr) passed not equal to the value passed.
 	public ArrayList<ArrayList<String>> subset(TreeNode root, int attr, int value, String type) {
		ArrayList<ArrayList<String>> subset = new ArrayList<ArrayList<String>>();
		
		for(int i = 0; i < root.getRecords().size(); i++) {
			ArrayList<String> record = root.getRecords().get(i);
			if(type.equalsIgnoreCase("equalto")){
				if(Integer.parseInt(record.get(attr)) == value)
					subset.add(record);
			}
			else{
				if(Integer.parseInt(record.get(attr)) != value)
					subset.add(record);
			}
		}
		return subset;
	}
}
