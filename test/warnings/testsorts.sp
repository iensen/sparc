sorts
#s = {a,b,c}.
#r = {1,2,3}.
predicates
s(#s).
q(#r).
rules

s(X) :- #s(X), r(X).

