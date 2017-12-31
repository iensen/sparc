#maxint=2000.
#const numSteps=1.
val(loc(O_G,L1_G),false,I_G):-val(loc(O_G,L2_G),true,I_G),L1_G!=L2_G,thing(O_G),step(I_G),place(L1_G),place(L2_G).
val(F_G,Y_G,VAR_0):-inertial_fluent(F_G),val(F_G,Y_G,I_G),not  -val(F_G,Y_G,VAR_1),VAR_0=I_G+1,VAR_1=I_G+1,boolean(Y_G),step(VAR_1),fluent(F_G),step(I_G),step(VAR_0).
val(loc(T_G,main_library),true,0):-textbook(T_G),not  ab(d1(T_G)),place(main_library),thing(T_G).
val(loc(T_G,aux_library),true,0):-textbook(T_G),not  ab(d2(T_G)),place(aux_library),thing(T_G).
val(loc(T_G,office),true,0):-textbook(T_G),not  ab(d3(T_G)),thing(T_G),place(office).
ab(d2(T_G)):-preferred(d1(T_G),d2(T_G)),textbook(T_G),not  ab(d1(T_G)).
ab(d3(T_G)):-preferred(d1(T_G),d3(T_G)),textbook(T_G),not  ab(d1(T_G)).
ab(d3(T_G)):-preferred(d2(T_G),d3(T_G)),textbook(T_G),not  ab(d2(T_G)).
preferred(d1(T_G),d3(T_G)):-preferred(d1(T_G),d2(T_G)),preferred(d2(T_G),d3(T_G)),textbook(T_G).
-preferred(d1(T_G),d1(T_G)):-textbook(T_G),textbook(T_G).
-preferred(d2(T_G),d2(T_G)):-textbook(T_G),textbook(T_G).
-preferred(d3(T_G),d3(T_G)):-textbook(T_G),textbook(T_G).
appl(r_0(T_G))|-appl(r_0(T_G)):-textbook(T_G).
:~appl(r_0(T_G)),textbook(T_G).
ab(d1(T_G)):-textbook(T_G),appl(r_0(T_G)).
appl(r_1(T_G))|-appl(r_1(T_G)):-textbook(T_G).
:~appl(r_1(T_G)),textbook(T_G).
ab(d2(T_G)):-textbook(T_G),appl(r_1(T_G)).
appl(r_2(T_G))|-appl(r_2(T_G)):-textbook(T_G).
:~appl(r_2(T_G)),textbook(T_G).
ab(d3(T_G)):-textbook(T_G),appl(r_2(T_G)).
:-obs(F_G,true,I_G),val(F_G,false,I_G),fluent(F_G),step(I_G).
:-obs(F_G,false,I_G),val(F_G,true,I_G),step(I_G),fluent(F_G).
val(F_G,Y_G,0):-obs(F_G,Y_G,0),fluent(F_G),boolean(Y_G).
-val(F_G,V2_G,I_G):-val(F_G,V1_G,I_G),V1_G!=V2_G,boolean(V1_G),boolean(V2_G),step(I_G),fluent(F_G).
obs(loc(text0,main_library),false,1).
preferred(d1(text0),d2(text0)).
preferred(d2(text0),d3(text0)).
textbook(text0).
inertial_fluent(loc(rob0,office)).
inertial_fluent(loc(text0,aux_library)).
inertial_fluent(loc(rob0,aux_library)).
inertial_fluent(loc(rob0,main_library)).
inertial_fluent(loc(text0,main_library)).
inertial_fluent(loc(text0,office)).
thing(text0).
thing(rob0).
step(0).
step(1).
place(aux_library).
place(office).
place(main_library).
boolean(true).
boolean(false).
fluent(loc(rob0,office)).
fluent(loc(text0,aux_library)).
fluent(loc(rob0,aux_library)).
fluent(loc(rob0,main_library)).
fluent(loc(text0,main_library)).
fluent(loc(text0,office)).
val_(X1,X2,X3):-val(X1,X2,X3).
ab_(X1):-ab(X1).
% val_ ab_
