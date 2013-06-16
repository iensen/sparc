sorts

#ss2=1..4.
#ss1=f(#ss2).
#s1=#ss1+#ss2.
#s2=1..4.
#s=f(#s1(X),#s2(Y)): not(X>Y or X<Y).
predicates
p(#s).
rules
p(f(2,3)).
