sorts

#s = {a,b,c}.
#all =    #s + {d}.
predicates

f(#all).
s(#all).

rules

:- f(X).

display
#all.
#s.
f.

