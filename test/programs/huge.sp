sorts
#s = 1..100.

predicates

p(#s).

rules

p(X) | -p(X).

display
p.
-p.



