p(a):-not  -p(a).
appl(r_0)|-appl(r_0).
:~appl(r_0).
-p(a):-appl(r_0).
p(b):-not  -p(b).
appl(r_1)|-appl(r_1).
:~appl(r_1).
-p(b):-appl(r_1).
p(c):-not  -p(c).
appl(r_2)|-appl(r_2).
:~appl(r_2).
-p(c):-appl(r_2).
r(a):-p(a).
-r(a):-p(b).
-r(a):-p(c).
