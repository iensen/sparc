sort definitions
s=1..2.
s2=3..4.
predicate declarations
p(s).
q(s2).
program rules
%node(1).
 :- p(X), #count{V : p(V+1),q(V+1)} >0.
