
sorts

#s1={1,2}.

#s2 = {f(1,1)}.

predicates

p(#s2).

rules

p(f(2,X)).
-p(X) :- not p(X).


