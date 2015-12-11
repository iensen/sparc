sorts
#time=0..4.
#node={roh,roj,r1u,r1r,ff345j,r3d,f1f,rf345j,r1a,lfh,r5d,lfj,ohmso,ff12j,rfh,l2u,ffj,ffh,r5r,
     ro345j,rf12j,r3r,l2d,lf,lo345j,ro12j,f3d,f1l,loj,lo,loh,f3l,f1d,f3f,rf,f3u,l4d,f5l,ro,foh,l3l,
     foj,f1u,r4d,l3d,oxfeed,l3a,lo12j,lf12j,r2d,l2l,r4u,l4u,ff,r4r,f5r,r2u,fo345j,r2r,fo,lf345j,f4r,f2f,f2d,r3a,
     l1u,fo12j,l1l,f2r,l5d,f4d,f2u,rfj,l4l,l5l,l1a,ohmsf,fxfeed}.

#valve={rfm3,rfm2,rfm1,foi12,rfm5,rfm4,roha,fom2,ffdummy,rfx345,rfha,rfhb,foha,ffm4,lfi345b,fom3,lfi345a,lfx345,
      fom5,rox345,rom4,lfi12,rom2,rom3,rom1,roi345b,lox345,loi12,lfdummy,lohb,lom1,lom2,lom3,lom4,fom4,lfx12,loi345a,
      lox12,loi345b,fohb,foi345,lom5,ffm5,ffi345,ffm3,ffm2,ffm1,rodummy,lfhb,lfha,lfm5,lfm4,roi12,rom5,lfm1,lfm3,lfm2,
      ffha,ffhb,ffi12,loha,fom1,rfi345a,rfi345b,fodummy,rfx12,rox12,lodummy,rfdummy,rfi12,roi345a,rohb,ffdummy,fodummy,
      lfdummy,lodummy,rfdummy,rodummy}.

#switch={li345a,li345b,fi345,fm2,lm2,lm3,lm4,lm5,rx345,fi12,fhb,ri345b,ri345a,rx12,lm1,rm2,rm3,rm1,rm4,
        rm5,ri12,lx345,li12,rha,rhb,lhb,lha,fm4,fm5,fha,fm3,fm1,lx12}.

#circuit={rmc2,rmc3,fic12,rmc1,rmc4,rmc5,fmc4,fmc5,lic345a,fmc3,fmc1,lhca,lhcb,ric345a,fic345,rhcb,rhca,lxc345,ric12,
         lmc4,lmc5,lmc1,lmc2,lmc3,ric345b,fmc2,lic345b,lic12,fhcb,fhca,rxc12,lxc12,rxc345,rmc3,fic12,rmc1,rmc4,rmc5,
         fmc4,fmc5,lic345a,fmc3,fmc1,lhca,lhcb,ric345a,fic345,rhcb,rhca,lxc345,ric12,lmc4,lmc5,lmc1,lmc2,lmc3,ric345b,
         fmc2,lic345b,lic12,fhcb,fhca,rxc12,lxc12,rxc345}.

#command={opena_li12,opena_ffi12,opena_lx12,closea_lfha,closea_lfhb,closea_ffm1,closea_ffm2,closea_ffm3,closea_ffm4,
               open_lfm4,open_lfm3,open_lfm2,open_lfm1,openb_lox12,closea_rfm4,closea_rfm1,closea_rfm2,closea_rfm3,opena_foi345,
               open_rfm4,open_rfm1,close_roi345a,open_rfm3,open_rfm2,closeb_lfm3,closeb_lfm2,closeb_lfm1,openb_rox12,closeb_lfm4,
               opena_ffhb,opena_ffha,opena_rx12,openb_lfi12,openb_rfx12,openb_lfx12,close_rfx345,openb_rfi12,close_lfx345,
               close_rfi345b,close_rfi345a,closea_rfhb,closea_rfha,open_lfi345a,open_lfi345b,open_lfx345,open_loi345b,
               open_loi345a,open_rfi345a,opena_foi12,opena_lfha,opena_lfhb,openb_roi12,open_ffm1,open_ffm3,open_ffm2,
               opena_rfhb,open_ffm4,opena_rfha,closeb_ffm4,closeb_ffm1,closeb_ffm3,closeb_ffm2,closea_ffhb,closea_ffha,
               closea_lfm2,closea_lfm3,closea_lfm1,closea_lfm4,close_loi345b,close_loi345a,opena_ri12,close_roi345b,
               close_lfi345a,close_lfi345b,close_rox345,close_lox345,open_rfx345,openb_loi12,opena_ffi345,open_rfi345b,
               open_rox345,open_lox345,open_roi345a,open_roi345b,closeb_rfm1,closeb_rfm3,closeb_rfm2,closeb_rfm4,closea_fi12,
               opena_ffm5,closea_ffm5,closea_li12,close_lx12,opena_lfm5,closea_lfm5,closea_ri12,close_rx12,opena_rfm5,closea_rfm5,
               closeb_ffi12,closeb_foi12,openb_ffm5,closeb_ffm5,closeb_lfi12,closeb_loi12,close_lfx12,close_lox12,openb_lfm5,
               closeb_lfm5,closeb_rfi12,closeb_roi12,close_rfx12,close_rox12,openb_rfm5,closeb_rfm5,cc(closea_fi12,closeb_ffi12),
               cc(closea_fi12,closeb_foi12),cc(opena_ffm5,openb_ffm5),cc(opena_ffm5,openb_ffm5),cc(closea_ffm5,closeb_ffm5),
               cc(closea_ffm5,closeb_ffm5),cc(closea_li12,closeb_lfi12),cc(closea_li12,closeb_loi12),cc(close_lx12,close_lfx12),
               cc(close_lx12,close_lox12),cc(opena_lfm5,openb_lfm5),cc(closea_lfm5,closeb_lfm5),cc(closea_ri12,closeb_rfi12),
               cc(closea_ri12,closeb_roi12),cc(close_rx12,close_rfx12),cc(close_rx12,close_rox12),cc(opena_rfm5,openb_rfm5),
               cc(closea_rfm5,closeb_rfm5)}.


#system={fwd_rcs,left_rcs,right_rcs,ohms}.

#possible_direction={up,down,left,right,forward,forward,aft}.

#maneuver={minus_x,plus_x,minus_y,plus_y,minus_z,
          plus_z,minus_roll,plus_roll,minus_pitch,plus_pitch,
          minus_yaw,plus_yaw}.


#state={open,closed,gpc}.



#possible_switch_state=in_state(#switch,#state).
#inStateValveState=in_state(#valve,#state).
#abInputState=ab_input(#valve).

#possible_valve_state=#inStateValveState+#abInputState.

#leakingState=leaking(#node).
#pressurizedByState=pressurized_by(#node,#node).
#pressurizedState=pressurized(#node).
#readyToFireState=ready_to_fire(#node).
#fireVernierState=fire_vernier(#node).

#possible_node_state= #leakingState
                     +#pressurizedByState
                     +#pressurizedState
                     +#readyToFireState
                     +#fireVernierState.

#possible_shuttle_state=maneuver_of(#maneuver,#system).

#possible_state=#possible_node_state+#possible_valve_state+#possible_shuttle_state+#possible_switch_state.
#device=#valve+#switch.
#device_type={v_switch,valve}.

#flipAction=flip(#switch,#state).
#possible_action=#flipAction+#command.





predicates

link(#node,#node,#valve).
tank_of(#node,#system).
tank(#node).
jet_of(#node,#system).
vernier_of(#node,#system).
direction(#node,#possible_direction).
pair_of_jets(#node,#node).
leaking(#node).
h(#possible_state,#time).
nh(#possible_state,#time).
has_leak(#valve).
in_state(#device,#possible_state).
damaged(#node).
done(#maneuver,#system).
next(#time,#time).
stuck_device(#device).
stuck(#device,#state).
controls(#switch,#valve).
state_of(#state,#device_type).
occurs(#possible_action,#time).
commands(#possible_action,#valve,#state).
bad_circuitry(#valve).
opposite_state(#state,#state).
dummy_valve(#valve).
of_type(#device,#device_type).
controls_3(#switch,#valve,#circuit).
basic_command(#command,#valve,#state).
action_of(#possible_action,#system).
of_type_3(#device,#system,#device_type).
involved(#system,#time).
goal(#time,#system).
done_1(#system).
aft_system(#system).
use_xfeed(#nat).
type_cc(#command).
goal_1(#nat).
dummy_valve_2(#valve,#system).
issued_commands(#valve,#time).
command_between(#switch,#time,#time).
open_prev(#valve,#time).
prev(#valve,#valve).
perf(#possible_valve_state,#time).

rules

%%%%%%%%%%%%%%%%%%%%%%      FACTS      %%%%%%%%%%%%%%%%%%%%%%%%


%//% link(N1,N2,V) is true iff node N1 is connected to node N2 via valve V.
%//% links for fuel lines

%//% Forward RCS
link(ffh,ff,ffha).
link(ffh,ff,ffhb).
link(ff,ffj,ffdummy).
link(ffj,ff12j,ffi12).
link(ffj,ff345j,ffi345).
link(ff12j,f1u,ffm1).
link(ff12j,f1f,ffm1).
link(ff12j,f1l,ffm1).
link(ff12j,f1d,ffm1).
link(ff12j,f2u,ffm2).
link(ff12j,f2f,ffm2).
link(ff12j,f2r,ffm2).
link(ff12j,f2d,ffm2).
link(ff345j,f3u,ffm3).
link(ff345j,f3f,ffm3).
link(ff345j,f3l,ffm3).
link(ff345j,f3d,ffm3).
link(ff345j,f4r,ffm4).
link(ff345j,f4d,ffm4).
link(ff345j,f5l,ffm5).
link(ff345j,f5r,ffm5).

%//% Left RCS
link(lfh,lf,lfha).
link(lfh,lf,lfhb).
link(lf,lfj,lfdummy).
link(lfj,lf12j,lfi12).
link(lfj,lf345j,lfi345a).
link(lfj,lf345j,lfi345b).
link(lf12j,l1u,lfm1).
link(lf12j,l1a,lfm1).
link(lf12j,l1l,lfm1).
link(lf12j,l2u,lfm2).
link(lf12j,l2d,lfm2).
link(lf12j,l2l,lfm2).
link(lf12j,fxfeed,lfx12).
link(fxfeed,lf12j,lfx12).
link(lf345j,l3a,lfm3).
link(lf345j,l3d,lfm3).
link(lf345j,l3l,lfm3).
link(lf345j,l4u,lfm4).
link(lf345j,l4d,lfm4).
link(lf345j,l4l,lfm4).
link(lf345j,l5d,lfm5).
link(lf345j,l5l,lfm5).
link(lf345j,fxfeed,lfx345).
link(fxfeed,lf345j,lfx345).

%//% Right RCS
link(rfh,rf,rfha).
link(rfh,rf,rfhb).
link(rf,rfj,rfdummy).
link(rfj,rf12j,rfi12).
link(rfj,rf345j,rfi345a).
link(rfj,rf345j,rfi345b).
link(rf12j,r1u,rfm1).
link(rf12j,r1a,rfm1).
link(rf12j,r1r,rfm1).
link(rf12j,r2u,rfm2).
link(rf12j,r2d,rfm2).
link(rf12j,r2r,rfm2).
link(rf12j,fxfeed,rfx12).
link(fxfeed,rf12j,rfx12).
link(rf345j,r3a,rfm3).
link(rf345j,r3d,rfm3).
link(rf345j,r3r,rfm3).
link(rf345j,r4u,rfm4).
link(rf345j,r4d,rfm4).
link(rf345j,r4r,rfm4).
link(rf345j,r5d,rfm5).
link(rf345j,r5r,rfm5).
link(rf345j,fxfeed,rfx345).
link(fxfeed,rf345j,rfx345).


%//% links for oxidizer lines

%//% Forward RCS
link(foh,fo,foha).
link(foh,fo,fohb).
link(fo,foj,fodummy).
link(foj,fo12j,foi12).
link(foj,fo345j,foi345).
link(fo12j,f1u,fom1).
link(fo12j,f1f,fom1).
link(fo12j,f1l,fom1).
link(fo12j,f1d,fom1).
link(fo12j,f2u,fom2).
link(fo12j,f2f,fom2).
link(fo12j,f2r,fom2).
link(fo12j,f2d,fom2).
link(fo345j,f3u,fom3).
link(fo345j,f3f,fom3).
link(fo345j,f3l,fom3).
link(fo345j,f3d,fom3).
link(fo345j,f4r,fom4).
link(fo345j,f4d,fom4).
link(fo345j,f5l,fom5).
link(fo345j,f5r,fom5).

%//% Left RCS
link(loh,lo,loha).
link(loh,lo,lohb).
link(lo,loj,lodummy).
link(loj,lo12j,loi12).
link(loj,lo345j,loi345a).
link(loj,lo345j,loi345b).
link(lo12j,l1u,lom1).
link(lo12j,l1a,lom1).
link(lo12j,l1l,lom1).
link(lo12j,l2u,lom2).
link(lo12j,l2d,lom2).
link(lo12j,l2l,lom2).
link(lo12j,oxfeed,lox12).
link(oxfeed,lo12j,lox12).
link(lo345j,l3a,lom3).
link(lo345j,l3d,lom3).
link(lo345j,l3l,lom3).
link(lo345j,l4u,lom4).
link(lo345j,l4d,lom4).
link(lo345j,l4l,lom4).
link(lo345j,l5d,lom5).
link(lo345j,l5l,lom5).
link(lo345j,oxfeed,lox345).
link(oxfeed,lo345j,lox345).

%//% Right RCS
link(roh,ro,roha).
link(roh,ro,rohb).
link(ro,roj,rodummy).
link(roj,ro12j,roi12).
link(roj,ro345j,roi345a).
link(roj,ro345j,roi345b).
link(ro12j,r1u,rom1).
link(ro12j,r1a,rom1).
link(ro12j,r1r,rom1).
link(ro12j,r2u,rom2).
link(ro12j,r2d,rom2).
link(ro12j,r2r,rom2).
link(ro12j,oxfeed,rox12).
link(oxfeed,ro12j,rox12).
link(ro345j,r3a,rom3).
link(ro345j,r3d,rom3).
link(ro345j,r3r,rom3).
link(ro345j,r4u,rom4).
link(ro345j,r4d,rom4).
link(ro345j,r4r,rom4).
link(ro345j,r5d,rom5).
link(ro345j,r5r,rom5).
link(ro345j,oxfeed,rox345).
link(oxfeed,ro345j,rox345).



%/ tank_of(X,R) is true iff X is a tank of RCS R.

tank_of(ffh,fwd_rcs).    % //% ffh = forward fuel helium tank
tank_of(ff,fwd_rcs).     % //% ff  = forward fuel propellant tank
tank_of(foh,fwd_rcs).    % //% foh = forward oxidizer helium tank
tank_of(fo,fwd_rcs).     %// % fo  = forward oxidizer propellant tank

tank_of(lfh,left_rcs).   % //% lfh = left fuel helium tank.
tank_of(lf,left_rcs).    % //% lf  = left fuel propellant tank.
tank_of(loh,left_rcs).   % //% loh = left oxidizer helium tank.
tank_of(lo,left_rcs).    % //% lo  = left oxidizer propellant tank.

tank_of(rfh,right_rcs).  % //% rfh = right fuel helium tank.
tank_of(rf,right_rcs).   % //% rf  = right fuel propellant tank.
tank_of(roh,right_rcs).  % ////% roh = right oxidizer helium tank.
tank_of(ro,right_rcs).   % //% ro  = right oxidizer propellant tank.%

tank_of(ohmsf,ohms).     % //% ohmsf = ohms fuel propellant tank.
tank_of(ohmso,ohms).     % //% ohmso = ohms oxidizer propellant tank.


%/ jet_of(J,R) is true iff J is a jet belonging to rcs R.

jet_of(f1u,fwd_rcs).     % // f1u = forward_rcs 1 up
jet_of(f1f,fwd_rcs).     % // f1f = forward_rcs 1 forward
jet_of(f1l,fwd_rcs).     % // f1l = forward_rcs 1 left
jet_of(f1d,fwd_rcs).     % // f1d = forward_rcs 1 down
jet_of(f2u,fwd_rcs).     % // f2u = forward_rcs 2 up
jet_of(f2f,fwd_rcs).     % // f2f = forward_rcs 2 forward
jet_of(f2r,fwd_rcs).    %  // f2r = forward_rcs 2 right
jet_of(f2d,fwd_rcs).     % // f2d = forward_rcs 2 down
jet_of(f3u,fwd_rcs).     % // f3u = forward_rcs 3 up
jet_of(f3f,fwd_rcs).     % // f3f = forward_rcs 3 forward
jet_of(f3l,fwd_rcs).     % // f3l = forward_rcs 3 left
jet_of(f3d,fwd_rcs).     % // f3d = forward_rcs 3 down
jet_of(f4r,fwd_rcs).     % // f4r = forward_rcs 4 right
jet_of(f4d,fwd_rcs).     % // f4d = forward_rcs 4 down

jet_of(l1u,left_rcs).    % // l1u = left_rcs 1 up
jet_of(l1a,left_rcs).  %   // l1f = left_rcs 1 aft
jet_of(l1l,left_rcs).    % // l1l = left_rcs 1 left
jet_of(l2u,left_rcs).    % // l2u = left_rcs 2 up
jet_of(l2l,left_rcs).   %  // l2f = left_rcs 2 left
jet_of(l2d,left_rcs).    % // l2d = left_rcs 2 down
jet_of(l3a,left_rcs).   %  // l3u = left_rcs 3 aft
jet_of(l3l,left_rcs).   %  // l3l = left_rcs 3 left
jet_of(l3d,left_rcs).   %  // l3d = left_rcs 3 down
jet_of(l4u,left_rcs).   %  // l4d = left_rcs 4 up
jet_of(l4l,left_rcs).   %  // l4r = left_rcs 4 left
jet_of(l4d,left_rcs).    % // l4d = left_rcs 4 down

jet_of(r1u,right_rcs).  %  // r1u = right_rcs 1 up
jet_of(r1a,right_rcs).  %  // r1f = right_rcs 1 aft
jet_of(r1r,right_rcs).  %  // r1l = right_rcs 1 right
jet_of(r2u,right_rcs).  %  // r2u = right_rcs 2 up
jet_of(r2r,right_rcs).   % // r2f = right_rcs 2 right
jet_of(r2d,right_rcs).  %  // r2d = right_rcs 2 down
jet_of(r3a,right_rcs).   % // r3u = right_rcs 3 aft
jet_of(r3r,right_rcs).   % // r3l = right_rcs 3 right
jet_of(r3d,right_rcs).   % // r3d = right_rcs 3 down
jet_of(r4u,right_rcs).   % // r4d = right_rcs 4 up
jet_of(r4r,right_rcs).   % // r4r = right_rcs 4 right
jet_of(r4d,right_rcs).   %// r4d = right_rcs 4 down

%// verniers - small jets

vernier_of(f5l,fwd_rcs).     %// f5l = forward_rcs 5 left
vernier_of(f5r,fwd_rcs).     %// f5r = forward_rcs 5 right
vernier_of(l5l,left_rcs).   % // l5l = left_rcs 5 left
vernier_of(l5d,left_rcs).    %// l5d = left_rcs 5 down
vernier_of(r5r,right_rcs).   %// r5r = right_rcs 5 right
vernier_of(r5d,right_rcs).   %// r5d = right_rcs 5 down

%// direction(J,D) iff jet J fires in direction D.

direction(f1f,forward).
direction(f2f,forward).
direction(f3f,forward).

direction(f1u,up).
direction(f2u,up).
direction(f3u,up).
direction(l1u,up).
direction(l2u,up).
direction(l4u,up).
direction(r1u,up).
direction(r2u,up).
direction(r4u,up).

direction(f1d,down).
direction(f2d,down).
direction(f3d,down).
direction(f4d,down).
direction(l2d,down).
direction(l3d,down).
direction(l4d,down).
direction(r2d,down).
direction(r3d,down).
direction(r4d,down).

direction(l1a,aft).
direction(l3a,aft).
direction(r1a,aft).
direction(r3a,aft).

direction(f1l,left).
direction(f3l,left).
direction(l1l,left).
direction(l2l,left).
direction(l3l,left).
direction(l4l,left).

direction(f2r,right).
direction(f4r,right).
direction(r1r,right).
direction(r2r,right).
direction(r3r,right).
direction(r4r,right).


pair_of_jets(f1d,f2d).
pair_of_jets(f1d,f4d).
pair_of_jets(f3d,f2d).
pair_of_jets(f3d,f4d).

%% A node is leaking at any time it is connected to a leaking valve 
%% which is open.

h(leaking(N1), T) :- link(N1,N2,V),
                     has_leak(V),
                     h(in_state(V,open),T).
					 
%% A node is leaking if it is connected by an open valve to another node 
%% which is leaking.

h(leaking(N1),T) :- link(N1,N2,V),
                    h(in_state(V,open),T),
                    h(leaking(N2),T).


%% Non-tank node N1 is pressurized by tank X if it is not leaking and
%% is connected by an open valve to a node which is pressurized by tank X.

h(pressurized_by(N1,X),T) :-not tank_of(N1,R),
                             link(N2,N1,V),
                             h(in_state(V,open),T),
                             tank_of(X,R),
                             h(pressurized_by(N2,X),T),
                             not h(leaking(N1),T).
%% Tank node N1 is pressurized by tank X if it is connected
%% by an open valve to a node which is pressurized by tank X. 

h(pressurized_by(N1,X),T) :- tank_of(N1,R),
                             link(N2,N1,V),
                             h(in_state(V,open),T),
                             tank_of(X,R),
                             h(pressurized_by(N2,X),T).
							 
							 
%% The crossfeeds cannot be simultaneously pressurized by two tanks.

:- tank_of(X,R),
   tank_of(Y,R1),
   X!=Y,
   h(pressurized_by(fxfeed,X),T),
   h(pressurized_by(fxfeed,Y),T).   

:- tank_of(X,R),
   tank_of(Y,R1),
   X!=Y,
   h(pressurized_by(oxfeed,X),T),
   h(pressurized_by(oxfeed,Y),T).   




%% The crossfeeds cannot be simultaneously pressurized by two tanks.


%% A jet is ready to fire iff it is pressurized by both fuel
%% and oxidizer tanks and it is not damaged. 
                         
h(ready_to_fire(J),T) :- jet_of(J,R),
                         tank_of(TK1,R1),	%%% marcy 05/12/2003
                         tank_of(TK2,R2),	%%% marcy 05/12/2003
                         TK1!=TK2,
                         h(pressurized_by(J,TK1),T), 
                         h(pressurized_by(J,TK2),T), 
                         not damaged(J). 
h(fire_vernier(J),T) :-  vernier_of(J,R),
                         tank_of(TK1,R),
                         tank_of(TK2,R),
                         TK1!=TK2,
                         h(pressurized_by(J,TK1),T), 
                         h(pressurized_by(J,TK2),T), 
                         not damaged(J). 
% The shuttle is ready for a maneuver X iff a set of jets
% satisfying the requirements for maneuver X are ready to fire.

% In order to increase the efficiency in planning the actions
% required for a maneuver, we represent a maneuver X by its
% three subparts each corresponding to the portion executed by
% a different RCS subsystem
% 		h(maneuver_of(X,S),T).
%
% If maneuver X does not involve any action of RCS subsystem S
% we add atom "done(X,S)" to the description. The following rule 
% ensures that maneuver X of subsystem S is ready at time T.
                  
h(maneuver_of(X,S),T) :-
                      done(X,S).
					  
%% The shuttle is ready for maneuver +X if an aft jet is ready
%% to fire on both the left and right rcs.

h(maneuver_of(plus_x,left_rcs),T) :- jet_of(J,left_rcs),
                                  direction(J,aft),
                                  h(ready_to_fire(J),T).

h(maneuver_of(plus_x,right_rcs),T) :- jet_of(J,right_rcs),
                                   direction(J,aft),
                                   h(ready_to_fire(J),T).
done(plus_x,fwd_rcs).


%% The shuttle is ready for maneuver -X if two of the forward 
%% jets on the forward rcs are ready to fire.

h(maneuver_of(minus_x,fwd_rcs),T) :- jet_of(J1,fwd_rcs),
                                  jet_of(J2,fwd_rcs),
                                  direction(J1,forward),
                                  direction(J2,forward),
                                  J1!=J2,
                                  h(ready_to_fire(J1),T),
                                  h(ready_to_fire(J2),T).

done(minus_x,left_rcs).
done(minus_x,right_rcs).



%% The shuttle is ready for maneuver +Y if a left jet is ready
%% to fire on both the left and forward rcs.

h(maneuver_of(plus_y,left_rcs),T) :- jet_of(J,left_rcs),         
                                  direction(J,left),                         
                                  h(ready_to_fire(J),T).
                        
h(maneuver_of(plus_y,fwd_rcs),T) :- jet_of(J,fwd_rcs),
                                 direction(J,left),
                                 h(ready_to_fire(J),T).

done(plus_y,right_rcs).



%% The shuttle is ready for maneuver -Y if a right jet is ready
%% to fire on both the right and forward rcs.

h(maneuver_of(minus_y,right_rcs),T) :- jet_of(J,right_rcs),
                                    direction(J,right),
                                    h(ready_to_fire(J),T).

h(maneuver_of(minus_y,fwd_rcs),T) :- jet_of(J,fwd_rcs), 
                                  direction(J,right), 
                                  h(ready_to_fire(J),T).

done(minus_y,left_rcs).



%% The shuttle is ready for maneuver +Z if an upward jet is ready
%% to fire on all three rcs'.

h(maneuver_of(plus_z,left_rcs),T) :- jet_of(J,left_rcs),
                                  direction(J,up),
                                  h(ready_to_fire(J),T).

h(maneuver_of(plus_z,right_rcs),T) :-jet_of(J,right_rcs),
                                   direction(J,up),   
                                   h(ready_to_fire(J),T).

 
h(maneuver_of(plus_z,fwd_rcs),T) :-  jet_of(J,fwd_rcs), 
                                 direction(J,up),  
                                 h(ready_to_fire(J),T).

 

%% The shuttle is ready for maneuver -Z if a downward jet is 
%% ready to fire on the right and left rcs, and a pair of
%% downward jets on the forward rcs.

h(maneuver_of(minus_z,left_rcs),T) :- jet_of(J,left_rcs),
                                   direction(J,down),
                                   h(ready_to_fire(J),T).
 
h(maneuver_of(minus_z,right_rcs),T) :- jet_of(J,right_rcs), 
                                    direction(J,down),
                                    h(ready_to_fire(J),T).
  
h(maneuver_of(minus_z,fwd_rcs),T) :- pair_of_jets(J1,J2), 
                                  h(ready_to_fire(J1),T), 
                                  h(ready_to_fire(J2),T). 
 


%% The shuttle is ready for maneuver +roll if a downward jet on the
%% left rcs and an upward jet on the right rcs are both ready to fire.

h(maneuver_of(plus_roll,left_rcs),T) :- jet_of(J,left_rcs),
                                     direction(J,down),                      
                                     h(ready_to_fire(J),T). 

h(maneuver_of(plus_roll,right_rcs),T) :- jet_of(J,right_rcs),
                                      direction(J,up),
                                      h(ready_to_fire(J),T).

done(plus_roll,fwd_rcs).



%% The shuttle is ready for maneuver -roll if an upward jet on the
%% left rcs and a downward jet on the right rcs are both ready to fire.

h(maneuver_of(minus_roll,left_rcs),T) :- jet_of(J,left_rcs),
                                      direction(J,up),
                                      h(ready_to_fire(J),T). 

h(maneuver_of(minus_roll,right_rcs),T) :- jet_of(J,right_rcs), 
                                       direction(J,down),
                                       h(ready_to_fire(J),T).
done(minus_roll,fwd_rcs).



%% The shuttle is ready for maneuver +pitch if a pair of downward 
%% jets are ready to fire on the forward rcs and an upward jet is
%% ready to fire on the left and right rcs.

h(maneuver_of(plus_pitch,fwd_rcs),T) :- pair_of_jets(J1,J2),
                                     h(ready_to_fire(J1),T),
                                     h(ready_to_fire(J2),T).

h(maneuver_of(plus_pitch,left_rcs),T) :- jet_of(J,left_rcs), 
                                      direction(J,up), 
                                      h(ready_to_fire(J),T).
 
h(maneuver_of(plus_pitch,right_rcs),T) :- jet_of(J,right_rcs), 
                                       direction(J,up), 
                                       h(ready_to_fire(J),T). 
 


%% The shuttle is ready for maneuver -pitch if an upward jet is 
%% ready to fire on the forward rcs and a downward jet is ready
%% to fire on the left and right rcs.

h(maneuver_of(minus_pitch,fwd_rcs),T) :- jet_of(J,fwd_rcs),
                                      direction(J,up), 
                                      h(ready_to_fire(J),T).

h(maneuver_of(minus_pitch,left_rcs),T) :- jet_of(J,left_rcs),
                                       direction(J,down),  
                                       h(ready_to_fire(J),T).

h(maneuver_of(minus_pitch,right_rcs),T) :- jet_of(J,right_rcs),
                                        direction(J,down),  
                                        h(ready_to_fire(J),T).              
                    

%% The shuttle is ready for maneuver +yaw if a right jet is ready
%% to fire on the right rcs and a left jet is ready to fire on the 
%% forward rcs.

h(maneuver_of(plus_yaw,right_rcs),T) :- jet_of(J,right_rcs),
                                     direction(J,right),
                                     h(ready_to_fire(J),T).

h(maneuver_of(plus_yaw,fwd_rcs),T) :- jet_of(J,fwd_rcs),
                                   direction(J,left),   
                                   h(ready_to_fire(J),T).

done(plus_yaw,left_rcs).


%% The shuttle is ready for maneuver -yaw if a left jet is ready
%% to fire on the left rcs and a right jet is ready to fire on the 
%% forward rcs.

h(maneuver_of(minus_yaw,left_rcs),T) :- jet_of(J,left_rcs),
                                     direction(J,left),
                                     h(ready_to_fire(J),T).

h(maneuver_of(minus_yaw,fwd_rcs),T) :- jet_of(J,fwd_rcs),
                                    direction(J,right), 
                                    h(ready_to_fire(J),T).
   
done(minus_yaw,right_rcs).

%%%%%%%%%%%%%%%%%%%%%    Inertia Laws    %%%%%%%%%%%%%%%%%%%%%%%%


% Tanks mantain correct pressure unless some leak
% occurs along their path for some time.

h(pressurized_by(X,X),T1) :- next(T,T1),
                             tank_of(X,R),
                             h(pressurized_by(X,X),T),
                             not nh(pressurized_by(X,X),T1).

	

%%%%%%%%%%%%%%%    Consistency constraints   %%%%%%%%%%%%%%%%%%

:- link(N2,N,V),
   tank_of(X,R),                      
   h(pressurized_by(N,X),T),
   nh(pressurized_by(N,X),T).

:- link(N,N2,V),
   h(leaking(N),T),
   nh(leaking(N),T).



%%%%%%%%%%%%   End of Module: Hydraulic System   %%%%%%%%%%%%%%
%%%%%%%%%%%%%%%   Module: Valve Control System    %%%%%%%%%%%%%%
%
% Valves of the RCS system can be opened and closed by manipulating 
% mechanical switches connected to them or by issuing computer commands.
% Under normal circumstances, the on-board general purpose computer(s) 
% will be in charge of opening/closing valves and will achieve this
% objective by sending computer commands. If the computer system is
% malfunctioning an astronaut can normally override these commands by
% manually flipping the switches that control the valves to be
% opened/closed. Switches and computer commands are connected to 
% valves by electrical circuits. 
% 
% The VCS describes how computer commands and changes in the position 
% of switches affect the state of valves. The VCS has two levels of detail. 
% At the higher level, it is assumed that all electrical circuits are 
% working properly and therefore circuits are not included in the 
% representation. The lower level includes information about electrical
% circuits of the system. It is normally used when some of the circuits 
% are malfunctioning, and therefore flipping the switches and issuing 
% the computer commands can produce results unexpected by the high-level
% representation. 
%%
%%
%% HIGH-LEVEL VALVE CONTROL SYSTEM
%% ===============================
%%
%% At this level, the VCS is described by a set of switches, computer 
%% commands and valves, and by connections among them. Switches and valves 
%% will be called "devices". 
%%
%% Connections between switches and valves are described by the relation
%%
%%				controls(Sw,V)
%%
%% meaning that switch Sw controls the state of valve V. Connections 
%% between computer commands and valves are described by the relation
%%
%%				commands(CC,V,S)
%%
%% meaning that computer command CC commands valve V to move to state S.
%% A malfunctioning of the circuitry controlling valve V is represented 
%% by the relation
%%				bad_circuitry(V)
%%
%% The fact that a device is malfunctioning is represented by the relation
%%
%%				stuck(D,S)
%%
%% meaning that device D is stuck in state S. The rule

stuck_device(D) :- stuck(D,S).

%% says that a device D is stuck if it is stuck in state S.
%%
%% Normally there is a set, C, of computer commands responsible for 
%% moving a valve V into state S. There are two types of such sets, denoted 
%% by "and" and "or". If C is of type "and" then V is moved to S only if all 
%% the commands from C are issued simultaneously. Otherwise issuing one 
%% command from C is sufficient to achieve the desired effect. Information 
%% about the type of C is recorded by the statement
%%
%%				command_type(V,S,Ty)
%%
%% meaning that the set of computer commands responsible for moving valve V
%% into state S is of type Ty.
%%
%%
%% The dynamic behaviour of the high-level VCS is described by a set of fluents
%% and actions. Actions are represented as follows:
%%         
%%        action(flip(D,S)) - flips switch D to state S 
%%        action(cc(V,S)) - issue all the computer commands required to move
%%                          valve V to state S
%%        action(CC) - issues computer command CC.
%% 
%% The state of a device is described by the fluent
%%
%%				in_state(D,S)
%%
%% meaning that device D is in state S. 
%% Normally computer commands are issued to a valve only when the switch
%% connected to the valve is in gpc state. If a computer command is issued 
%% when the switch is not in gpc state, the state of the valve is undefined 
%% in the high-level VCS and the input is considered abnormal.
%% This is represented by the fluent
%%
%%				ab_input(V)
%%
%% The input of high-level VCS consists of:
%%   1) a collection of statements of the form h(is_state(D,S),0) describing
%%      the states of switches and valves in the initial situation;
%%   2) the description of possible malfunctionings of switches and valves;
%%   3) the sequence of actions which defines the past history of events up to
%%      moment T.
%% Notice that fluents of the form ab_input(V) cannot be part of the description
%% of the initial situation and this is enforced by the constraint:

:- controls(Sw,V), h(ab_input(V),0).
%% The output of this module is a description of the state of valves and 
%% switches at time T+1.
%%
%%
%% The effects of actions are defined by the following dynamic causal laws.
%%

%% If a properly working switch Sw is flipped to state S at time T, then
%% Sw will be in state S at the next moment of time.    

h(in_state(Sw,S),T1) :- next(T,T1),
                        of_type(Sw,v_switch),
                        state_of(S,v_switch),
                        occurs(flip(Sw,S),T),
                        not stuck_device(Sw).
%% If the switch controlling valve V is in gpc state, V is working
%% properly, and all the computer commands required to move V to some state S
%% were issued at time T, then V is in state S at the next moment of time.

h(in_state(V,S),T1) :- next(T,T1),
                       controls(Sw,V),
                       h(in_state(Sw,gpc),T),
		               occurs(CC,T),
                       commands(CC,V,S),
                       not stuck_device(V),
                       not bad_circuitry(V).

%% Note: we need to consider the case when two opposite cc's are issued.
%% In this case, the basic level can not determine the state of the valve,
%% instead the extended level should be used.
%% For that, we need to include condition: not ab_input(V) to the above
%% rule, and add a new rule with h(ab_input(V),T) as head saying that it is
%% abnormal if two opposite cc's are issued simultaneously at T.



%% A static connection between switches and valves is expressed by a 
%% static law. /
%%
%% If switch Sw controlling a valve V is in some state S (open or closed)
%% at time T, and both V and its connection to Sw are working properly, and 
%% the input is not abnormal, then V is also in state S at the same time.

h(in_state(V,S),T) :- controls(Sw,V),
                      h(in_state(Sw,S),T),
                      state_of(S,v_switch),
                      S!=gpc,
                      not h(ab_input(V),T),
                      not stuck_device(V),
                      not bad_circuitry(V).
%% If switch Sw controlling a valve V is in some state S (open or closed)
%% at time T, and all computer command required to move V to some state P
%% (different from S) were issued at time T, then the input to V is considered 
%% abnormal at time T+1, i.e. the state of V is undefined in high-level VCS.

h(ab_input(V),T1) :- next(T,T1),
                     controls(Sw,V),
                     h(in_state(Sw,S),T),
		             occurs(CC,T),
                     commands(CC,V,P),
                     state_of(S,v_switch),
                     S!=gpc,
                     S!=P,
                     not bad_circuitry(V).

%% A device is always on a state S if it stuck at S.

h(in_state(D,S),0) :- stuck(D,S).

%%%%%%%%%%%%%%%%%%%%%%%%      FACTS      %%%%%%%%%%%%%%%%%%%%%%%%


%% state_of(S,D) is true iff S is the state of device D. 

state_of(open, v_switch).
state_of(closed, v_switch).
state_of(gpc, v_switch).

state_of(open,valve).
state_of(closed,valve).

opposite_state(open,closed).
opposite_state(closed,open).
					  

%% of_type(D,Dev) is true iff D is a device of type Dev. 
 
of_type(V,valve)    :- controls(D,V). 
of_type(D,v_switch) :- controls(D,V).

of_type(V,valve) :- dummy_valve(V).
dummy_valve(ffdummy).
dummy_valve(fodummy).
dummy_valve(lfdummy).
dummy_valve(lodummy).
dummy_valve(rfdummy).
dummy_valve(rodummy).

%% controls(D,V) is true iff switch D controls valve V.
%% Each switch controls simultaneously two different valves.

controls(D,V) :- controls_3(D,V,C).
%% Forward RCS
controls_3(fha,ffha,fhca).
controls_3(fha,foha,fhca).
controls_3(fhb,ffhb,fhcb).
controls_3(fhb,fohb,fhcb).
controls_3(fi12,ffi12,fic12).
controls_3(fi12,foi12,fic12).
controls_3(fi345,ffi345,fic345).
controls_3(fi345,foi345,fic345).
controls_3(fm1,ffm1,fmc1).
controls_3(fm1,fom1,fmc1).
controls_3(fm2,ffm2,fmc2).
controls_3(fm2,fom2,fmc2).
controls_3(fm3,ffm3,fmc3).
controls_3(fm3,fom3,fmc3).
controls_3(fm4,ffm4,fmc4).
controls_3(fm4,fom4,fmc4).
controls_3(fm5,ffm5,fmc5).
controls_3(fm5,fom5,fmc5).

%% Left RCS 
controls_3(lha,lfha,lhca). 
controls_3(lha,loha,lhca). 
controls_3(lhb,lfhb,lhcb). 
controls_3(lhb,lohb,lhcb). 
controls_3(li12,lfi12,lic12). 
controls_3(li12,loi12,lic12). 
controls_3(li345a,lfi345a,lic345a). 
controls_3(li345a,loi345a,lic345a). 
controls_3(li345b,lfi345b,lic345b). 
controls_3(li345b,loi345b,lic345b). 
controls_3(lm1,lfm1,lmc1). 
controls_3(lm1,lom1,lmc1). 
controls_3(lm2,lfm2,lmc2). 
controls_3(lm2,lom2,lmc2). 
controls_3(lm3,lfm3,lmc3). 
controls_3(lm3,lom3,lmc3). 
controls_3(lm4,lfm4,lmc4). 
controls_3(lm4,lom4,lmc4). 
controls_3(lm5,lfm5,lmc5). 
controls_3(lm5,lom5,lmc5). 
controls_3(lx12,lfx12,lxc12). 
controls_3(lx12,lox12,lxc12). 
controls_3(lx345,lfx345,lxc345). 
controls_3(lx345,lox345,lxc345). 


%% Right RCS 
controls_3(rha,rfha,rhca). 
controls_3(rha,roha,rhca). 
controls_3(rhb,rfhb,rhcb). 
controls_3(rhb,rohb,rhcb). 
controls_3(ri12,rfi12,ric12). 
controls_3(ri12,roi12,ric12). 
controls_3(ri345a,rfi345a,ric345a). 
controls_3(ri345a,roi345a,ric345a). 
controls_3(ri345b,rfi345b,ric345b). 
controls_3(ri345b,roi345b,ric345b). 
controls_3(rm1,rfm1,rmc1). 
controls_3(rm1,rom1,rmc1). 
controls_3(rm2,rfm2,rmc2). 
controls_3(rm2,rom2,rmc2). 
controls_3(rm3,rfm3,rmc3). 
controls_3(rm3,rom3,rmc3). 
controls_3(rm4,rfm4,rmc4). 
controls_3(rm4,rom4,rmc4). 
controls_3(rm5,rfm5,rmc5). 
controls_3(rm5,rom5,rmc5). 
controls_3(rx12,rfx12,rxc12). 
controls_3(rx12,rox12,rxc12). 
controls_3(rx345,rfx345,rxc345). 
controls_3(rx345,rox345,rxc345). 
%% basic_command(CC,V,S) is true iff a single computer command CC 
%%                       moves valve V to state S.

%% Forward RCS

%% Commands to control valves ffha, ffhb, foha, fohb
basic_command(opena_ffha,ffha,open).
basic_command(opena_ffha,foha,open).
basic_command(closea_ffha,ffha,closed).
basic_command(closea_ffha,foha,closed).
basic_command(opena_ffhb,ffhb,open).
basic_command(opena_ffhb,fohb,open).
basic_command(closea_ffhb,ffhb,closed).
basic_command(closea_ffhb,fohb,closed).

%% Commands to control valves ffi12, foi12
basic_command(opena_ffi12,ffi12,open).
basic_command(opena_foi12,foi12,open).

%% Commands to control valves ffi345, foi345
basic_command(opena_ffi345,ffi345,open).
basic_command(opena_foi345,foi345,open).

%% Commands to control valves (manifolds) ffm1, fom1
basic_command(open_ffm1,ffm1,open).
basic_command(open_ffm1,fom1,open).
basic_command(closea_ffm1,ffm1,closed).
basic_command(closea_ffm1,fom1,closed).
basic_command(closeb_ffm1,ffm1,closed).
basic_command(closeb_ffm1,fom1,closed).

%% Commands to control valves (manifolds) ffm2, fom2
basic_command(open_ffm2,ffm2,open).
basic_command(open_ffm2,fom2,open).
basic_command(closea_ffm2,ffm2,closed).
basic_command(closea_ffm2,fom2,closed).
basic_command(closeb_ffm2,ffm2,closed).
basic_command(closeb_ffm2,fom2,closed).

%% Commands to control valves (manifolds) ffm3, fom3
basic_command(open_ffm3,ffm3,open).
basic_command(open_ffm3,fom3,open).
basic_command(closea_ffm3,ffm3,closed).
basic_command(closea_ffm3,fom3,closed).
basic_command(closeb_ffm3,ffm3,closed).
basic_command(closeb_ffm3,fom3,closed).

%% Commands to control valves (manifolds) ffm4, fom4
basic_command(open_ffm4,ffm4,open).
basic_command(open_ffm4,fom4,open).
basic_command(closea_ffm4,ffm4,closed).
basic_command(closea_ffm4,fom4,closed).
basic_command(closeb_ffm4,ffm4,closed).
basic_command(closeb_ffm4,fom4,closed).



%% Left RCS

%% Commands to control valves lfha, lohb, lfha, lohb 
basic_command(opena_lfha,lfha,open). 
basic_command(opena_lfha,loha,open). 
basic_command(closea_lfha,lfha,closed). 
basic_command(closea_lfha,loha,closed). 
basic_command(opena_lfhb,lfhb,open). 
basic_command(opena_lfhb,lohb,open). 
basic_command(closea_lfhb,lfhb,closed). 
basic_command(closea_lfhb,lohb,closed). 

%% Commands to control valves lfi12, loi12 
basic_command(opena_li12,lfi12,open). 
basic_command(opena_li12,loi12,open). 
basic_command(openb_lfi12,lfi12,open). 
basic_command(openb_loi12,loi12,open).

%% Commands to control valves (crossfeeds) lfx12, lox12 
basic_command(opena_lx12,lfx12,open). 
basic_command(opena_lx12,lox12,open). 
basic_command(openb_lfx12,lfx12,open). 
basic_command(openb_lox12,lox12,open).

%% Commands to control valves lfi345a, lfi345b, loi345a, lfi345b 
basic_command(open_lfi345a,lfi345a,open). 
basic_command(open_loi345a,loi345a,open).
basic_command(close_lfi345a,lfi345a,closed). 
basic_command(close_loi345a,loi345a,closed).  
 
basic_command(open_lfi345b,lfi345b,open). 
basic_command(open_loi345b,loi345b,open).  
basic_command(close_lfi345b,lfi345b,closed). 
basic_command(close_loi345b,loi345b,closed).

%% Commands to control valves (crossfeeds) lfx345, lox345
basic_command(open_lfx345,lfx345,open). 
basic_command(open_lox345,lox345,open).
basic_command(close_lfx345,lfx345,closed). 
basic_command(close_lox345,lox345,closed).   

%% Commands to control valves (manifolds) lfm1, lom1 
basic_command(open_lfm1,lfm1,open). 
basic_command(open_lfm1,lom1,open). 
basic_command(closea_lfm1,lfm1,closed). 
basic_command(closea_lfm1,lom1,closed). 
basic_command(closeb_lfm1,lfm1,closed). 
basic_command(closeb_lfm1,lom1,closed). 

%% Commands to control valves (manifolds) lfm2, lom2 
basic_command(open_lfm2,lfm2,open). 
basic_command(open_lfm2,lom2,open). 
basic_command(closea_lfm2,lfm2,closed). 
basic_command(closea_lfm2,lom2,closed). 
basic_command(closeb_lfm2,lfm2,closed). 
basic_command(closeb_lfm2,lom2,closed). 

%% Commands to control valves (manifolds) lfm3, lom3 
basic_command(open_lfm3,lfm3,open). 
basic_command(open_lfm3,lom3,open). 
basic_command(closea_lfm3,lfm3,closed). 
basic_command(closea_lfm3,lom3,closed). 
basic_command(closeb_lfm3,lfm3,closed). 
basic_command(closeb_lfm3,lom3,closed). 

%% Commands to control valves (manifolds) lfm4, lom4 
basic_command(open_lfm4,lfm4,open). 
basic_command(open_lfm4,lom4,open). 
basic_command(closea_lfm4,lfm4,closed). 
basic_command(closea_lfm4,lom4,closed). 
basic_command(closeb_lfm4,lfm4,closed). 
basic_command(closeb_lfm4,lom4,closed). 


%% Right RCS

%% Commands to control valves rfha, rohb, rfha, rohb 
basic_command(opena_rfha,rfha,open). 
basic_command(opena_rfha,roha,open). 
basic_command(closea_rfha,rfha,closed). 
basic_command(closea_rfha,roha,closed). 
basic_command(opena_rfhb,rfhb,open). 
basic_command(opena_rfhb,rohb,open). 
basic_command(closea_rfhb,rfhb,closed). 
basic_command(closea_rfhb,rohb,closed). 

%% Commands to control valves rfi12, roi12 
basic_command(opena_ri12,rfi12,open). 
basic_command(opena_ri12,roi12,open).
basic_command(openb_rfi12,rfi12,open). 
basic_command(openb_roi12,roi12,open).

%% Commands to control valves (crossfeeds) rfx12, rox12
basic_command(opena_rx12,rfx12,open). 
basic_command(opena_rx12,rox12,open).
basic_command(openb_rfx12,rfx12,open). 
basic_command(openb_rox12,rox12,open).

%% Commands to control valves rfi345a, rfi345b, roi345a, fi345b 
basic_command(open_rfi345a,rfi345a,open). 
basic_command(open_roi345a,roi345a,open).
basic_command(close_rfi345a,rfi345a,closed). 
basic_command(close_roi345a,roi345a,closed).
 
basic_command(open_rfi345b,rfi345b,open). 
basic_command(open_roi345b,roi345b,open).
basic_command(close_rfi345b,rfi345b,closed). 
basic_command(close_roi345b,roi345b,closed).

%% Commands to control valves (crossfeeds) rfx345, rox345
basic_command(open_rfx345,rfx345,open). 
basic_command(open_rox345,rox345,open).
basic_command(close_rfx345,rfx345,closed). 
basic_command(close_rox345,rox345,closed).

%% Commands to control valves (manifolds) rfm1, rom1 
basic_command(open_rfm1,rfm1,open). 
basic_command(open_rfm1,rom1,open). 
basic_command(closea_rfm1,rfm1,closed). 
basic_command(closea_rfm1,rom1,closed). 
basic_command(closeb_rfm1,rfm1,closed). 
basic_command(closeb_rfm1,rom1,closed). 

%% Commands to control valves (manifolds) rfm2, rom2 
basic_command(open_rfm2,rfm2,open). 
basic_command(open_rfm2,rom2,open). 
basic_command(closea_rfm2,rfm2,closed). 
basic_command(closea_rfm2,rom2,closed). 
basic_command(closeb_rfm2,rfm2,closed). 
basic_command(closeb_rfm2,rom2,closed). 

%% Commands to control valves (manifolds) rfm3, rom3 
basic_command(open_rfm3,rfm3,open). 
basic_command(open_rfm3,rom3,open). 
basic_command(closea_rfm3,rfm3,closed). 
basic_command(closea_rfm3,rom3,closed). 
basic_command(closeb_rfm3,rfm3,closed). 
basic_command(closeb_rfm3,rom3,closed). 

%% Commands to control valves (manifolds) rfm4, rom4 
basic_command(open_rfm4,rfm4,open). 
basic_command(open_rfm4,rom4,open). 
basic_command(closea_rfm4,rfm4,closed). 
basic_command(closea_rfm4,rom4,closed). 
basic_command(closeb_rfm4,rfm4,closed). 
basic_command(closeb_rfm4,rom4,closed). 

%% commands(CC,V,S) is true iff computer command CC moves
%%                  valve V to state S.

commands(CC,V,S) :- basic_command(CC,V,S).





%% commands(cc(CC1,CC2),V,S) is true iff both computer commands
%%                           CC1 and CC2 must be issued to move
%%                           valve V to state S.


%% Forward RCS

commands(cc(closea_fi12,closeb_ffi12),ffi12,closed). 
commands(cc(closea_fi12,closeb_foi12),foi12,closed). 

commands(cc(opena_ffm5,openb_ffm5),ffm5,open).
commands(cc(opena_ffm5,openb_ffm5),fom5,open). 

commands(cc(closea_ffm5,closeb_ffm5),ffm5,closed). 
commands(cc(closea_ffm5,closeb_ffm5),fom5,closed).

%%% Question for Matt: should openc be or with closec???   
%%commands(openc_ffm5,ffm5,open).
%%commands(openc_ffm5,fom5,open).
%%commands(closec_ffm5,ffm5,closed).
%%commands(closec_ffm5,fom5,closed).



%% Left RCS

commands(cc(closea_li12,closeb_lfi12),lfi12,closed). 
commands(cc(closea_li12,closeb_loi12),loi12,closed). 

commands(cc(close_lx12,close_lfx12),lfx12,closed). 
commands(cc(close_lx12,close_lox12),lox12,closed). 

commands(cc(opena_lfm5,openb_lfm5),lfm5,open). 
commands(cc(opena_lfm5,openb_lfm5),lom5,open).
  
commands(cc(closea_lfm5,closeb_lfm5),lfm5,closed). 
commands(cc(closea_lfm5,closeb_lfm5),lom5,closed).

%%% Question for Matt: should openc be or with closec???   
%%commands(openc_lfm5,lfm5,open). 
%%commands(openc_lfm5,lom5,open). 
%%commands(closec_lfm5,lfm5,closed). 
%%commands(closec_lfm5,lom5,closed). 



%% Right RCS

commands(cc(closea_ri12,closeb_rfi12),rfi12,closed). 
commands(cc(closea_ri12,closeb_roi12),roi12,closed).

commands(cc(close_rx12,close_rfx12),rfx12,closed). 
commands(cc(close_rx12,close_rox12),rox12,closed).  

commands(cc(opena_rfm5,openb_rfm5),rfm5,open). 
commands(cc(opena_rfm5,openb_rfm5),rom5,open).
  
commands(cc(closea_rfm5,closeb_rfm5),rfm5,closed). 
commands(cc(closea_rfm5,closeb_rfm5),rom5,closed).

%%% Question for Matt: should openc be or with closec???   
%%commands(openc_rfm5,rfm5,open). 
%%commands(openc_rfm5,rom5,open). 
%%commands(closec_rfm5,rfm5,closed). 
%%commands(closec_rfm5,rom5,closed). 

%%%%% Commands to open xfeed valves???
%%%%%%%%%%%%%%%%%%   Impossibility conditions  %%%%%%%%%%%%%%%%% 

%% A switch cannot be moved to a state it is already in.

:- of_type(Sw,v_switch),
   state_of(S,v_switch),   
   h(in_state(Sw,S),T),
   occurs(flip(Sw,S),T). 






%%%%%%%%%%%%%%%%%%%%%    Inertia Law    %%%%%%%%%%%%%%%%%%%%%%%%

%% The inertia law expresses the following default law: 
%% "Normally, things tend to stay as they were."

h(in_state(D,S),T1) :- next(T,T1),
                       of_type(D,Dev),                    
                       state_of(S,Dev),
                       h(in_state(D,S),T),
                       not nh(in_state(D,S),T1).

nh(in_state(D,S),T) :- of_type(D,Dev),
                       state_of(S,Dev),
                       state_of(S1,Dev),
                       S!=S1,
                       h(in_state(D,S1),T).


%%%%%%%%%%%%%%%%%    Consistency constraints   %%%%%%%%%%%%%%%%%%

%% A device can only be in one state at a time.

:- of_type(D,Dev),
   state_of(S,Dev),
   h(in_state(D,S),T),
   nh(in_state(D,S),T).



%%%%%%%%%%%%   End of Module: Valve Control System   %%%%%%%%%%%%



%%%%%%%%%%%%%%%%%%%%%%%%%      Time     %%%%%%%%%%%%%%%%%%%%%%%%%

%% The time range allowed for computation covers from moment 0 to 
%% the moment defined by constant "lasttime,"  which is provided
%% by the user at run time.
%% next(T,T1) is true iff the next moment of time after T is T1.                            
next(T,T1) :- T1=T+1.   


%% File "plan": Parallelizes plans for different RCSs
%% First Release date: 10/15/00
%%
%% $Name:  $


%% Typical use:
%%   crmodels3 -nmodels 1 -lparseopts "-c lasttime=4" rcs1 cr-plan-minimal4 heuristics problem-base tinst-1 hides


%%%%%%%%%%%%%%%%%%%%%    Planning Module    %%%%%%%%%%%%%%%%%%%%% 
%% 
%% This module establishes the search criteria used by the program 
%% to find a plan, or, sequence of actions that if executed would  
%% achieve the goal situation. 
%%    
%% The following rule expresses one possible search criteria. It 
%% can be read as: "do exactly one action at each moment of time  
%% until the goal is reached." 
%% It also prohibits an action to occur on the last moment of time  
%% because the effects of such action would be lost. Only results  
%% achieved inside the time limits established for the computation  
%% are considered. 

%% action_of(A,R) is true iff action A is performed on switches
%%                and commands from the RCS R system. 

action_of(flip(Sw,S),R) :- of_type_3(Sw,R,v_switch), 
                           state_of(S,v_switch).

action_of(CC,R) :- commands(CC,V,S),
                   of_type_3(V,R,valve).
 %                 % not bad_circuitry(V).
                  
%%action_of(CC,R) :- basic_command(CC,V,S),
%%                   of_type(V,R,valve),
%%                   bad_circuitry(V).                   
                   
%%action_of(CC,R) :- commands(cc(CC,CC1),V,S),
%%                   of_type(V,R,valve),
%%                   type_cc(CC),
%%                   type_cc(CC1),
%%                   CC!=CC1,
%%                   bad_circuitry(V).
                   
%%action_of(CC,R) :- commands(cc(CC1,CC),V,S),
%%                   of_type(V,R,valve),
%%                   type_cc(CC),
%%                   type_cc(CC1),
%%                   CC!=CC1,
%%                   bad_circuitry(V).               
 

%% it is impossible to have no actions at time T
%% and one or more actions at time T+1.
%%
%% NOTE: this constraint improves not only the
%%       quality of plans, but also the performance.
%%
%%:- next(T,T1), action_at(T1), not action_at(T).

%% it is impossible to have concurrent actions in
%% a subsystem.
%%
%%:- time(T), system(R),
%%   action_of(A1,R), action_of(A2,R),
%%   A1!=A2,
%%   occurs(A1,T), occurs(A2,T).

%% 
%%
%%r1(A_x,T_x): occurs(A_x,T_x) +- time(T_x),
%%	     T_x < lasttime,
%%	     system(R),
%%	     action_of(A_x,R),
%%	     not goal(T_x,R),
%%	     not done(R).

%% 
%%
occurs(A,T) :+ action_of(A,R).
:- A1!=A2,
   occurs(A1,T), occurs(A2,T),
   action_of(A1,R),action_of(A2,R).
   
   
 
   
  
:- action_of(A,R),
   occurs(A,T), not involved(R,T).
%%	     ,
%%	     not goal(T_x,SS),
%%	     not done(SS).

%%	     involved(R,T).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%% XFEED %%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% Subsystem R is involved in the maneuver at time T
%% if the goal in that subsystem has not yet been achieved.
%%
involved(R,T) :-
		 not goal(T,R),
		 not done_1(R).



%% aft subsystems
%%
aft_system(left_rcs).
aft_system(right_rcs).


r1: use_xfeed(0) :+.
%%#hide use_xfeed.

involved(AFT1,T) :- aft_system(AFT1), 
		    aft_system(AFT2), AFT2!=AFT1,
		    not goal(T,AFT2),
		    not done_1(AFT2),
		    use_xfeed(0).
			
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%   END OF XFEED
%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%% For Typing Purposes only
type_cc(closea_fi12).
type_cc(closeb_ffi12).
type_cc(closeb_foi12).

type_cc(closea_li12).
type_cc(closeb_lfi12).
type_cc(closeb_loi12).

type_cc(close_lx12).
type_cc(close_lfx12).
type_cc(close_lox12).

type_cc(closea_ri12).
type_cc(closeb_rfi12).
type_cc(closeb_roi12).

type_cc(close_rx12).
type_cc(close_rfx12).
type_cc(close_rox12).

type_cc(opena_ffm5).
type_cc(openb_ffm5).
type_cc(opena_lfm5).
type_cc(openb_lfm5).
type_cc(opena_rfm5).
type_cc(openb_rfm5).

type_cc(closea_ffm5).
type_cc(closeb_ffm5).
type_cc(closea_lfm5).
type_cc(closeb_lfm5).
type_cc(closea_rfm5).
type_cc(closeb_rfm5).

%%%%%%%%%%%%%%%%%%%%%%%%%     Goal     %%%%%%%%%%%%%%%%%%%%%%%%%% 
 
goal_1(0) :- 
        goal(T1,left_rcs),
        goal(T2,right_rcs),
        goal(T3,fwd_rcs).

goal(T,S) :-  done_1(S).

:- not goal_1(0).




%%%%%%%%%%%%%%%%%%%%%%%%%   End: Goal   %%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%%%%   End: Goal   %%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%%%%%%%             Facts                %%%%%%%%%%%%%%%%%

%% of_type(D,R,Dev) is true iff device D from RCS R is of type Dev. 

%% Switches

of_type_3(fha,fwd_rcs,v_switch).      % % fha   = forward helium a 
of_type_3(fhb,fwd_rcs,v_switch).       %% fhb   = forward helium b 
of_type_3(fi12,fwd_rcs,v_switch).      %% fi12  = forward isolation 12 
of_type_3(fi345,fwd_rcs,v_switch).    % % fi345 = forward isolation 345 
of_type_3(fm1,fwd_rcs,v_switch).      % % fm1   = forward manifold 1 
of_type_3(fm2,fwd_rcs,v_switch).      % % fm2   = forward manifold 2 
of_type_3(fm3,fwd_rcs,v_switch).     %  % fm3   = forward manifold 3 
of_type_3(fm4,fwd_rcs,v_switch).     %  % fm4   = forward manifold 4 
of_type_3(fm5,fwd_rcs,v_switch).      % % fm5   = forward manifold 5 


of_type_3(lha,left_rcs,v_switch).    %  % lha   = left helium a 
of_type_3(lhb,left_rcs,v_switch).      %% lhb   = left helium b 
of_type_3(li12,left_rcs,v_switch).    % % li12  = left isolation 12 
of_type_3(li345a,left_rcs,v_switch).  % % li345a= left isolation 345a 
of_type_3(li345b,left_rcs,v_switch).  % % li345b= left isolation 345b 
of_type_3(lm1,left_rcs,v_switch).    %  % lm1   = left manifold 1 
of_type_3(lm2,left_rcs,v_switch).     % % lm2   = left manifold 2 
of_type_3(lm3,left_rcs,v_switch).    %  % lm3   = left manifold 3 
of_type_3(lm4,left_rcs,v_switch).    %  % lm4   = left manifold 4 
of_type_3(lm5,left_rcs,v_switch).    %  % lm5   = left manifold 5 
of_type_3(lx12,left_rcs,v_switch).   %  % lx12  = left cross-feed isolation 12  
of_type_3(lx345,left_rcs,v_switch).   % % lx345 = left cross-feed isolation 345  

of_type_3(rha,right_rcs,v_switch).     %% rha   = right helium a 
of_type_3(rhb,right_rcs,v_switch).    % % rhb   = right helium b 
of_type_3(ri12,right_rcs,v_switch).   % % ri12  = right isolation 12 
of_type_3(ri345a,right_rcs,v_switch). % % ri345a= right isolation 345a 
of_type_3(ri345b,right_rcs,v_switch). % % ri345b= right isolation 345b 
of_type_3(rm1,right_rcs,v_switch).    % % rm1   = right manifold 1 
of_type_3(rm2,right_rcs,v_switch).    % % rm2   = right manifold 2 
of_type_3(rm3,right_rcs,v_switch).     %% rm3   = right manifold 3 
of_type_3(rm4,right_rcs,v_switch).    % % rm4   = right manifold 4 
of_type_3(rm5,right_rcs,v_switch).     %% rm5   = right manifold 5 
of_type_3(rx12,right_rcs,v_switch).   % % rx12  = right cross-feed isolation 12  
of_type_3(rx345,right_rcs,v_switch).  % % rx345 = right cross-feed isolation 345 
 
%% Valves
 
of_type_3(ffha,fwd_rcs,valve). 
of_type_3(foha,fwd_rcs,valve).
of_type_3(ffhb,fwd_rcs,valve).
of_type_3(fohb,fwd_rcs,valve).
of_type_3(ffi12,fwd_rcs,valve).
of_type_3(foi12,fwd_rcs,valve).
of_type_3(ffi345,fwd_rcs,valve).
of_type_3(foi345,fwd_rcs,valve).
of_type_3(ffm1,fwd_rcs,valve).
of_type_3(fom1,fwd_rcs,valve).
of_type_3(ffm2,fwd_rcs,valve).
of_type_3(fom2,fwd_rcs,valve).
of_type_3(ffm3,fwd_rcs,valve).
of_type_3(fom3,fwd_rcs,valve).
of_type_3(ffm4,fwd_rcs,valve).
of_type_3(fom4,fwd_rcs,valve).
of_type_3(ffm5,fwd_rcs,valve).
of_type_3(fom5,fwd_rcs,valve).

of_type_3(lfha,left_rcs,valve). 
of_type_3(loha,left_rcs,valve).
of_type_3(lfhb,left_rcs,valve).
of_type_3(lohb,left_rcs,valve).
of_type_3(lfi12,left_rcs,valve).
of_type_3(loi12,left_rcs,valve).
of_type_3(lfi345a,left_rcs,valve).
of_type_3(loi345a,left_rcs,valve).
of_type_3(lfi345b,left_rcs,valve).
of_type_3(loi345b,left_rcs,valve).
of_type_3(lfm1,left_rcs,valve).
of_type_3(lom1,left_rcs,valve).
of_type_3(lfm2,left_rcs,valve).
of_type_3(lom2,left_rcs,valve).
of_type_3(lfm3,left_rcs,valve).
of_type_3(lom3,left_rcs,valve).
of_type_3(lfm4,left_rcs,valve).
of_type_3(lom4,left_rcs,valve).
of_type_3(lfm5,left_rcs,valve).
of_type_3(lom5,left_rcs,valve).
of_type_3(lfx12,left_rcs,valve).
of_type_3(lox12,left_rcs,valve).
of_type_3(lfx345,left_rcs,valve).
of_type_3(lox345,left_rcs,valve).

of_type_3(rfha,right_rcs,valve). 
of_type_3(roha,right_rcs,valve).
of_type_3(rfhb,right_rcs,valve).
of_type_3(rohb,right_rcs,valve).
of_type_3(rfi12,right_rcs,valve).
of_type_3(roi12,right_rcs,valve).
of_type_3(rfi345a,right_rcs,valve).
of_type_3(roi345a,right_rcs,valve).
of_type_3(rfi345b,right_rcs,valve).
of_type_3(roi345b,right_rcs,valve).
of_type_3(rfm1,right_rcs,valve).
of_type_3(rom1,right_rcs,valve).
of_type_3(rfm2,right_rcs,valve).
of_type_3(rom2,right_rcs,valve).
of_type_3(rfm3,right_rcs,valve).
of_type_3(rom3,right_rcs,valve).
of_type_3(rfm4,right_rcs,valve).
of_type_3(rom4,right_rcs,valve).
of_type_3(rfm5,right_rcs,valve).
of_type_3(rom5,right_rcs,valve).
of_type_3(rfx12,right_rcs,valve).
of_type_3(rox12,right_rcs,valve).
of_type_3(rfx345,right_rcs,valve).
of_type_3(rox345,right_rcs,valve).
of_type_3(V,R,valve) :- dummy_valve_2(V,R).
dummy_valve_2(ffdummy,fwd_rcs).
dummy_valve_2(fodummy,fwd_rcs).
dummy_valve_2(lfdummy,left_rcs).
dummy_valve_2(lodummy,left_rcs).
dummy_valve_2(rfdummy,right_rcs).
dummy_valve_2(rodummy,right_rcs).


%% File: heuristics
%% $Name:  $

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  HEURISTICS  %%%%%%%%%%%%%%%%%%%%%%%%%


%% In order to prune as early as possible any unsuccessful branches  
%% of computation, several heuristics related to the domain are  
%% also added to this module. Heuristics are "rules-of-thumb,"  
%% knowledge about the domain that has been accumulated by  
%% specialists and experts. This knowledge is here expressed in the  
%% form of constraint rules.  
 
%% H1 - Do not repeat actions already performed.  
 
:- action_of(A,R),    
   T1!=T2,  
   occurs(A,T1),  
   occurs(A,T2). 
  
    

%% H3 - Do not do two different types of actions with the same effect.

:- occurs(flip(Sw,S),T),
   controls(Sw,V),
   commands(CC,V,S),
   occurs(CC,T1).



%% H4 - Don't pressurize nodes already pressurized.

:- 
   link(N1,N2,V1),
   link(N1,N2,V2),
   V1!=V2,
   h(in_state(V1,open),T),
   h(in_state(V2,open),T),
   not stuck(V1,open),
   not stuck(V2,open),
   not h(in_state(V1,open),0),
   not h(in_state(V2,open),0).



%% H5 - If circuitry is working properly, do not issue a computer command 
%% to move a valve V to a state S1 at time T1, and later issue a computer
%% command to move V to state S2 opposite to S1.

:- T2>T1,
   commands(CC1,V,S1),
   occurs(CC1,T1),
   opposite_state(S1,S2),
   commands(CC2,V,S2),
   occurs(CC2,T2).

   
 %  % H6 - Do not flip a switch to a state S and later to the opposite state.

:- 
   T2>T1,
   controls(Sw,V), 
   occurs(flip(Sw,S),T1),
   opposite_state(S,S1),
   occurs(flip(Sw,S1),T2).



%% H7 - Do not do move a normally functioning valve V to a state S
%% through a flipping action, and at a different time move V 
%% to an opposite state S' by issuing a computer command.

:-
   T2!=T1,
   commands(CC,V,S1),
   occurs(CC,T1),
   opposite_state(S1,S2),
   controls(Sw,V), 
   occurs(flip(Sw,S2),T2).



%% H8 - If a switch Sw controlling a valve V is in state S1
%% different from the state S of V, do not flip Sw to S.

:- 
   controls(Sw,V1),
   controls(Sw,V2),
   V1!=V2,
   h(in_state(V1,S1),T),
   h(in_state(V2,S1),T),
   state_of(S1,valve),
   occurs(flip(Sw,S1),T).



%% H9 - A pair of valves controlled by the same computer command
%% should not be moved to the state they are already in.

:- 
   occurs(CC,T),
   commands(CC,V1,S),
   commands(CC,V2,S),
   h(in_state(V1,S),T),
   h(in_state(V2,S),T),
   V1!=V2.



    
%% H11 - Do not flip switch Sw to gpc position at any time T unless 
%% a computer command is issued for a valve controlled by Sw  
%% at T+1.  
 
:- next(T,T1), 
   occurs(flip(Sw,gpc),T), 
   controls(Sw,V),
   not issued_commands(V,T1). 

   
 issued_commands(V,T) :- 
                        commands(CC,V,S),  
                        occurs(CC,T). 

 
%% H12 - Do not flip switch Sw to gpc position at time T1 and 
%% perform another action on Sw at time T2 without issuing 
%% a computer command between T1,T2. 
 
:-  
   T2>T1,
   controls(Sw,V),
   state_of(S,v_switch),
   occurs(flip(Sw,gpc),T1), 
   occurs(flip(Sw,S),T2), 
   not command_between(Sw,T1,T2). 
    
 
%% command_between(Sw,T1,T2) is true iff a computer command is  
%%                          issued between times T1 and T2 to  
%%                          move a valve controlled by switch Sw.     
 
command_between(Sw,T1,T2) :-  
                             T1<T,
                             T<T2, 
                             controls(Sw,V), 
                             commands(CC,V,S),   
                             occurs(CC,T). 
 
%% A normally functioning valve connecting nodes N1 and N2 should 
%% not be open if N1 is not pressurized.

%%:- time(T),
%%   link(N1,N2,V),
%%   not has_leak(V),
%%   not stuck(V),
%%   h(in_state(V,open),T),
%%   tank_of(TK,R),
%%   is_pressurized_by(N1,TK),
%%   not h(pressurized_by(N1,TK),T).
 %% Node N belongs to the plumbing line which is pressurized by tank TK.

%%is_pressurized_by(N,TK) :- time(T),
%%                           link(N1,N,V),
%%                           tank_of(TK,R),
%%                           h(pressuuuuuuuurized_by(N,TK),T).


%%--------------------------------
%% Above constraint was modified by Gelfond on 3/7/01 as follows:

%% A normally functioning valve connecting nodes N1 and N2 should 
%% not be open if N1 is not pressurized.

:- link(N1,N2,V),
   h(in_state(V,open),T),
   not h(pressurized(N1),T),
   not has_leak(V),
   not stuck_device(V),
   not h(in_state(V,open),0).	%% Added by Gelfond 04/01/01
   
h(pressurized(N),T) :- tank(TK),
                       h(pressurized_by(N,TK),T).             

tank(TK) :- tank_of(TK,R).
   
%%-------- end modification ----------
              

%% H10 - Do not issue a computer command to a valve controlled by a 
%% switch Sw when Sw is not on state GPC. 
 
:- controls(Sw,V),
   commands(CC,V,S),
   occurs(CC,T), 
   not h(in_state(Sw,gpc),T). 

 
%% H13 - Do not open a valve if a previous valve in the path is not open. 


:- perf(in_state(V,open),T),
   prev(V1,V),
   not dummy_valve(V1), 
   not open_prev(V,T). 

:- perf(in_state(V,open),T),
   dummy_valve(V1),
   prev(V1,V),
   prev(V2,V1),
   V1!=V2,V!=V2,V1!=V,
   not open_prev(V1,T).



 
%% perf(in_state(V,open),T) is true iff an action to open valve V  
%%                          is performed at time T. 
 
perf(in_state(V,open),T) :- controls(Sw,V), 
	                    occurs(flip(Sw,open),T). 
 
perf(in_state(V,open),T) :- commands(CC,V,open),                       
     		                occurs(CC,T).
  

%% prev(V1,V2) is true iff valve V1 is located before valve V2 
%%             in some path. 
 
prev(V1,V2) :- link(N1,N2,V1),  
               link(N2,N3,V2),
			   N1!=N2,N2!=N3,N1!=N3,V1!=V2.


 
%% open_prev(V,T) is true iff there exists a valve previous to  
%%                valve V in a path which is open at time T. 
 
open_prev(V,T) :- 
		  prev(V1,V), 
		  h(in_state(V1,open),T). 
 
%% File: problem-base
%% $Name:  $

%%%%%%%%%%%%%%%%%%%       Problem Module       %%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%     Initial Situation    %%%%%%%%%%%%%%%%%%%% 
%%%%%%%%%%%%%%%%%%%        COMMON PART       %%%%%%%%%%%%%%%%%%%% 



%% Initially, the Helium tanks are pressurized. 
%% h(pressurized_by(X,X),0) is true iff tank X is pressurized by X  
%% at time 0. 
 
h(pressurized_by(ffh,ffh),0). 
h(pressurized_by(foh,foh),0). 
h(pressurized_by(lfh,lfh),0). 
h(pressurized_by(loh,loh),0). 
h(pressurized_by(rfh,rfh),0). 
h(pressurized_by(roh,roh),0). 

 
%% All switches are normally in state GPC initially. 
%% h(in_state(D,S),0) is true iff device D is in state S at time 0. 

h(in_state(Sw,gpc),0) :- of_type(Sw,v_switch), 
                         not nh(in_state(Sw,gpc),0).

 
%% Valves are all normally closed initially. 

h(in_state(V,closed),0) :- of_type(V,valve),
                           not  h(in_state(V,open),0). 
 
%% Except the dummy valves which are always open.

stuck(V,open) :- dummy_valve(V).
%%
%% tinst-1: problem instance for use with cr-plan
%%
%% usage:
%%   crmodels -lparseopts "-c lasttime=3" rcs1 cr-plan-minimal1 heuristics
%%            problem-base tinst-1 hides
%%
%%
%% Sample output:
%%
%% +++Regular program is inconsistent. Trying with CR-Rules...
%% findbest version 0.1. Reading...done
%% Answer: 1 (smodel's Stable Model #137)
%% Stable Model: occurs(flip(fha,open),0) occurs(flip(fi12,open),1) occurs(flip(fi345,open),1) occurs(flip(fm1,open),2) occurs(flip(fm3,open),2) maxtime(3) 
%% Answer: 2 (smodel's Stable Model #541)
%% Stable Model: occurs(flip(fha,open),0) occurs(flip(fi12,open),1) occurs(flip(fi345,open),1) occurs(flip(fm2,open),2) occurs(flip(fm3,open),2) maxtime(3) 
%% Answer: 3 (smodel's Stable Model #8685)
%% Stable Model: occurs(flip(fha,open),0) occurs(flip(fi12,open),1) occurs(flip(fm1,open),2) occurs(flip(fm2,open),2) maxtime(3) 
%% Answer: 4 (smodel's Stable Model #11745)
%% Stable Model: occurs(flip(fhb,open),0) occurs(flip(fi12,open),1) occurs(flip(fi345,open),1) occurs(flip(fm2,open),2) occurs(flip(fm3,open),2) maxtime(3) 
%% Answer: 5 (smodel's Stable Model #14561)
%% Stable Model: occurs(flip(fhb,open),0) occurs(flip(fi12,open),1) occurs(flip(fi345,open),1) occurs(flip(fm1,open),2) occurs(flip(fm3,open),2) maxtime(3) 
%% Answer: 6 (smodel's Stable Model #17784)
%% Stable Model: occurs(flip(fhb,open),0) occurs(flip(fi12,open),1) occurs(flip(fm1,open),2) occurs(flip(fm2,open),2) maxtime(3) 
%% False
%% Duration: 63.513


%%%%%%%%%%      Initial Situation       %%%%%%%%%%
%%%%%%%%%% FAULTS and OTHER EXCEPTIONS  %%%%%%%%%%
%% --no faults-- %
%%%%%%%%%%%%%     GOALS     %%%%%%%%%%%%%



goal(T,fwd_rcs) :- h(maneuver_of(minus_x,fwd_rcs),T).

goal(T,left_rcs) :- h(maneuver_of(minus_x,left_rcs),T).

goal(T,right_rcs) :- h(maneuver_of(minus_x,right_rcs),T).


%display
%-occurs(X,Y).
%#command(X).
