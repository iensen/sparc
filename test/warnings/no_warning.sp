sorts
#s1={a}.
#s2=f(#s1).
#s3={a}.

predicates
p(#s2).
q(#s3).

rules
p(f(X)):-q(X).
