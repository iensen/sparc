#maxint=1000.
holds(on(b0,t),0).
holds(on(b3,b0),0).
holds(on(b2,b3),0).
holds(on(b1,t),0).
holds(on(b4,b1),0).
holds(on(b5,t),0).
holds(on(b6,b5),0).
holds(on(b7,b6),0).
-holds(on(B_G,L_G),0):-not  holds(on(B_G,L_G),0),fluent(on(B_G,L_G)).
holds(on(B_G,L_G),VAR_0):-occurs(put(B_G,L_G),I_G),VAR_0=I_G+1,action(put(B_G,L_G)),step(VAR_0),step(I_G),fluent(on(B_G,L_G)).
-holds(on(B_G,L2_G),I_G):-holds(on(B_G,L1_G),I_G),L1_G!=L2_G,step(I_G),fluent(on(B_G,L1_G)),fluent(on(B_G,L2_G)).
-holds(on(B2_G,B_G),I_G):-holds(on(B1_G,B_G),I_G),B1_G!=B2_G,block(B_G),fluent(on(B1_G,B_G)),step(I_G),block(B_G),fluent(on(B2_G,B_G)).
holds(F_G,VAR_0):-holds(F_G,I_G),not  -holds(F_G,VAR_0),VAR_0=I_G+1,fluent(F_G),step(VAR_0),step(I_G).
-holds(F_G,VAR_0):--holds(F_G,I_G),not  holds(F_G,VAR_0),VAR_0=I_G+1,step(VAR_0),fluent(F_G),step(I_G).
-occurs(put(B_G,L_G),I_G):-holds(on(B1_G,B_G),I_G),fluent(on(B1_G,B_G)),step(I_G),action(put(B_G,L_G)).
-occurs(put(B1_G,B_G),I_G):-holds(on(B2_G,B_G),I_G),block(B_G),step(I_G),action(put(B1_G,B_G)),block(B_G),fluent(on(B2_G,B_G)).
occurs(put(b2,t),0).
occurs(put(b7,b2),1).
fluent(on(b4,t)).
fluent(on(b3,b1)).
fluent(on(b2,b7)).
fluent(on(b1,b0)).
fluent(on(b3,b5)).
fluent(on(b5,t)).
fluent(on(b6,b5)).
fluent(on(b0,b1)).
fluent(on(b3,t)).
fluent(on(b7,b2)).
fluent(on(b7,b6)).
fluent(on(b2,b3)).
fluent(on(b4,b6)).
fluent(on(b5,b7)).
fluent(on(b1,b4)).
fluent(on(b0,t)).
fluent(on(b0,b5)).
fluent(on(b5,b3)).
fluent(on(b2,t)).
fluent(on(b4,b2)).
fluent(on(b2,b0)).
fluent(on(b6,b0)).
fluent(on(b7,b5)).
fluent(on(b3,b6)).
fluent(on(b0,b2)).
fluent(on(b6,t)).
fluent(on(b0,b7)).
fluent(on(b1,b3)).
fluent(on(b7,b1)).
fluent(on(b3,b2)).
fluent(on(b2,b4)).
fluent(on(b4,b5)).
fluent(on(b5,b0)).
fluent(on(b5,b4)).
fluent(on(b1,b7)).
fluent(on(b4,b1)).
fluent(on(b0,b6)).
fluent(on(b1,b2)).
fluent(on(b7,b4)).
fluent(on(b6,b7)).
fluent(on(b1,b6)).
fluent(on(b7,b0)).
fluent(on(b6,b3)).
fluent(on(b2,b5)).
fluent(on(b1,t)).
fluent(on(b5,b1)).
fluent(on(b4,b0)).
fluent(on(b0,b3)).
fluent(on(b6,b2)).
fluent(on(b7,t)).
fluent(on(b3,b4)).
fluent(on(b4,b7)).
fluent(on(b3,b0)).
fluent(on(b7,b3)).
fluent(on(b1,b5)).
fluent(on(b4,b3)).
fluent(on(b2,b6)).
fluent(on(b6,b4)).
fluent(on(b5,b6)).
fluent(on(b2,b1)).
fluent(on(b5,b2)).
fluent(on(b3,b7)).
fluent(on(b6,b1)).
fluent(on(b0,b4)).
action(put(b3,b5)).
action(put(b5,b6)).
action(put(b3,b1)).
action(put(b5,b2)).
action(put(b3,t)).
action(put(b1,b5)).
action(put(b0,b3)).
action(put(b4,b7)).
action(put(b2,b5)).
action(put(b2,b1)).
action(put(b0,b6)).
action(put(b4,b0)).
action(put(b6,b2)).
action(put(b7,b6)).
action(put(b7,b2)).
action(put(b0,b4)).
action(put(b5,b1)).
action(put(b3,b6)).
action(put(b1,b6)).
action(put(b3,b2)).
action(put(b1,b2)).
action(put(b4,t)).
action(put(b2,b6)).
action(put(b6,b5)).
action(put(b7,b5)).
action(put(b0,b5)).
action(put(b6,b1)).
action(put(b7,b1)).
action(put(b4,b3)).
action(put(b1,b7)).
action(put(b3,b7)).
action(put(b0,b1)).
action(put(b4,b5)).
action(put(b2,b7)).
action(put(b1,b3)).
action(put(b5,b4)).
action(put(b6,b0)).
action(put(b6,t)).
action(put(b7,t)).
action(put(b1,t)).
action(put(b2,b3)).
action(put(b4,b2)).
action(put(b7,b4)).
action(put(b5,b0)).
action(put(b7,b0)).
action(put(b0,t)).
action(put(b6,b4)).
action(put(b3,b0)).
action(put(b5,b3)).
action(put(b0,b2)).
action(put(b4,b6)).
action(put(b5,b7)).
action(put(b6,b7)).
action(put(b1,b4)).
action(put(b0,b7)).
action(put(b3,b4)).
action(put(b2,b0)).
action(put(b2,t)).
action(put(b1,b0)).
action(put(b4,b1)).
action(put(b2,b4)).
action(put(b5,t)).
action(put(b7,b3)).
action(put(b6,b3)).
step(2).
step(1).
step(0).
block(b0).
block(b1).
block(b3).
block(b2).
block(b5).
block(b4).
block(b7).
block(b6).
