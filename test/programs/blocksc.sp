#maxint=1000.
sort definitions
block=$b[0-2].
location=block+$t.
action=   
            put(block(X),location(Y)):{X=Y}+
            put(block(X),location(Y)):{X>Y}-
            put(block(X),location(Y)):{X=Y}.
predicate declarations
occurs(action).
program rules
occurs(put(X,Y)).
