sorts
#num = 1..5.
#value = {170,10,90,75,60}.

predicates
hotel(#num).
cost(#num,#value).
goal().

rules
hotel(X):+.

cost(1,170).
cost(2,10). 
cost(3,90). 
cost(4,75).
cost(5,60).

goal :- hotel(X).
:- not goal.

#minimize {Y@1, X:hotel(X), cost(X,Y)}.

display
hotel.
