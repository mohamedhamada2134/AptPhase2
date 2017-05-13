package back;
import java.awt.*;
import java.awt.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by user on 5/2/2017.
 */
public class Ranker {

    private Map<String, Float> wordsMap = new LinkedHashMap<String, Float>();
    private Map<String, Float> phraseMap = new LinkedHashMap<String, Float>();

    private float max1 = 0;
    private float max2 = 0;

    //private Vector<String> urls ;
    private Vector<String> finalurls = new Vector();
    boolean check = true;

    public Ranker() {
    }

    public void computeWordsMap(Map<Integer, Integer> a) {
        if (!a.isEmpty()) {
            Map<String, Float> urlsmixed = new LinkedHashMap<String, Float>();
            try {
                max1 = 0;
                max2 = 0;
                check = true;
                String sql2 = "Select max(rank) from URLS";
                PreparedStatement ps2 = SearchQueryProcessor.connection.prepareStatement(sql2);
                ResultSet ranks = ps2.executeQuery();
                if (ranks.next()) {
                    max1 = ranks.getInt("max(rank)");
                }

                String url;
                int rankii;

                Map.Entry<Integer, Integer> maxEntry = null;
                for (Map.Entry<Integer, Integer> entry : a.entrySet()) {
                    if (maxEntry == null) {
                        maxEntry = entry;
                        //System.out.println(entry.getValue());

                    }

                    if (entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                        maxEntry = entry;
                    }
                    //System.out.println(entry.getKey());
                }
                max2 = maxEntry.getValue();

                for (Map.Entry<Integer, Integer> entry : a.entrySet()) {

                    String sqlm = "Select * from URLS where urlid=?";
                    PreparedStatement psm = SearchQueryProcessor.connection.prepareStatement(sqlm);
                    psm.setInt(1, entry.getKey());
                    ResultSet tableurls = psm.executeQuery();
                    while (tableurls.next()) {
                        url = tableurls.getString("url");
                        rankii = tableurls.getInt("rank");
                        urlsmixed.putIfAbsent(url, score(rankii, entry.getValue()));
                        //System.out.println( urlsmixed.get(url));
                    }
                }

                /* ArrayList sortedValues = new ArrayList(urlsmixed.values());

                Collections.sort(sortedValues, Collections.reverseOrder());

                for (int i = 0; i < sortedValues.size(); i++) {
                    for (Map.Entry<String, Float> entry : urlsmixed.entrySet()) {
                        //System.out.println(sortedValues.get(i));
                        if (entry.getValue() == sortedValues.get(i)) {
                            System.out.println(entry.getKey());
                            System.out.println(entry.getValue());

                            finalurls.addElement(entry.getKey());
                        }

                    }

                }
                wordsMap = urlsmixed;
                for (int i = 0; i < finalurls.size(); i++) {
                    System.out.println("//////////////////////");
                    System.out.println(finalurls.get(i));
                }
                //System.out.println(finalurls.size());*/
                wordsMap = urlsmixed;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void computePhraseMap(Map<Integer, Integer> b) {
        if (!b.isEmpty()) {
            Map<String, Float> urlsmixed = new LinkedHashMap<String, Float>();
            try {
                max1 = 0;
                max2 = 0;
                check = true;
                String sql22 = "Select max(rank) from URLS";
                PreparedStatement ps22 = SearchQueryProcessor.connection.prepareStatement(sql22);
                ResultSet ranks = ps22.executeQuery();
                if (ranks.next()) {
                    max1 = ranks.getInt("max(rank)");
                }

                String url;
                int rankii;

                Map.Entry<Integer, Integer> maxEntry = null;
                for (Map.Entry<Integer, Integer> entry : b.entrySet()) {
                    if (maxEntry == null) {
                        maxEntry = entry;
                        //System.out.println(entry.getValue());

                    }

                    if (entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                        maxEntry = entry;
                    }
                    //System.out.println(entry.getKey());
                }
                if (maxEntry != null) {
                    max2 = maxEntry.getValue();
                } else {
                    //System.out.println("phrase not found");
                    check = false;
                }

                if (check) {
                    for (Map.Entry<Integer, Integer> entry : b.entrySet()) {

                        String sqlm = "Select * from URLS where urlid=?";
                        PreparedStatement psm = SearchQueryProcessor.connection.prepareStatement(sqlm);
                        psm.setInt(1, entry.getKey());
                        ResultSet tableurls = psm.executeQuery();
                        while (tableurls.next()) {
                            url = tableurls.getString("url");
                            rankii = tableurls.getInt("rank");
                            urlsmixed.putIfAbsent(url, score(rankii, entry.getValue()));
                            //System.out.println( urlsmixed.get(url));
                        }
                    }

                    /* ArrayList sortedValues = new ArrayList(urlsmixed.values());

                    Collections.sort(sortedValues, Collections.reverseOrder());

                    for (int i = 0; i < sortedValues.size(); i++) {
                        for (Map.Entry<String, Float> entry : urlsmixed.entrySet()) {
                            //System.out.println(sortedValues.get(i));
                            if (entry.getValue() == sortedValues.get(i)) {
                                System.out.println(entry.getKey());
                                System.out.println(entry.getValue());

                                finalurls.addElement(entry.getKey());
                            }

                        }

                    }

                   /* for (int i = 0; i < finalurls.size(); i++) {
                        System.out.println("//////////////////////");
                        System.out.println(finalurls.get(i));
                    }*/
                    phraseMap = urlsmixed;
                    //System.out.println(finalurls.size());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private float score(int pop, int rel) {
        //System.out.println(pop);
        //System.out.println(rel);

        return ((float) (pop / max1) * (float) (rel / max2)) / ((float) (pop / max1) + (float) (rel / max2));
    }

    private float score2(float words, float phrase) {

        return words + phrase;
    }

    public boolean ComputeFinalMap() {
        if (!wordsMap.isEmpty() && !phraseMap.isEmpty()) {
            for (Map.Entry<String, Float> entry : phraseMap.entrySet()) {
                
                    if (!wordsMap.containsKey(entry.getKey())) {
                        wordsMap.put(entry.getKey(), entry.getValue());
                    } else {
                        wordsMap.put(entry.getKey(), score2(wordsMap.get(entry.getKey()), entry.getValue()));

                    }
                
            }
           // System.out.println("1");
        } else if (wordsMap.isEmpty() && !phraseMap.isEmpty()) {
            wordsMap = phraseMap;
            //System.out.println("2");
        } else if (wordsMap.isEmpty() && phraseMap.isEmpty() ) {
            //System.out.println("3");
            return false;

        }
        //System.out.println("4");
        ArrayList sortedValues = new ArrayList(wordsMap.values());

        Collections.sort(sortedValues, Collections.reverseOrder());
        //System.out.println(sortedValues.size());
        for (int i = 0; i < sortedValues.size(); i++) {
            //System.out.println("ffff");
            for (Map.Entry<String, Float> entry : wordsMap.entrySet()) {
                //System.out.println(sortedValues.get(i));
                if (entry.getValue() == sortedValues.get(i)) {
                    System.out.println(entry.getKey());
                    System.out.println(entry.getValue());

                    finalurls.addElement(entry.getKey());
                }

            }

        }
        for (int i = 0; i < finalurls.size(); i++) {
            System.out.println("//////////////////////");
            System.out.println(finalurls.get(i));
        }
        return true;
    }

    /*public Map<String, Float> getWordsMap() {
        return wordsMap;
    }

    public Map<String, Float> getPhraseMap() {
        return phraseMap;
    }*/
    public Vector<String> getFinalUrls() {
        return finalurls;
    }
}
