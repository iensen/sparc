package querying.parsing.AnswerSets;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

import warnings.StringListUtils;



public class DLVAnswerSetParser extends AnswerSetParser{
    private Scanner sc;
    private String buf;
    int bufIdx;
    public DLVAnswerSetParser() {

    }

    private Character nextChar() {
        if(buf==null || bufIdx==buf.length()) {
           if(sc.hasNext()) {
               buf=sc.next();
               bufIdx=0;
           }
           else {
               return null;
           }
        }
        return buf.charAt(bufIdx++);
    }

    @Override
    public ArrayList<AnswerSet> getAnswerSets(String output) {
        sc=new Scanner(new StringReader(output));
        ArrayList<AnswerSet> result=new ArrayList<AnswerSet>();
        Character next=null;
        while((next=nextChar())!=null) {
            if(next=='{') {
                StringBuilder sb=new StringBuilder();
                while((next=nextChar())!='}') {
                    sb.append(next);
                }
                AnswerSet answerSet=new AnswerSet();
               
                answerSet.literals.addAll(StringListUtils.splitCommaSequence(sb.toString()));
                result.add(answerSet);
            }
        }

        return result;
    }
}
