sorts
 #region = {tx, ok, co}.
 #color = {red, blue, green}.

predicates
 neighbor(#region, #region).
 has(#region, #color). 

rules
 % define neighbor relation
 neighbor(tx,ok).
 neighbor(ok,co).
 neighbor(co,tx).
 neighbor(X,Y) :- neighbor(Y,X).

 % select colors
 1{has(X,C)}1:- #region(X).
 :- neighbor(X,Y), has(X,C), has(Y,C).


display
  has(X,Y).
