sorts
#s=1..2.
predicates
p(#s).
q(#s).
rules
p(X):- 2*X=(X-1)*(X-1)+2.
q(1).
