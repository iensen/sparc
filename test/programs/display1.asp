#maxint=2000.
p(a).
-p(b).
p(f(a)).
-p(f(b)).
q:-s_(a).
s_(f(b)).
s_(b).
s_(c).
s_(a).
s_(f(a)).
p_(X1):-p(X1).
% p_
