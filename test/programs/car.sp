
sorts
#person = {mike}.
#car = {c}.
#default = d(#person).

predicates
owns(#person, #car).
broken(#car).
starts(#car).
mobile(#person).
ab(#default).

rules

-broken(X) :- not ab(d(X)),
              not broken(X).
broken(X):+.

starts(X) :- turn_key(X),
             -broken(X).
mobile(P) :- owns(P, X),
             -broken(X).

owns(mike,c).
turn_key(c).
%-starts(c).


