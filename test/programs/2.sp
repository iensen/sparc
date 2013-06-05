sort definitions
s1=$a|b|c.
predicate declarations
p(s1).
r(s1).
program rules
p(eee) :- not -p(a).
-p(a) :+.
p(b):- not -p(b).
-p(b) :+.
p(c) :- not -p(c).
-p(c):+.
r(a):-p(a).
-r(a):-p(b).
-r(a):-p(c).
