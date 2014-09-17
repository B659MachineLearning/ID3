import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;


public class ID3 {
	 public static int NUM_ATTRS = 18;
	 public static ArrayList<Integer> usedAttributes = new ArrayList<Integer>();
	 public static int count = 0;
	 public static int depth = 0;
	  
public static void main(String args[]) {
	 
	 ArrayList<ArrayList<String>> records = new ArrayList<ArrayList<String>>();
	 ArrayList<ArrayList<String>> test_records = new ArrayList<ArrayList<String>>();
	 BuildTree t = new BuildTree();
	 
	 depth = Integer.parseInt(Config.readConfig("depth"));
	
	 //Read the data in CSV file with attributes and examples
	 String trainFilePath = Config.readConfig("trainFileName"); 
	 records = DataLoader.readRecords(trainFilePath);
	 
	 System.out.println("Data Set : "+records.toString());
	 System.out.println("Number of examples : "+(records.size()-1));
	 
	 TreeNode root = new TreeNode();
	
	for(ArrayList<String> record : records) {
		root.getRecords().add(record);
	}
	
	t.constructTree(records, root, depth);
	
	assignLeaves(root);
	
	String testFilePath = Config.readConfig("testFileName");
	test_records = DataLoader.readRecords(testFilePath);
	//System.out.println("Test Record : "+records.get(60));
	for(int i =0 ; i<test_records.size(); i++){
		traverseTree(test_records.get(i), root);
	}
	System.out.println(count+" Correct predictions out of "+test_records.size());
	return;
	
		
	}
	
	public static void traverseTree(ArrayList<String> r, TreeNode root) {
		
		int testAttr = root.getTestAttribute();
		int inputAttr = Integer.parseInt(r.get(testAttr));
		//System.out.println("testAttr : "+testAttr+" inputAttr : "+inputAttr);
		if(root.getChildren() != null)
			traverseTree(r, root.getChildren()[inputAttr]);
		
		if(root.children == null){
			ArrayList<String> rec = root.getRecords().get(0);
			int leafBranch = Integer.parseInt(rec.get(root.getParent().getTestAttribute()));
			System.out.println("Prediction : "+root.getLeafAttribute()[leafBranch]);
			if(Integer.parseInt(r.get(16))==root.getLeafAttribute()[leafBranch])
				count++;
			else
				System.out.println("Incorrect Prediction for "+r.toString());
		}
		
		return;
		
	}
	
	public static void assignLeaves(TreeNode root){
		
		String key;
		int value;
		
		if(root.children != null){
			assignLeaves(root.children[0]);
			assignLeaves(root.children[1]);
		}
		else{
			System.out.println("Branch : "+root.getTestValue());
			HashMap<String, Integer> counts = new HashMap<String, Integer>();
			ArrayList<ArrayList<String>> records = root.getRecords();
			for(int j = 0; j<records.size(); j++){
				key = records.get(j).get(16);
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
			root.setLeafAttribute(maxKey, root.getTestValue());
			System.out.println("Possible predictions : "+counts.toString());
			
		}
		return;	
	}
	 
	 public static boolean isAttributeUsed(int attribute) {
			if(usedAttributes.contains(attribute)) {
				return true;
			}
			else {
				return false;
			}
	}
	 
	 public static int setSize(String set) {
			if(set.equalsIgnoreCase("type")) {
				return 8;
			}
			return 0;
	}
}
