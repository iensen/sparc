sorts
#s=1..2.
#s2=3..4.
predicates
p(#s).
q(#s2).
rules
%node(1).
 :- p(X), #count{V : p(V+1),q(V+1)} >0.
