sorts
#node={a,b,c,d,e}.
#weight=1..5.

predicates
in_tree(#node,#node,#weight).

rules
:~ in_tree(X,Y,C). [1:C]
