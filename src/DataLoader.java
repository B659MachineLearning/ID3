import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/*
 * Function to read a CSV file and store the data set
 * into a ArrayList of a ArrayList of Strings.
 * First element is a List of names of Features.
 */
public class DataLoader {
	//public static final String FILE_PATH = "zoo-train.csv";
    //public static ArrayList<ArrayList<String>> labelsname = new ArrayList<ArrayList<String>>();
    public static ArrayList<String> labels = new ArrayList<String>();
    public static ArrayList<Integer> catFeatures = new ArrayList<Integer>();
    public static ArrayList<ArrayList<String>> readRecords(String FILE_PATH){
		BufferedReader reader = null;
		ArrayList<ArrayList<String>> records = new ArrayList<ArrayList<String>>();

        try { 
           File f = new File(FILE_PATH);
           FileInputStream fis = new FileInputStream(f); 
           reader = new BufferedReader(new InputStreamReader(fis));
           
           // read the first record of the file
           String line;
           int numberOfFeatures;
           String currLable;
           String currFeature;
           ArrayList<String> r = null;
           
           line = reader.readLine();
           StringTokenizer st = new StringTokenizer(line, ",");
           numberOfFeatures = st.countTokens();
           for(int i = 0; i<numberOfFeatures; i++){
        	   currLable = st.nextToken();
        	   if(labels.size()<numberOfFeatures)
        		   labels.add(currLable);
           }
           //labelsname.add(labels);
           while ((line = reader.readLine()) != null) {
               st = new StringTokenizer(line, ",");
               r = new ArrayList<String>();
               for(int i = 0; i<numberOfFeatures; i++){
            	   currFeature = st.nextToken();
            	   if(Integer.parseInt(currFeature)>1 && !catFeatures.contains(i))
            		   catFeatures.add(i);
            	   r.add(currFeature);
               }
               records.add(r);
           }
           System.out.println("Categorical Feature names : "+catFeatures.toString());
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        //debug
        //System.out.println("Lables : "+labels.toString());
		return records;  
    }
           
}
