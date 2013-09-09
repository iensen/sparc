#maxint=20.

sorts
#s = [0..5].
predicates
p(#s).
q().
rules
p(1).
p(2).
p(X+1) :- p(X+7).


