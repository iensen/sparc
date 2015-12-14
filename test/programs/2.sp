sorts
#s1={a,b}.  % term "a" has sort "s1"
predicates
p(#s1).  %predicate  "p" accepts terms of the sort s1 
q(#s1).  %predicate  "q" accepts terms of the sort s1 
rules
p(b) :- not q(a).
-p(a).
q(a):+.  % this is a CR-RULE.

display
p.
-p.
q.
-q.
#s1.
