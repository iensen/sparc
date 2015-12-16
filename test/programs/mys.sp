sorts
#person={andy,vinny,cole,ben}.
#statement1=hated(#person,#person).
#statement2=friends(#person,#person).
#statement3=murderer(#person).
#statement4=outoftown(#person).
#statement5=innocent(#person).
#statement6=know(#person,#person).
#statement7=together(#person,#person).
#statement8=out_of_town(#person).
#statement=#statement1+#statement2+#statement3+#statement4+
          #statement5+#statement6+#statement7+#statement8.
   
#truth={0,1}.


predicates

says(#person,#statement,#truth).
holds(#statement).
murderer(#person).

rules

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
            not holds(murderer(P)).
-holds(S) :- says(P,S,0),
            not holds(murderer(P)).
%r2:One of the suspects is a murderer.
holds(murderer(andy)) | holds(murderer(ben)) | holds(murderer(cole)).

%r3:relation together is symmetric and transitive:
holds(together(A,B)) :- holds(together(B,A)).
holds(together(A,B)) :- holds(together(A,C)),holds(together(C,B)).

%r4:Relation friends is symmetric:
holds(friends(A,B)) :- holds(friends(B,A)).

%r5: Murderers are not innocent.
:- holds(innocent(P)), holds(murderer(P)).

%r6: A person cannot be seen together with people who are out of town.
:- holds(out_of_town(A)), holds(together(A,B)).

%r7: Friends know each other.
:- -holds(know(A,B)),holds(friends(A,B)).

%r8:A person who was out of town cannot be the murderer.
:- holds(murderer(P)),holds(out_of_town(P)).

%r9: We introduce murderer relation
murderer(P) :- holds(murderer(P)).

display
murderer.


