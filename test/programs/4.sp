sort definitions
s1=$a|b|c.

predicate declarations

p(s1).
q(s1).

program rules

label1: q(a):+.
label2: q(b):+p(a).
label3: :+p(c).
