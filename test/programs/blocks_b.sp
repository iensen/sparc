#maxint=1000.
sorts
#block=[b][0..7].
#location=#block+{t}.
#fluent=on(#block(X),#location(Y)):X!=Y.
#action=put(#block(X),#location(Y)):X!=Y.
#step=0..2.
predicates
holds(#fluent,#step).
occurs(#action,#step).



rules

-holds(on(B,L),I+1) :- occurs(put(B,L),I).

