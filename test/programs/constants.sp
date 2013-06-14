#const n=5.
#const m=1.
sorts
#s=1..n.
predicates
p(#s).
q(#s).
rules
q(2).
p(X):-q(X+m).
