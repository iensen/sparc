has_grounding("p(f(X)):-q(X). ( line: 11, column: 1)"):-s2(f(X_G)),s3(X_G).
warning("p(f(X)):-q(X). ( line: 11, column: 1)"):- not has_grounding("p(f(X)):-q(X). ( line: 11, column: 1)").
p(f(X_G)):-q(X_G),s2(f(X_G)),s3(X_G).
s2(f(a)).
s3(b).
