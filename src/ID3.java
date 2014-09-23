import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;


public class ID3 {
	 public static int NUM_ATTRS = 18;
	 public static int classVals = 0;
	 public static ArrayList<String> possClasses = new ArrayList<String>();
	 public static ArrayList<Integer> usedAttributes = new ArrayList<Integer>();
	 public static int count = 0;
	 public static int depth = 0;
	 public static int indexOfClassLabel;
	 public static String classLabel = Config.readConfig("classLable"); 
	 
	 public static Boolean printTree = true;
	  
	 public static void main(String args[]) {

		ArrayList<ArrayList<String>> records = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> test_records = new ArrayList<ArrayList<String>>();
		BuildTree t = new BuildTree();
		 
		depth = Integer.parseInt(Config.readConfig("depth"));
		
		//Read the data in CSV file with attributes and examples
		String trainFilePath = Config.readConfig("trainFileName"); 
		records = DataLoader.readRecords(trainFilePath);
		 
		//System.out.println("Data Set : "+records.toString());
		System.out.println("Number of examples : "+(records.size()-1));
		System.out.println("Lables : "+DataLoader.labels.toString());
		
		//Index of classLabel
		indexOfClassLabel = DataLoader.labels.indexOf(classLabel);
		
		TreeNode root = new TreeNode();
		
		for(ArrayList<String> record : records) {
			root.getRecords().add(record);
		}
		
		classVals = setSize(classLabel, root);
		t.constructTree(records, root, depth);
		
		assignLeaves(root);
		
		//printTree(root);
		
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
		System.out.println("======================================");
		double accuracy = ((double)count/(double)test_records.size())*100;
		System.out.println("Accuracy for Depth "+depth+" : "+accuracy+"%");
		
		return;
	}
	
	public static void traverseTree(ArrayList<String> r, TreeNode root) {
		if(root.isLeaf()){
			//System.out.println("Record under test : "+r.toString());
			//debug
			//System.out.println("Prediction : "+root.getLeafAttribute()[0]);
			if(Integer.parseInt(r.get(indexOfClassLabel)) == Integer.parseInt(root.getLeafAttribute()[0]))
				count++;
			//else
				//System.out.println("Incorrect Prediction for "+r.toString()+" Predicted value : "+root.getLeafAttribute()[0]);
		}
		else{
			int testAttr = root.getTestAttribute();
			int inputAttr = Integer.parseInt(r.get(testAttr));
			//System.out.println("testAttr : "+testAttr+" inputAttr : "+inputAttr);
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
	
	
	public static void printTree(TreeNode root){
		
		if(!root.isLeaf()){
			System.out.println(getLableName(root.getTestAttribute())+" = "+root.getTestValue()[0]);
			System.out.println(getLableName(root.getTestAttribute())+" = "+root.getTestValue()[1]);
			printTree(root.getChildren()[0]);
			printTree(root.getChildren()[1]);
		}
		else{
			System.out.println("Leaf for :"+getLableName(root.getParent().getTestAttribute())+" : "+root.getRecords().size()+" classes for "+root.getLeafAttribute()[0]);
		}

		return;
	}
	
	
	public static void assignLeaves(TreeNode root){
		
		String key;		
		
		if(root.getChildren() != null && root.getTestAttribute() != -1){
			assignLeaves(root.getChildren()[0]);
			assignLeaves(root.getChildren()[1]);
		}
		else{
			//System.out.println("Branch : "+root.getTestValue());
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
				root.setLeafAttribute(Integer.toString(maxKey), 0);
				root.setLeaf();
			}
			else{
				root.setLeaf();
			}
			
		}
		return;	
	}
	
	
	
	public static String getLableName(int index){
		return DataLoader.labels.get(index);
	}
	
	public static boolean isAttributeUsed(int attribute) {
		if(usedAttributes.contains(attribute)) {
			return true;
		}
		else {
			return false;
		}
	}
 
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
