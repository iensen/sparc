sorts
#s1={a,b,c}.

predicates

p(#s1).
q(#s1).

rules

label1: q(a):+.
label2: q(b):+p(a).
label3: :+p(c).
