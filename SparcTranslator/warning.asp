has_grounding("p(X+1):-p(X). ( line: 6, column: 1)"):-VAR_0=X_G+1,s(VAR_0),s(X_G).
warning("p(X+1):-p(X). ( line: 6, column: 1)"):- not has_grounding("p(X+1):-p(X). ( line: 6, column: 1)").
p(VAR_0):-p(X_G),VAR_0=X_G+1,s(VAR_0),s(X_G).
s(1).
