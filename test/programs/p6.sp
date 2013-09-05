
#const n = 3.

sorts

#s1 = [0..n].
#s2 = {a,b}.

predicates

p(#s1,#s2).

q(#s2).

rules

q(a).
p(X,Y) :- q(X).

