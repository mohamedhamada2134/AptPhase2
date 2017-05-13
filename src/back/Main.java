package back;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mostafa4
 */
public class Main {

    static int Threads_Number;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int input;
        Scanner in = new Scanner(System.in);
        System.out.println("if you want to search for word press 1 else press 0 ");
        input=in.nextInt();
        if ( input== 0) {
            System.out.println("please enter the number of the Threads :");
            Threads_Number = in.nextInt();
            Indexer.stopWords();
            for (int i = 0; i < Threads_Number; i++) {
                new Crawler(Threads_Number, i).start();
            }

        } /*else if(input==1){
            System.out.println("enter your search word");
            Scanner in1 = new Scanner(System.in);
            String word=in1.nextLine();
           SearchQueryProcessor sq1= new SearchQueryProcessor(word);
           PhaseSearcher ps1= new PhaseSearcher(word);
           Ranker r= new Ranker();
           r.computeWordsMap(sq1.getUrlsPro());
           r.computePhraseMap(ps1.getUrlsPrase());
           if(!r.ComputeFinalMap())
               System.out.println("NO RESULT FOUND");
           
            
            
        }
*/
    }
}
