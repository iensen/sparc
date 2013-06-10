sorts
#s1={a,b,c}.
predicates
p(#s1).
r(#s1).
rules
p(a) :- not -p(a).
-p(a) :+.
p(b):- not -p(b).
-p(b) :+.
p(c) :- not -p(c).
-p(c):+.
r(a):-p(a).
-r(a):-p(b).
-r(a):-p(c).
