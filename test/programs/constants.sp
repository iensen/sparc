#const n=5.
#const m=1.
sort definitions
s=1..n.
predicate declarations
p(s).
q(s).
program rules
q(2).
p(X):-q(X+m).
