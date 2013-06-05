sort definitions
s1=$a.  % term "a" has sort "s1"
predicate declarations
p(s1).  %predicate  "p" accepts terms of the sort s1 
q(s1).  %predicate  "q" accepts terms of the sort s1 
program rules
p(a) :- not q(a).
-p(a).
q(a):+.  % this is a CR-RULE.
