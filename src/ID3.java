import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;


public class ID3 {
	 public static int NUM_ATTRS = 18;
	 public static ArrayList<Integer> usedAttributes = new ArrayList<Integer>();
	 public static int count = 0;
	 public static int depth = 0;
	 public static int indexOfClassLabel;
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
		String classLabel = Config.readConfig("classLable"); 
		for (int index= 0 ; index < DataLoader.labels.size(); index++){
			if (DataLoader.labels.get(index).equalsIgnoreCase(classLabel)){
				indexOfClassLabel = index;
				break;
			}
		}
		
		TreeNode root = new TreeNode();
		
		for(ArrayList<String> record : records) {
			root.getRecords().add(record);
		}
		
		t.constructTree(records, root, depth);
		
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
		System.out.println("======================================");
		double accuracy = ((double)count/(double)test_records.size())*100;
		System.out.println("Accuracy for Depth "+depth+" : "+accuracy+"%");
		return;
	}
	
	public static void traverseTree(ArrayList<String> r, TreeNode root) {
		
		int testAttr = root.getTestAttribute() >= 0 ? root.getTestAttribute() : 0;
		int inputAttr = Integer.parseInt(r.get(testAttr));
		//System.out.println("testAttr : "+testAttr+" inputAttr : "+inputAttr);
		if(root.getChildren() != null){
			//System.out.println(" visited : "+DataLoader.labels.get(root.getTestAttribute()));
			traverseTree(r, root.getChildren()[inputAttr]);
		}
		
		if(root.children == null){
			System.out.println("Record under test : "+r.toString());
			ArrayList<String> rec = root.getRecords().get(0);
			int leafBranch = 3;
			if(DataLoader.catFeatures.contains(root.getParent().getTestAttribute()) && !root.getTestValue().equalsIgnoreCase("Rest"))
				leafBranch = 0;
			else if(DataLoader.catFeatures.contains(root.getParent().getTestAttribute()) && root.getTestValue().equalsIgnoreCase("Rest"))
				leafBranch = 1;
			else
				leafBranch = Integer.parseInt(rec.get(root.getParent().getTestAttribute()));
			
			//debug
			//System.out.println("Prediction : "+root.getLeafAttribute()[leafBranch]);
			
			if(Integer.parseInt(r.get(indexOfClassLabel))==root.getLeafAttribute()[leafBranch])
				count++;
			else
				System.out.println("Incorrect Prediction for "+r.toString()+" Predicted value : "+root.getLeafAttribute()[leafBranch]);
		}
		
		return;
		
	}
	
	public static void printTree(TreeNode root){
		Boolean isCategorical = false;
		if(root.getChildren() == null)
			isCategorical = DataLoader.catFeatures.contains(root.getParent().getTestAttribute());
		
		if(root.getTestValue() == null || root.getTestValue().equalsIgnoreCase("0") && !root.getTestValue().equalsIgnoreCase("Rest")){
			if(root.getTestAttribute() >=0)
				System.out.println("0 -- "+DataLoader.labels.get(root.getTestAttribute()));
			if(root.getLeafAttribute()[0] != -1)
				if(isCategorical)
					System.out.println(DataLoader.labels.get(root.getParent().getTestAttribute())+" = "+root.getParent().getTestValue()+" Leaf 0 -- Class "+root.getLeafAttribute()[0]+" -- "+root.getRecords().size()+" Examples");
				else
					System.out.println(DataLoader.labels.get(root.getParent().getTestAttribute())+" Leaf 0 -- Class "+root.getLeafAttribute()[0]+" -- "+root.getRecords().size()+" Examples");
			if(root.getLeafAttribute()[1] != -1)
				if(isCategorical)
					System.out.println(DataLoader.labels.get(root.getParent().getTestAttribute())+" != "+" Leaf 1 -- Class "+root.getLeafAttribute()[1]+" -- "+root.getRecords().size()+" Examples");
				else
					System.out.println(DataLoader.labels.get(root.getParent().getTestAttribute())+" Leaf 1 -- Class "+root.getLeafAttribute()[1]+" -- "+root.getRecords().size()+" Examples");
		}
		else{
			if(root.getTestAttribute() >=0)
				System.out.println("1 -- "+DataLoader.labels.get(root.getTestAttribute()));
			if(root.getLeafAttribute()[0] != -1)
				if(isCategorical)
					System.out.println(DataLoader.labels.get(root.getParent().getTestAttribute())+" = "+root.getParent().getTestValue()+" Leaf 0 -- Class "+root.getLeafAttribute()[0]+" -- "+root.getRecords().size()+" Examples");
				else
				 System.out.println(DataLoader.labels.get(root.getParent().getTestAttribute())+" Leaf 0 -- Class "+root.getLeafAttribute()[0]+" -- "+root.getRecords().size()+" Examples");
			if(root.getLeafAttribute()[1] != -1)
				if(isCategorical)
					System.out.println(DataLoader.labels.get(root.getParent().getTestAttribute())+" != "+root.getParent().getTestValue()+" Leaf 1 -- Class "+root.getLeafAttribute()[1]+" -- "+root.getRecords().size()+" Examples");
				else
					System.out.println(DataLoader.labels.get(root.getParent().getTestAttribute())+" Leaf 1 -- Class "+root.getLeafAttribute()[1]+" -- "+root.getRecords().size()+" Examples");
		}
		
		if(root.children != null){
			printTree(root.children[0]);
			printTree(root.children[1]);
		}
	}
	
	
	public static void assignLeaves(TreeNode root){
		
		String key;		
		
		if(root.children != null){
			assignLeaves(root.children[0]);
			assignLeaves(root.children[1]);
		}
		else{
			//System.out.println("Branch : "+root.getTestValue());
			HashMap<String, Integer> counts = new HashMap<String, Integer>();
			ArrayList<ArrayList<String>> records = root.getRecords();
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
			root.setLeafAttribute(maxKey, Integer.parseInt(root.getTestValue()));
			//System.out.println("Possible predictions : "+counts.toString());
			
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
