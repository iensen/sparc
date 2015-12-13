%% ancestors.sp -- SPARC version of ancestors from Section 4.1.3
%% Last Modified: 2/7/14 
%% Defining Ancestors

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
sorts

#person = {bill, mary, bob, kathy, mike, rich, patty, sam, susan}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
predicates

father(#person,#person).
mother(#person,#person).
parent(#person,#person).
ancestor(#person,#person).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
rules

father(bob,bill).  
father(rich,bob).  
father(mike,mary). 
father(sam,rich).  
mother(mary,bill).
mother(patty,bob).
mother(kathy,mary).
mother(susan,rich).

-father(F,C) :- not father(F,C).

-mother(M,C) :- not mother(M,C).
                
parent(X,Y) :- father(X,Y).
parent(X,Y) :- mother(X,Y).
-parent(X,Y) :- not parent(X,Y).

ancestor(X,Y) :- parent(X,Y).
ancestor(X,Y) :- parent(Z,Y),
                 ancestor(X,Z).
-ancestor(X,Y) :- not ancestor(X,Y).