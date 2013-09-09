
sorts
#person = {mike}.
#car = {c}.
#default = d(#car).

predicates
owns(#person, #car).
broken(#car).
starts(#car).
mobile(#person).
ab(#default).
<<<<<<< HEAD
turn_key(#car).
=======
>>>>>>> 3fbc26c10d5738c7e547f41fba18d47a02a9cfa2

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



