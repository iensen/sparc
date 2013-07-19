
sorts
#num=1..9.
#region=1..9.
#coords=c(#num,#num).

predicates
pos(#num,#coords).
in_region(#coords,#num).
same_region(#coords,#coords).

rules

:-               pos(N,P1),
                 pos(N,P2),
                 P1!=P2,
                 same_region(P1,P2).


