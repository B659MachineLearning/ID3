package com.machinelearning.classification.ID3;
/*
 * Authors : Aniket Bhosale and Mayur Tare
 * 
 * Description :
 * Class to start the ID3 algorithm.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

public class ID3 {
	 public static int classVals = 0;
	 public static ArrayList<String> possClasses = new ArrayList<String>();
	 public static ArrayList<Integer> usedAttributes = new ArrayList<Integer>();
	 public static int count = 0;
	 public static int depth = 0;
	 public static int indexOfClassLabel;
	 public static String classLabel = Config.readConfig("classLable");
	 
	 public static HashMap<Integer, Integer> prediction = new  HashMap<Integer, Integer>();
	  
	 public static void main(String args[]) {
		ArrayList<ArrayList<String>> records = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> test_records = new ArrayList<ArrayList<String>>();
		BuildTree t = new BuildTree();
		 
		depth = Integer.parseInt(Config.readConfig("depth"));
		
		//Read the data in CSV file with attributes and examples
		String trainFilePath = Config.readConfig("trainFileName"); 
		records = DataLoader.readRecords(trainFilePath);
		 
		System.out.println("Number of examples : "+records.size());
		System.out.println("Lables : "+DataLoader.labels.toString());
		
		//Index of classLabel
		indexOfClassLabel = DataLoader.labels.indexOf(classLabel);
		
		TreeNode root = new TreeNode();
		
		for(ArrayList<String> record : records) {
			root.getRecords().add(record);
		}
		
		classVals = setSize(classLabel, root);
		
		//Construct Decision tree using Training set.
		t.constructTree(records, root, depth);
		
		//Assign LeafAttributes to the missed Leaf Nodes 
		assignLeaves(root);
		
		System.out.println("---------------------------------");
		System.out.println("Tree for Depth "+depth+" : \n---------------------------------");
		printTree(root);
		System.out.println("--------------------------------");
		
		String testFilePath = Config.readConfig("testFileName");
		test_records = DataLoader.readRecords(testFilePath);
		System.out.println("Test Report : \n--------------------------------");
		for(int i =0 ; i<test_records.size(); i++){
			traverseTree(test_records.get(i), root);
		}
		System.out.println("======================================");
		System.out.println(count+" Correct predictions out of "+test_records.size());
		System.out.println("Prediction : "+prediction.toString());
		System.out.println("======================================");
		double accuracy = ((double)count/(double)test_records.size())*100;
		System.out.println("Accuracy for Depth "+depth+" : "+accuracy+"%");
		
		return;
	}
	
	//Traverse the tree to predict for the input record 
	public static void traverseTree(ArrayList<String> r, TreeNode root) {
		if(root.isLeaf()){			
			int key = Integer.parseInt(r.get(indexOfClassLabel));
			if(key == Integer.parseInt(root.getLeafAttribute())){
				if(!prediction.containsKey(key))
					prediction.put(key, 1);
				else
					prediction.put(key, prediction.get(key)+1);
				count++;
			}
			else{
				System.out.println("Incorrect Prediction for "+r.toString()+" --> "+root.getLeafAttribute());
			}
		
		}
		else{
			int testAttr = root.getTestAttribute();
			int inputAttr = Integer.parseInt(r.get(testAttr));
			if(root.getChildren() != null){
				if(inputAttr == Integer.parseInt(root.getTestValue()[0])){
					traverseTree(r, root.getChildren()[0]);
				}
				else{
					traverseTree(r, root.getChildren()[1]);
				}
			}
		}		
		return;
	}
	
	//Function to Print the Tree
	public static void printTree(TreeNode root){
		
		if(!root.isLeaf()){
			System.out.println(getLableName(root.getTestAttribute())+" = "+root.getTestValue()[0]);
			System.out.println(getLableName(root.getTestAttribute())+" = "+root.getTestValue()[1]);
			printTree(root.getChildren()[0]);
			printTree(root.getChildren()[1]);
		}
		else{
			System.out.println("Leaf for :"+getLableName(root.getParent().getTestAttribute())+" : "+root.getRecords().size()+" classes for "+root.getLeafAttribute());
		}

		return;
	}
	
	//Assign Leaf attributes to the leaf nodes which are not assigned any due to Depth limit reached or Attribute exhaustion
	public static void assignLeaves(TreeNode root){
		String key;		
		if(root.getChildren() != null && root.getTestAttribute() != -1){
			assignLeaves(root.getChildren()[0]);
			assignLeaves(root.getChildren()[1]);
		}
		else{
			HashMap<String, Integer> counts = new HashMap<String, Integer>();
			ArrayList<ArrayList<String>> records = root.getRecords();
			TreeNode test = root.getParent();
			while(records.size() == 0){
				records = test.getRecords();
				test = test.getParent();
			}
			if(!records.isEmpty()){
				for(int j = 0; j<records.size(); j++){
					key = records.get(j).get(indexOfClassLabel);
					if(!counts.containsKey(key)){
						counts.put(key, 1);
					}
					else{
						counts.put(key, counts.get(key)+1);
					}
				}
				int maxValue = Collections.max(counts.values());
				int maxKey = -1;
				for(Entry<String, Integer> itr : counts.entrySet()){
					if(itr.getValue() == maxValue)
						maxKey = Integer.parseInt(itr.getKey()); 
				}
				root.setLeafAttribute(Integer.toString(maxKey));
				root.setLeaf();
			}
			else{
				root.setLeaf();
			}
		}
		return;	
	}
	
	//Get Lable Name from provided index
	public static String getLableName(int index){
		return DataLoader.labels.get(index);
	}
	
	//Check if the Attribute is used in the tree previously
	public static boolean isAttributeUsed(int attribute) {
		if(usedAttributes.contains(attribute)) {
			return true;
		}
		else {
			return false;
		}
	}

	//Set the maximum number of children for a root
	public static int setSize(String set, TreeNode root) {
		String currVal;
		for(int m = 0; m< root.getRecords().size(); m++){
			currVal = root.getRecords().get(m).get(ID3.indexOfClassLabel);
			if(!possClasses.contains(currVal))
				possClasses.add(currVal);		
		}
		return possClasses.size();
	}
}
