%% ignite.sp -- SPARC version of Igniting the Burner from Section 9.2.2
%% Last Modified: 2/7/14
%% Invoke with: java -jar sparc.jar -solver clingo ignite.sp
%% A burner is connected to a gas tank through a pipeline. The gas tank is on
%% the left-most end of the pipeline and the burner is on the right-most end.
%% The pipeline is made up of sections connected with each other by valves. The
%% pipe sections can be either pressurized by the tank or unpressurized. Opening
%% a valve causes the section on its right side to be pressurized if the section
%% to its left is pressurized. Moreover, for safety reasons, a valve can be 
%% opened only if the next valve in the line is closed. Closing a valve causes 
%% the pipe section on its right side to be unpressurized.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
sorts

#section = [s][1..3].
#valve = [v][1..2].
#ov = opened(#valve).
#ps = pressurized(#section).
#fluent = {burner_on} + #ov + #ps.
#ft = {inertial, defined}.
#action = {ignite} + open(#valve) + close(#valve).
#step = 0..4.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
predicates

fluent(#ft, #fluent).
holds(#fluent, #step).
occurs(#action, #step).
connected_to_tank(#section).
connected_to_burner(#section).
connected(#section, #valve, #section).
goal(#step).
success().
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
rules

connected_to_tank(s1).
connected(s1,v1,s2).
connected(s2,v2,s3).
connected_to_burner(s3).

fluent(inertial, burner_on).
fluent(inertial, opened(V)).
fluent(defined, pressurized(S)).

display
fluent.
connected.
connected_to_tank.
connected_to_burner.
#section.
#valve.
#ov.
#ps.
#fluent.
#ft.
#action .
#step.









