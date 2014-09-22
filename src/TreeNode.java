import java.util.ArrayList;


public class TreeNode {
	private TreeNode parent;
	public TreeNode[] children;
	public ArrayList<ArrayList<String>> records;
	private double entropy;
	private int testAttribute;
	private String testValue;
	private int leafAttribute[];
	
	public TreeNode(){
		this.parent = null;
		this.children = null;
		this.records = new ArrayList<ArrayList<String>>();
		this.entropy = 0.0;
		this.testAttribute = -1;
		this.leafAttribute = new int[]{-1, -1};
	}
	
	public void setParent(TreeNode parent){
		this.parent = parent;
	}
	
	public TreeNode getParent(){
		return this.parent;
	}
	
	public void setChildren(TreeNode[] children){
		this.children = children;
	}
	
	public TreeNode[] getChildren(){
		return this.children;
	}
	
	public void setRecords(ArrayList<ArrayList<String>> records){
		this.records = records;
	}
	
	public ArrayList<ArrayList<String>> getRecords(){
		return this.records;
	}
	
	public void setEntropy(double entropy){
		this.entropy = entropy;
	}
	
	public double getEntropy(){
		return this.entropy;
	}
	
	public void setTestAttribute(int attr){
		this.testAttribute = attr;
	}
	
	public int getTestAttribute(){
		return this.testAttribute;
	}
	
	public void setLeafAttribute(int attr, int branch){
		System.out.println("=====================================================================================================");
		System.out.println("Setting Leaf for node with parent :"+this.parent.getTestAttribute()+" for branch "+branch+" as "+attr);
		System.out.println("=====================================================================================================");
		this.leafAttribute[branch] = attr;
	}
	
	public int[] getLeafAttribute(){
		return this.leafAttribute;
	}
	
	public void setTestValue(String value){
		this.testValue = value;
	}
	
	public String getTestValue(){
		return this.testValue;
	}
	
}
