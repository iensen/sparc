package querying.parsing.AnswerSets;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;



public class ClingoAnswerSetParser extends AnswerSetParser{

    @Override
    public ArrayList<AnswerSet> getAnswerSets(String output) {
        Scanner sc=new Scanner(new StringReader(output));
        ArrayList<AnswerSet> result=new ArrayList<AnswerSet>();
        while(sc.hasNext()) {
          String nextLine=sc.nextLine();
          if(nextLine.startsWith("Answer")) {
              AnswerSet answerSet=new AnswerSet();
              String answerSetLine=sc.nextLine();
              if(answerSetLine.length()>0 && !answerSetLine.matches("/^\\s*$/")) {
            	   String[] list= answerSetLine.split("\\s+");
                   Collections.addAll(answerSet.literals,list);
              }
           
              result.add(answerSet);
          }
        }
        return result;
    }
}
