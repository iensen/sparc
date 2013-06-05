sort definitions
s1=1..10.
s2=5..15.
predicate declarations
p(s1).
q(s2).
r(s1).
f().
program rules
q(6).
p(X):-q(X).
f :-  #count{V : r(V),q(V)} >0.
