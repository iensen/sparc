sorts
#s=1..5.
predicates
p(#s).
rules
p(1..5).
p(X):-p(X+5).
