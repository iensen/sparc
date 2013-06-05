sort definitions
node=$a|b|c|d|e.
weight=1..5.

predicate declarations
in_tree(node,node,weight).

program rules
:~ in_tree(X,Y,C). [1:C]
