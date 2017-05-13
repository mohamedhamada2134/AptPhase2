
package back;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.tartarus.snowball.ext.PorterStemmer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mostafa
 */
public class PhaseSearcher {

    PorterStemmer stemmer = new PorterStemmer();
    private Vector<IndexRowItem> items = new Vector<IndexRowItem>(5);
    private int value = 0;
    private Map<Integer, Integer> urlsphrase = new LinkedHashMap<Integer, Integer>();
    private String[] StopWords = new String[667];
    public PhaseSearcher(String querypro) {
        
        stopWords();
        String[] stopWrds = StopWords;
        
        Pattern p = Pattern.compile(".*\\\"(.*)\\\".*");
        Matcher m = p.matcher(querypro);
       if(m.find()){
        querypro = m.group(1);
        //System.out.println("l2ythhhhhhhhaaaaaaaaaaaa");
       }
       else 
           return;
       
        querypro=querypro.trim();
        String[] arrofwordstemp = querypro.split(" +");
        int cntnull=0;
        List<String> temp = new LinkedList<String>(Arrays.asList(arrofwordstemp));
        temp.replaceAll(String::toLowerCase);
        temp.removeAll(Arrays.asList(stopWrds));
        
        arrofwordstemp=temp.toArray(arrofwordstemp); 
        
        //temp.removeAll(Arrays.asList(stopWrds));
        //temp.removeAll(Arrays.asList("", null));
       //temp.toArray(arrofwords);
        for(int i=0;i<arrofwordstemp.length;i++)
        {
            if(arrofwordstemp[i]==null)
              cntnull++;
            //System.out.println(arrofwordstemp[i]);
        }
        /////////////////////////////////
        String[] arrofwords= new String[arrofwordstemp.length-cntnull];
        for(int i=0;i<arrofwordstemp.length;i++)
        {
            if(arrofwordstemp[i]!=null)
            {  arrofwords[i]=arrofwordstemp[i];
            System.out.println(arrofwords[i]);}
        }
        for (int l = 0; l < arrofwords.length; l++) {
           
            String s1 = arrofwords[l];
            stemmer.setCurrent(s1);
            stemmer.stem();
            s1 = stemmer.getCurrent();
            arrofwords[l] = s1;
           
        }

        try {
            String query = "SELECT * FROM WORDS WHERE Word=? ;";
            PreparedStatement preparedStmt = SearchQueryProcessor.connection.prepareStatement(query);
            preparedStmt.setString(1, arrofwords[0]);
            ResultSet rprocess = preparedStmt.executeQuery();

            while (rprocess.next()) {

                int urlid = rprocess.getInt("url_id");
                String importance = rprocess.getString("importance");
                int position = rprocess.getInt("position");

                IndexRowItem t1 = new IndexRowItem();

                t1.setWord(arrofwords[0]);
                t1.setImportance(importance);
                t1.setPosition(position);
                t1.setUrlID(urlid);

                items.add(t1);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < items.size(); j++) {

            int c = 0;

            for (int i = 1; i < arrofwords.length; i++) {

                try {

                    String query = "SELECT * FROM WORDS WHERE Word=? and importance=? and position=? and url_id=?;";
                    PreparedStatement preparedStmt = SearchQueryProcessor.connection.prepareStatement(query);
                    preparedStmt.setString(1, arrofwords[i]);
                    preparedStmt.setString(2, items.elementAt(j).getImportance());
                    preparedStmt.setInt(3, items.elementAt(j).getPosition() + i);
                    preparedStmt.setInt(4, items.elementAt(j).getUrlID());
                    ResultSet rprocess = preparedStmt.executeQuery();
                    boolean b1 = false;
                    if (rprocess.next()) {
                        b1 = true;
                        c++;

                    }
                    if (!b1) {
                        break;
                    }

                } catch (SQLException e) {

                }

            }
            if (c == arrofwords.length - 1) {
                urlsphrase.putIfAbsent(items.elementAt(j).getUrlID(), 0);
                value = urlsphrase.get(items.elementAt(j).getUrlID());
                value = value + 1;
                urlsphrase.put(items.elementAt(j).getUrlID(), value);
            }

        }
       //new Ranker(urlsphrase,1);
    }
    public void stopWords() {
        try {
            FileReader fileReader = new FileReader("/F:/college/cmp3/second term/APT/project/interface/stopwords.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                StopWords[i] = line;
                i++;
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 
    public Map<Integer, Integer> getUrlsPrase(){
        return urlsphrase;
    }
}
