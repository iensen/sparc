%% s_bwplan.sp =
%%   s_bw.sp + simple planning module using disjunctive rule
%%           + example initial situation 
%%           + example goal
%%   where s_bw.sp is the AL system description of the blocks world 
%%         domain translated into SPARC
%% Chapter 9 Section 9.1
%% Last Modified: 2/24/14
%% 
%% For a quick result, invoke with 
%%    java -jar sparc.jar s_bwplan.sp -A -solver clingo | mkatoms
%% However, since SPARC doesn't understand #show yet, you may want to wait for 
%%    java -jar sparc.jar s_crossing.sp -A -solveropts "-pfilter=occurs" | mkatoms
%% As always, piping the output to mkatoms is optional but nice.
%% 
%% The basic blocks world consists of a robotic arm that can manipulate
%% configurations of same-sized cubic blocks on a table. There are
%% limitations to what the robotic arm can do. It can move
%% unoccupied blocks, one at a time, onto other unoccupied blocks or
%% onto the table. (An unoccupied block is one that does not have another
%% block stacked on it.) At any given step, a block can be in at most
%% one location; in other words, a block can be directly on top of one
%% other block, or on the table. We do not impose a limit on how tall
%% our towers can be. Our table is big enough to hold all the blocks,
%% even if they are not stacked. We do not take into account spatial
%% relationships of towers, just which blocks are on top
%% of each other and which blocks are on the table.

#const n = 8.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
sorts
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

#block = [b][0..7].

#location = #block + {t}.

#inertial_fluent = on(#block(X),#location(Y)):X!=Y.

#defined_fluent = above(#block(X),#location(Y)):X!=Y.

#fluent = #inertial_fluent + #defined_fluent.
          
#action = put(#block(X),#location(Y)):X!=Y.

#step = 0..n.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
predicates
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

holds(#fluent,#step).
occurs(#action,#step).
success().
goal(#step).
something_happened(#step).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
rules
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% Laws %%

%% Putting block B on location L at step I
%% causes B to be on L at step I+1:
%% put(B,L) causes on(B,L)
holds(on(B,L),I+1) :- occurs(put(B,L),I).

%% A block cannot be in two locations at once:
%% -on(B,L2) if on(B,L1), L1 != L2
-holds(on(B,L2),I) :- holds(on(B,L1),I), 
                      L1 != L2.

%% Only one block can be set directly on top of another:
%% -on(B2,B) if on(B1,B), B1 != B2
-holds(on(B2,B),I) :- #block(B), 
                      holds(on(B1,B),I),
                      B1 != B2.

%% B is above L if it is directly on top of it or on top of 
%% another block that is above L:
%% above(B,L) if on (B,L)
%% above(B,L) if on(B,B1), above(B1,L)
holds(above(B2,B1),I) :- holds(on(B2,B1),I).

holds(above(B2,B1),I) :- holds(on(B2,B),I),
                         holds(above(B,B1),I).
                  
%% It is impossible to move an occupied block:       
%% impossible put(B,L) if on (B1,B)
-occurs(put(B,L),I) :- holds(on(B1,B),I).

%% It is impossible to move a block onto an occupied block:
%% impossible put(B1,B) if on(B2,B).
-occurs(put(B1,B),I) :- #block(B),
                        holds(on(B2,B),I).
                        
%% CWA for Defined Fluents

-holds(F,I) :- #defined_fluent(F), 
               not holds(F,I).

%% General Inertia Axiom

holds(F,I+1) :- #inertial_fluent(F),
                holds(F,I),
                not -holds(F,I+1).

-holds(F,I+1) :- #inertial_fluent(F),
                 -holds(F,I),
                 not holds(F,I+1).
                 
%% CWA for Actions

-occurs(A,I) :- not occurs(A,I).
                
                
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%       
%% Simple Planning Module using Disjunctive Rule:   
  
success :- goal(I).
:- not success.

occurs(A,I) | -occurs(A,I) :- not goal(I).
                              
%% Do not allow concurrent actions:
:- occurs(A1,I),
   occurs(A2,I),
   A1 != A2.

%% An action occurs at each step before
%% the goal is achieved:

something_happened(I) :- occurs(A,I).

:- goal(I), goal(I-1),
   J < I,
   not something_happened(J).
                                        
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Specific initial situation.
%% Change at will:

%% holds(on(B,L),I): a block B is on location L at step I.
holds(on(b0,t),0).                         
holds(on(b3,b0),0).
holds(on(b2,b3),0).
holds(on(b1,t),0).
holds(on(b4,b1),0).
holds(on(b5,t),0).
holds(on(b6,b5),0).
holds(on(b7,b6),0).   

%% If block B is not known to be on location L at step 0,
%% then we assume it is not.
-holds(on(B,L),0) :- not holds(on(B,L),0). 
                     
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Specific goal. 
%% Change at will:

goal(I) :- 
   holds(on(b4,t),I),  holds(on(b6,t),I),  holds(on(b1,t),I),
   holds(on(b3,b4),I), holds(on(b7,b3),I), holds(on(b2,b6),I),
   holds(on(b0,b1),I), holds(on(b5,b0),I).
                     
                     
%% Alternative Goal:
%goal(I) :- holds(on(b3,t),I).