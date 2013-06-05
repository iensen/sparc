#maxint=1000.
sort definitions
block=$b[0-3].
location=block.
action=put2(block(X),block(Y)):{X=Y}.
predicate declarations
occurs(action).
program rules
occurs(put2(X,Y)).
