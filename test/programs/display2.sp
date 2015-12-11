sorts
#s = {a,b,c,f(a),f(b)}.

predicates
p(#s).
q().
s(#s).

rules

s(a) :- #s(a).

display
-q.
#s.



