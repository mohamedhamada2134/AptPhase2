package back;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tartarus.snowball.ext.PorterStemmer;

/**
 * Created by user on 5/2/2017.
 */
public class SearchQueryProcessor {

    /*private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/MY_DB";*/
    PorterStemmer stemmer = new PorterStemmer();
    static Connection connection;
    Statement stat;
    private Map<Integer, Integer> urlspro = new LinkedHashMap<Integer, Integer>();
    

    public SearchQueryProcessor(String querypro,Connection connection) {
        
        int value;
        //OpenConnection();
        this.connection=connection;
        querypro=querypro.replaceAll("\\\"(.*)\\\"","");
        querypro=querypro.trim();
        String[] words = querypro.split(" +");
        int cnt=0;
        for (int i = 0; i < words.length; i++) {
            //System.out.println(words[i]);
            words[i]=words[i].toLowerCase();
            String s1 = words[i];
            //System.out.println(words[i]);
            stemmer.setCurrent(s1);
            stemmer.stem();
            s1 = stemmer.getCurrent();
            words[i] = s1;
            System.out.println(words[i]);
        }
        for(int i=0;i<words.length;i++){
        try {
            String query = "SELECT * FROM WORDS WHERE word= ? ;";
            PreparedStatement preparedStmt = this.connection.prepareStatement(query);
            preparedStmt.setString(1,words[i]);
            ResultSet rprocess = preparedStmt.executeQuery();
            
            while (rprocess.next()) {
                cnt++;
                int urlid = rprocess.getInt("url_id");
                //System.out.println(urlid);
                if (!urlspro.containsKey(urlid)) {
                    urlspro.put(urlid, 0);
                }

                if (rprocess.getString("importance") == "h1" || rprocess.getString("importance") == "h2"
                        || rprocess.getString("importance") == "h3" || rprocess.getString("importance") == "h4"
                        || rprocess.getString("importance") == "h5" || rprocess.getString("importance") == "h6") {
                    value = urlspro.get(urlid);
                    value = value + 5;
                    urlspro.put(urlid, value);
                } else if (rprocess.getString("importance") == "title") {
                    value = urlspro.get(urlid);
                    value = value + 7;
                    urlspro.put(urlid, value);
                } else if (rprocess.getString("importance") == "p") {
                    value = urlspro.get(urlid);
                    value = value + 3;
                    urlspro.put(urlid, value);
                } else {
                    value = urlspro.get(urlid);
                    value = value + 1;
                    urlspro.put(urlid, value);
                }

            }
            } catch (SQLException e) {
            e.printStackTrace();
        }
        }
            if (cnt==0)
            {
                System.out.println("Word does not exist");
             }
            /*else {//preparedStmt.close();
            
            //new Ranker(urlspro,0);
            }

        
        //preparedStmt.setInt(1, id);*/

        

    }
    public Map<Integer, Integer> getUrlsPro(){
        return urlspro;
    }
  /*  public void OpenConnection() {
        try {
            connection = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            System.out.println("Database connected!");
            stat = connection.createStatement();

        } catch (SQLException ex2) {
            throw new IllegalStateException("Can't Open Connection", ex2);
        }
    }*/

}
