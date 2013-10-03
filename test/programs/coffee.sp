#maxint=100.

%%%%%%%%%%%%%%%%%%
sorts
%%%%%%%%%%%%%%%%%%

#office=[off][0..2]. %% there are 3 offices
#kitchen=[kit][0..0]. %% there is only 1 kitchen
#room=#office+#kitchen. %% there are totally 4 rooms

#cup=[c][0..0].

#f0 = robot_in(#room).
#f1 = cup_in(#cup, #room).
#f2 = robot_has_cup(#cup).
#f3 = ready_to_serve_with(#cup).
#f4 = coffee_served_in(#office).
#fluent = #f0 + #f1 + #f2 + #f3 + #f4. 

#a0 = find(#cup).
#a1 = load(#cup).
#a2 = unload(#cup).
#a3 = fill_with_coffee(#cup).
#a4 = move_to(#room).
#action = #a0 + #a1 + #a2 + #a3 + #a4.

%%%%%%%%%%%%%%%%%%
predicates
%%%%%%%%%%%%%%%%%%

holds(#fluent,#step).
occurs(#action,#step).
goal().

%%%%%%%%%%%%%%%%%%
rules
%%%%%%%%%%%%%%%%%%

% initial
holds(robot_in(off0), 0).
holds(cup_in(c0, off0), 0).
-holds(robot_has_cup(C), 0) :- #cup(C).
-holds(ready_to_serve_with(C), 0) :- #cup(C).
-holds(coffee_served_in(O), 0) :- #office(O). 
-holds(F,0) :- not holds(F,0).

-holds(robot_in(R1), I) :- holds(robot_in(R2), I), R1 != R2.
-holds(cup_in(C, R1), I) :- holds(cup_in(C, R2), I), R1 != R2.

% inertia axioms
holds(F,I+1) :- holds(F,I), not -holds(F,I+1).
-holds(F,I+1) :- -holds(F,I), not holds(F,I+1). 

holds(robot_in(R), I+1) :- occurs(move_to(R), I).
holds(robot_in(R), I+1) :- occurs(find(C), I), holds(cup_in(C, R), I).

holds(robot_has_cup(C), I+1) :- occurs(load(C), I), holds(robot_in(R), I), holds(cup_in(C, R), I).
-holds(cup_in(C, R), I+1) :- occurs(load(C), I), holds(robot_in(R), I), holds(cup_in(C, R), I).

holds(cup_in(C, R), I+1) :- occurs(unload(C), I), holds(robot_in(R), I), holds(robot_has_cup(C), I). 
-holds(robot_has_cup(C), I+1) :- occurs(unload(C), I), holds(robot_in(R), I), holds(robot_has_cup(C), I). 

holds(ready_to_serve_with(C), I+1) :- occurs(fill_with_coffee(C), I), holds(robot_in(R), I), holds(robot_has_cup(C), I), #kitchen(R).

holds(coffee_served_in(O), I+1) :- occurs(unload(C), I), holds(ready_to_serve_with(C), I), holds(robot_in(O), I). 
holds(cup_in(O), I+1) :- occurs(unload(C), I), holds(ready_to_serve_with(C), I), holds(robot_in(O), I). 
-holds(ready_to_serve_with(C), I+1) :- occurs(unload(C), I), holds(ready_to_serve_with(C), I). 

goal :- holds(coffee_served_in(off1), 10).

occurs(A,T):+.
