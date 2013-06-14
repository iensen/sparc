-in(V2_G,V_G):-in(V1_G,V_G),V1_G!=V2_G,vertex(V_G),vertex(V1_G),vertex(V2_G).
-in(V_G,V2_G):-in(V_G,V1_G),V1_G!=V2_G,vertex(V_G),vertex(V1_G),vertex(V2_G).
reached(V2_G):-init(V1_G),in(V1_G,V2_G),vertex(V1_G),vertex(V2_G).
reached(V2_G):-reached(V1_G),in(V1_G,V2_G),vertex(V2_G),vertex(V1_G).
-reached(V_G):-not  reached(V_G),vertex(V_G).
:--reached(V_G),vertex(V_G).
in(X_G,Y_G) | -in(X_G,Y_G):-edge(X_G,Y_G),vertex(X_G),vertex(Y_G).
init(1).
edge(1,2).
edge(2,3).
edge(3,4).
edge(4,5).
edge(5,6).
edge(6,1).
vertex(3).
vertex(2).
vertex(1).
vertex(6).
vertex(5).
vertex(4).
