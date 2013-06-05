#const n=4.

sort definitions

block=$b[1-3].
location=block+$t.
step=0..n.
inertial_fluent=  on(block,location)
       + larger(block,block)
       + same(block,block).
defined_fluent=$bin+$bout.
fluent=inertial_fluent+defined_fluent.
action=put(block,location).
size=$l|m|s.


predicate declarations
holdsout(block,block,step).
holdsout2(block,block,step).

holds(fluent,step).
blk_size(block,size).
success().
something_happened(step).
occurs(action,step).
c().
program rules
%%INITIAL CONFIGURATION
%%======================
holds(on(b1,t),0).
holds(on(b2,t),0).
holds(on(b3,t),0).
-holds(on(B,L), 0) :- not holds(on(B,L), 0).

%%RULES
%%======
holds(on(B,L), I+1) :- occurs(put(B,L),I).
-holds(on(B,L2), I) :- holds(on(B,L1), I), L1 != L2.
-holds(on(B2,B), I) :- holds(on(B1,B), I), B1 != B2,block(B). 
holds(F, I+1) :- holds(F, I), not -holds(F, I+1),inertial_fluent(F).
-holds(F, I+1) :- -holds(F, I), not holds(F, I+1),inertial_fluent(F).
-occurs(put(B,L), I) :- holds(on(B1,B),I).
-occurs(put(B1,B), I) :- holds(on(B2,B),I),block(B).
 occurs(put(b2,b1),1).
 occurs(put(b1,b3),0). 

%%TO DETERMINE THE LARGER AMONG BLOCKS
%%======================================

blk_size(b1,l).
blk_size(b2,m).
blk_size(b3,l).

holds(larger(B1,B2),Z):- blk_size(B1,l),blk_size(B2,m).
holds(larger(B1,B2),Z):- blk_size(B1,m),blk_size(B2,s).
-holds(larger(B2,B1),Z):-not holds(larger(B2,B1),Z).
holds(larger(B1,B3),Z):-holds(larger(B1,B2),Z),holds(larger(B2,B3),Z).


%%SAME SIZED BLOCKS
%%=================

holds(same(B1,B2),Z):-blk_size(B1,Y1),blk_size(B2,Y2),B1!=B2,Y1=Y2.
-holds(same(B1,B2),Z) :-  not holds(same(B1,B2),Z).
holds(same(B1,B2),Z):- blk_size(B1,Y1),blk_size(B2,Y1),B1=B2.

%%BLOCKS ORDER PLANNING
%%=====================
           
holds(bin,I):-   not holds(bout,I).
holds(bout,I):-  holds(on(B1,B2),I),holds(on(B2,L),I),holds(larger(B1,B2),I).
%holds(bout,I):-  holds(on(B1,t),I), holds(on(B2,t),I),B1!=B2.
holdsout(b1,b1,I):-holds(bout,I).
holdsout2(B1,B2,I):-holds(on(B1,t),I), holds(on(B2,t),I),B1!=B2.

occurs(put(b2,b1),1).
occurs(put(b1,b3),0). 

success:- holds(bin,I).

%:-not success.

occurs(A,I)|-occurs(A,I):-not holds(bin,I).

:-occurs(A1,I),occurs(A2,I),A1!=A2.

something_happened(I) :-occurs(A,I).
:- holds(bin,I),I=K+1,not holds(bin,K),J < I,not something_happened(J).





