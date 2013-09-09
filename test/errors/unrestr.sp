sorts
#s={f(a),b}.
predicates
p(#s).
rules
p(f(X)):-Y<2,Z<2,F>3,#count{Q:Q<W,p(W),T<2},p(Y).
