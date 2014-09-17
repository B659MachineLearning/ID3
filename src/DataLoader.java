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
           ArrayList<String> r = null;
           
           line = reader.readLine();
           StringTokenizer st = new StringTokenizer(line, ",");
           numberOfFeatures = st.countTokens();
           for(int i = 0; i<numberOfFeatures; i++){
        	   labels.add(st.nextToken());
           }
           //labelsname.add(labels);
           while ((line = reader.readLine()) != null) {
               st = new StringTokenizer(line, ",");
               r = new ArrayList<String>();
               for(int i = 0; i<numberOfFeatures; i++){
            	   r.add(st.nextToken());
               }
               records.add(r);
           }
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        System.out.println("Lables : "+labels.toString());
		return records;  
    }
           
}
