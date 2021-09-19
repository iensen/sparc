%t contains a function symbol of wrong arity
% example provided by Yulia Kahl
sorts
    #block = {b1,b2}.
    #fluent = occupied(#block(B)).
predicates
    holds(#fluent).
rules
    holds(occupied(B1,B2)).
