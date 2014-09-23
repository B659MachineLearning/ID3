/*
 * Authors : Aniket Bhosale and Mayur Tare
 * 
 * Description :
 * A Java Object to encapsulate Tree node details.
 */

import java.util.ArrayList;


public class TreeNode {
	private TreeNode parent;
	public TreeNode[] children;
	public ArrayList<ArrayList<String>> records;
	private double entropy;
	private int testAttribute;
	private String testValue[];
	private Boolean isLeaf;
	private String leafAttribute;
	
	public TreeNode(){
		this.parent = null;
		this.children = null;
		this.records = new ArrayList<ArrayList<String>>();
		this.entropy = 0.0;
		this.testAttribute = -1;
		this.leafAttribute = "-1";
		this.testValue = new String[]{"NA","NA"};
		this.isLeaf = false;
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
	
	public void setLeafAttribute(String attr){
		this.leafAttribute = attr;
	}
	
	public String getLeafAttribute(){
		return this.leafAttribute;
	}
	
	public void setTestValue(String value, int branch){
		this.testValue[branch] = value;
	}
	
	public String[] getTestValue(){
		return this.testValue;
	}
	
	public void setLeaf(){
		this.isLeaf = true;
	}
	
	public Boolean isLeaf(){
		return this.isLeaf;
	}
	
}
