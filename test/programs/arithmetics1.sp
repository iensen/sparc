sorts
#s=1..100.
predicates
p(#s).
q(#s).
rules
p((((X+1)*2-1)*10)+2*X):-q(X).
q(1).
