sorts
#s=1..2.
predicates
node(#s).
a(#s).
rules
node(1).
a(X) :- node(X), #count{V : not node(V)} >0.
