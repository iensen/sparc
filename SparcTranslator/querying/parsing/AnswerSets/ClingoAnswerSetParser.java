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
              Collections.addAll(answerSet.atoms,answerSetLine.split("\\s+"));
              result.add(answerSet);
          }
        }
        return result;
    }
}
