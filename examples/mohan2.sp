




preferred(d1(T_G),d3(T_G)):-preferred(d1(T_G),d2(T_G)),preferred(d2(T_G),d3(T_G)),textbook(T_G).
appl(r_0(T_G))|-appl(r_0(T_G)):-textbook(T_G).
appl(r_1(T_G))|-appl(r_1(T_G)):-textbook(T_G).
:-appl(r_2(T_G)),textbook(T_G).
preferred(d1(text0),d2(text0)).
preferred(d2(text0),d3(text0)).
textbook(text0).

