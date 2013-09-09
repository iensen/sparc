sorts
#s=1..5.
predicates
p(#s).
q(#s).
rules
p(X+10):-q(X).
p(X+20):-p(X).
