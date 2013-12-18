#const n=5.
#const m=1.
sorts
#s2 = {n,m,f(n)}.
#s=1..n.
predicates
p(#s).
q(#s).
t(#s2).
rules
q(2).
p(X):-q(X+m).
t(X).
