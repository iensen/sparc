#const numSteps = 1.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 sorts
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

#place = {office, main_library, aux_library}.

#robot = {rob0}.
#textbook = {text0}.
#object = #textbook.
#thing = #object + #robot.

#boolean = {true, false}.

#default = d1(#textbook) + d2(#textbook) + d3(#textbook).

#step = 0..numSteps.

%% Fluents
#inertial_fluent = loc(#thing, #place).
#fluent = #inertial_fluent.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 predicates
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

val(#fluent, #boolean, #step).

ab(#default).
preferred(#default, #default).

obs(#fluent, #boolean, #step).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 rules
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% State Constraints

%% If an object is in one location, it cannot be in another
val(loc(O, L1), false, I) :- val(loc(O, L2), true, I), L1!=L2.


%% General Inertia Axiom
val(F, Y, I+1) :- #inertial_fluent(F),
             	  val(F, Y, I),
                  not -val(F, Y, I+1).



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Defaults
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% D1
val(loc(T, main_library), true, 0) :- #textbook(T),
				      not ab(d1(T)).

%% D2								 
val(loc(T, aux_library), true, 0) :- #textbook(T),
				     not ab(d2(T)).

%% D3								 
val(loc(T, office), true, 0) :- #textbook(T),
				not ab(d3(T)).

ab(d2(T)) :- preferred(d1(T), d2(T)), #textbook(T),
	     not ab(d1(T)).
 
ab(d3(T)) :- preferred(d1(T), d3(T)), #textbook(T),
	     not ab(d1(T)).

ab(d3(T)) :- preferred(d2(T), d3(T)), #textbook(T),
	     not ab(d2(T)).

preferred(d1(T), d3(T)) :- preferred(d1(T), d2(T)),
		  	preferred(d2(T), d3(T)).

-preferred(d1(T), d1(T)).
-preferred(d2(T), d2(T)).
-preferred(d3(T), d3(T)).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% CR rules
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
ab(d1(T)) :+ #textbook(T).
ab(d2(T)) :+ #textbook(T).
ab(d3(T)) :+ #textbook(T).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Reality check + obs/val rules
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% Reality check axioms
:- obs(F, true, I), val(F, false, I).
:- obs(F, false, I), val(F, true, I).

%% Observations set values of fluents in the initial state...
val(F, Y, 0) :- obs(F, Y, 0).

%% Only one value possible for a fluent; not really necessary...
-val(F, V2, I) :- val(F, V1, I), V1!=V2.

%-obs(F, V2, I) :- obs(F, V1, I), V1!=V2.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Tests
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

obs(loc(text0, main_library), false, 1).
preferred(d1(text0), d2(text0)).
preferred(d2(text0), d3(text0)).
%preferred(d1(text0), d3(text0)).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
display
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
val.
ab.
%obs.
