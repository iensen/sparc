sorts
#s = {a,b,c}.

predicates
p(#s).
q(#s,#s).
r(#s).
t(#s).

rules
{p(X):q(X,Y)} :- r(Y), t(X).
