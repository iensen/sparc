sorts
#s1={a}. % term "a" has sort "s1"
predicates
p(#s1). %predicate "p" accepts terms of sort s1
q(#s1). %predicate "q" accepts terms of sort s1
rules
p(a) :- not q(a).
-p(a).
q(a):+. % this is a CR-RULE.

display
p.
-p.
-q.
q.
#s1.



