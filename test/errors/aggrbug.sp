#maxint=20.
sorts
#s={f(5),6}.
predicates
p(#s).
rules
p(Y+1):-#count{X:not p(X+1)}.
