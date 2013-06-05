sort definitions
person=$andy|vinny|cole|ben.


statement=
         hated(person,person)+
         friends(person,person)+
         murderer(person)+
         outoftown(person)+
         innocent(person)+
         know(person,person)+
         together(person,person)+
         out_of_town(person).
truth=$0|1 .


predicate declarations

says(person,statement,truth).
holds(statement).
murderer(person).

program rules



% Andy says:
says(andy, murderer(andy), 0). % He didn't do it.
says(andy, hated(cole,vinny), 1). % Cole hated Vinny.
says(andy, friends(ben,vinny), 1). %Ben and Vinny were friends.

%Ben says:
says(ben, out_of_town(ben), 1).%He was out of town.
says(ben, know(ben,vinny), 0). %He didn't know Vinny.

%Cole says:
says(cole, innocent(cole), 1).  
says(cole, together(andy,vinny), 1). % He saw Andy and Ben
says(cole, together(ben,vinny), 1). % with the victim.


%r1:Everyone, except possibly for the murderer, is telling the truth:
holds(S) :- says(P,S,1),
            -holds(murderer(P)).
-holds(S) :- says(P,S,0),
            -holds(murderer(P)).
%r2:One of the suspects is a murderer.
holds(murderer(andy)) | holds(murderer(ben)) | holds(murderer(cole)).

%r3:normally people are not murderers:
-holds(murderer(P)) :- not holds(murderer(P)).

%r4:relation together is symmetric and transitive:
holds(together(A,B)) :- holds(together(B,A)).
holds(together(A,B)) :- holds(together(A,C)),holds(together(C,B)).

%r5:Relation friends is symmetric:
holds(friends(A,B)) :- holds(friends(B,A)).

%r6: Murderers are not innocent.
:- holds(innocent(P)), holds(murderer(P)).

%r7: A person cannot be seen together with people who are out of town.
:- holds(out_of_town(A)), holds(together(A,B)).

%r8: Friends know each other.
:- -holds(know(A,B)),holds(friends(A,B)).

%r9:A person who was out of town cannot be the murderer.
:- holds(murderer(P)),holds(out_of_town(P)).

%r10: We introduce murderer relation
murderer(P) :- holds(murderer(P)).












