sorts
#s={f(a),b}.
predicates
p(#s).
rules
p(Z):- #count{X:p(Y)}.
