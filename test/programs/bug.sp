
#maxint=1000.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
sorts

#step = 0..6.
#place = {office, main_library, aux_library, kitchen}. 
#robot = [rob][0..0]. 
#object = [obj][0..0]. 
#thing = #robot + #object.

#fl_loc = loc(#thing, #place).
#fl_in_hand = in_hand(#robot, #object).
#fl_same_loc = same_loc(#robot, #object). 
#inertial_fluent = #fl_in_hand + #fl_loc + #fl_same_loc. 

%% #fluent = #inertial_fluent + #defined_fluent. 
#fluent = #inertial_fluent. 

#ac_move = move(#robot, #place). 
#ac_find = find(#robot, #object). 
#ac_grasp = grasp(#robot, #object). 
#ac_putdown = putdown(#robot, #object). 
#action = #ac_move + #ac_find + #ac_grasp + #ac_putdown. 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
predicates

holds(#fluent, #step). 
occurs(#action, #step). 
goal(#step). 
hr(#step). 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
rules

holds(loc(R, P), I+1) :- occurs(move(R, P), I). 

holds(loc(O, P), I+1) :- occurs(move(R, P), I),
                         holds(in_hand(R, O), I). 

holds(in_hand(R, O), I+1) :- occurs(grasp(R, O), I). 

-holds(in_hand(R, O), I+1) :- occurs(putdown(R, O), I). 

holds(same_loc(R, O), I+1) :- occurs(find(R, O), I). 


holds(loc(R, P), I) :- holds(loc(O, P), I), holds(same_loc(R, O), I). 
% -holds(loc(R, P), I) :- -holds(loc(O, P), I), holds(same_loc(R, O), I). 
% holds(loc(O, P), I) :- holds(loc(R, P), I), holds(same_loc(R, O), I). 
% -holds(loc(O, P), I) :- -holds(loc(R, P), I), holds(same_loc(R, O), I). 

holds(loc(O, P), I) :- holds(loc(R, P), I), 
                       holds(in_hand(R, O), I). 

-holds(loc(Th, P1), I) :- holds(loc(Th, P2), I), P1 != P2, #thing(Th). 

:- occurs(move(R, P), I), holds(loc(R, P), I), #robot(R). 

:- occurs(grasp(R, O), I), holds(loc(R, P1), I), holds(loc(O, P2), I),
   P1 != P2, #robot(R), #object(O). 
 
:- occurs(grasp(R, O), I), holds(loc(R, P), I), not holds(loc(O, P), I). 
% :- occurs(grasp(R, O), I), not holds(loc(R, P), I), holds(loc(O, P), I). 
% :- occurs(grasp(R, O), I), not holds(loc(R, P), I), not holds(loc(O, P), I). 

:- occurs(A1, I), occurs(A2, I), A1 != A2. 

%% CWA for Defined Fluents

%% -holds(F, I) :- #defined_fluent(F), not holds(F, I). 

%% General Inertia Axiom

holds(F, I+1) :- #inertial_fluent(F), 
                 holds(F, I), 
                 not -holds(F, I+1). 

-holds(F, I+1) :- #inertial_fluent(F), 
                  -holds(F, I), 
                  not holds(F, I+1). 

%% CWA for Actions

-occurs(A, I) :- #action(A), not occurs(A, I). 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Initial state

holds(loc(rob0, office), 0). 
% holds(loc(obj0, kitchen), 0). 
-holds(in_hand(rob0, obj0), 0). 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Goal state

:- not goal(1). 
goal(I) :- holds(loc(obj0, office), I), 
           holds(loc(rob0, office), I). 

% :- not goal(4). 
% goal(I) :- holds(loc(obj0, office), I), 
%           -holds(in_hand(rob0, obj0), I). 

occurs(A, I) :+ . 

holds(loc(O, P), I+1) :+ occurs(find(R, O), I). 
:- not hr(I), occurs(find(R, O), I). 
hr(I) :- holds(loc(O, P), I). 

display
holds.
#action.

