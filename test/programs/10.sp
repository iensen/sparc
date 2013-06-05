sort definitions
s=1..2.
predicate declarations
node(s).
a(s).
program rules
%node(1).
a(X) :- node(X), #count{V : not node(X),node(V)} >0.
