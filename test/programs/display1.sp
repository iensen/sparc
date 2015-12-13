sorts
#s = {a,b,c,f(a),f(b)}.

predicates
p(#s).
q().
s(#s).




rules
s(a):- #s(b).
s(a) :- #s(b).
-q:- #s(a).
p(a) :- -q.
-p(b).
p(f(a)).
-p(f(b)).


display
-q.
-p(f(X)).
p(X).
#s.



