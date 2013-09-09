#maxint=20.
sorts
#s={f(5),6}.
predicates
p(#s).
q(#s).
rules
p(f(X+1)):-X<2,Y<10,#count{Q:Q<W,q(W),T<2,q(T+3),q(Q*5)}>1,p(Y-1),p(X-1).
