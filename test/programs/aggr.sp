sorts
#s={a,b}.
predicates
p(#s).
q(#s).
rules
q(a).
p(a):-#count{Y:q(Y)}>0.
