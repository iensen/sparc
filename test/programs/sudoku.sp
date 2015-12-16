
sorts
#num=1..9.
#region=1..9.
#coords=c(#num,#num).

predicates
pos(#num,#coords).
in_region(#coords,#num).
same_region(#coords,#coords).

rules

%each cell should contain a value
pos(1,c(X,Y)) | pos(2,c(X,Y)) | pos(3,c(X,Y)) | pos(4,c(X,Y)) | pos(5,c(X,Y)) |
pos(6,c(X,Y)) | pos(7,c(X,Y)) | pos(8,c(X,Y)) | pos(9,c(X,Y)) :- #coords(c(X,Y)).

%No row contains the same number twice.
-pos(N,c(X,Y2)) :-pos(N,c(X,Y1)),
                  Y1 != Y2.
%No column contains the same number twice.
-pos(N,c(X1,Y)) :-pos(N,c(X2,Y)),
                  X1 != X2.

%No region contains the same number twice:

:-               pos(N,P1),
                 pos(N,P2),
                 P1!=P2,
                 same_region(P1,P2).

% in region definition:
in_region(c(X,Y),((X-1)/3)*3+((Y+2)/3)).
%translated as in_region(c(X_G,Y_G),VAR_2):-VAR_1=Y_G+2,VAR_0=VAR_1/3,VAR_5=X_G-1,VAR_4=VAR_5/3,VAR_3=VAR_4*3,VAR_2=VAR_3+VAR_0,coords(c(X_G,Y_G)),num(VAR_2).


%same region:
same_region(X,Y):-in_region(X,R),in_region(Y,R).

%PUZZLE INSTANCE:

pos(7,c(2,1)).
pos(1,c(4,1)).
pos(9,c(1,2)).
pos(3,c(6,2)).
pos(7,c(6,3)).
pos(2,c(8,3)).
pos(5,c(9,3)).
pos(6,c(2,4)).
pos(5,c(3,5)).
pos(6,c(5,5)).
pos(2,c(7,5)).
pos(9,c(8,5)).
pos(3,c(1,6)).
pos(4,c(4,6)).
pos(5,c(5,6)).
pos(6,c(9,6)).
pos(1,c(1,7)).
pos(2,c(2,7)).
pos(8,c(4,7)).
pos(8,c(1,8)).
pos(9,c(3,8)).
pos(4,c(7,8)).
pos(7,c(9,8)).
pos(5,c(1,9)).

display
pos.
