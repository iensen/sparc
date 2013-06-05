sort definitions
s1=1..10.
s2=5..15.
predicate declarations
p(s1).
q(s2).
program rules
q(6).
p(X):-q(X).
