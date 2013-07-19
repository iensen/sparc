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


