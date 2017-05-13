package back;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Transfer {
	public static String query;
	public static  Vector<String> finalurls = new Vector();
	public static Map <Integer, Integer> msq1 = new HashMap<Integer, Integer>();
	public static Map <Integer, Integer> msp1 = new HashMap<Integer, Integer>();
	public static Vector<String> statment = new Vector();
	
	public Transfer(){
		
	}
	 public static Vector<String> getResult(){
	    	return finalurls;
	    }
	 public static int getRsize(){
	    	return finalurls.size();
	    }
	 public static void start(Connection connection,String query){
        SearchQueryProcessor sq1= new SearchQueryProcessor(query,connection);
        PhaseSearcher ps1= new PhaseSearcher(query);
        Ranker r= new Ranker();
        r.computeWordsMap(sq1.getUrlsPro());
        r.computePhraseMap(ps1.getUrlsPrase());
        if(!r.ComputeFinalMap())
            System.out.println("NO RESULT FOUND");
        finalurls=r.getFinalUrls();
	 }
	 
	 public static void searchFiles(String q ,Connection connection){
		 statment.clear();
		 Pattern p = Pattern.compile(".*\\\"(.*)\\\".*");
	        Matcher m = p.matcher(q);
	       if(m.find()){
	        q = m.group(1);
	        //System.out.println("l2ythhhhhhhhaaaaaaaaaaaa");
	       }
	       q=q.trim();
		 String Value = q.toLowerCase();
		 
		 for (int i = 0; i < finalurls.size(); i++){
				try
			    {	int urlid=0;
					/*String query22 = "SELECT * FROM URLS WHERE url= ? ;";
		            PreparedStatement preparedStmt = connection.prepareStatement(query22);
		            preparedStmt.setString(1,finalurls.get(i));
		            ResultSet rprocess = preparedStmt.executeQuery();
		            */
			    String query22 = "SELECT * FROM URLS WHERE url='"+finalurls.get(i)+ "' ;";
			    Statement stmt = connection.createStatement();
			    ResultSet rprocess = stmt.executeQuery(query22);
			    while (rprocess.next()) {
		               
		                 urlid = rprocess.getInt("urlid");
		                
		                }
		            ///////////////////////////
		            
		            FileReader fileReader = new FileReader("/F:/college/cmp3/second term/APT/project/interface/"+Integer.toString(urlid)+".txt");
		            BufferedReader bufferedReader = new BufferedReader(fileReader);
		            StringBuffer stringBuffer = new StringBuffer();
		            String line;
		           
		            while ((line = bufferedReader.readLine()) != null) {
		            	String lineTemp=line.toLowerCase();
		                if(lineTemp.contains(Value))
		                {  System.out.println("hi");
		                	//int k=line.indexOf(Value);
		                    statment.addElement(line);
		                    break;
		                }else {statment.addElement(" ");}
		                
		            }
		            fileReader.close();
		            /////////////////////////
			       /* Scanner file = new Scanner(new File("/F:/college/cmp3/second term/APT/interface/"+Integer.toString(urlid)+".txt"));
			            while (file.hasNextLine())
			            {
			                final String lineFromFile = file.nextLine();
			                if(lineFromFile.contains(Value))
			                {  System.out.println("hi");
			                    statment.addElement(lineFromFile);
			                    break;
			                } 
			                }
			            file.close();*/
			    }catch (IOException e) {
					e.printStackTrace();

				} catch (SQLException ex) {
	                Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
	            }
		 }
	 }
	 public static Vector<String> getStatment(){
	    	return statment;
	    }
}
