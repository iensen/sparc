%t is a ground term not belonging to corresponding sort
#const n = 5. #maxint=n.
sorts
#s={a,b,f(a)}. 
predicates
p(#s).
rules
p(f(b)).
