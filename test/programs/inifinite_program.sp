sorts
#s=1..10.
predicates
p(#s).
rules
p(X+1):-p(X),not limit(X).
limit(3).
