sorts
#vertex=1..6.

predicates

in(#vertex,#vertex).
edge(#vertex,#vertex).
reached(#vertex).
init(#vertex).

rules
% each vertex is visited exactly once:
% 1.1 each vertex has at most one incoming edge
 -in(V2,V) :-
           in(V1,V),
           V1 != V2.
% 1.2 each vertex has at most one outcoming edge
-in(V,V2) :- 
          in(V,V1),
           V1 != V2.
%2.For the second condition, we recursively define relation reached(V) which
%holds if P visits vertex V on its way from the initial vertex:


reached(V2) :- init(V1),
               in(V1,V2).
reached(V2) :- reached(V1),
               in(V1,V2).
-reached(V) :-  not reached(V).
%guarantees that every vertex is reached
:-  -reached(V).
%select axiom:
in(X,Y) | -in(X,Y):-edge(X,Y).
% problem instance:
init(1).
edge(1,2).
edge(2,3).
edge(3,4).
edge(4,5).
edge(5,6).
edge(6,1).

display
in.
