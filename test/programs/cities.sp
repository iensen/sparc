sort definitions
city              =             $ lubbock 
|           dallas                     |          austin


	|         houston        |        san_antonio.

predicate declarations



arc     


  (         city, 
city         ).
connected  (city,




city).
blocked(                 city        ,           city).
program rules


arc(lubbock,      dallas).
arc(lubbock,austin).
arc(         dallas,                    austin).
arc(                                dallas,           houston).
arc(   
dallas,                   san_antonio).
arc(            austin,houston).


arc(houston,san_antonio).
arc(X,Y):-arc(Y,                X).
-arc(X,Y):-not arc(X,               Y).
connected(X,                Y):-arc(X,                 Y), -blocked(X,Y).
connected(X,Y):-connected(X,Z), connected(Z,Y).
-connected(X,Y)                     :-

 not connected(X,Y).
blocked(austin,                san_antonio).
blocked(X,Y)         :-             blocked(Y,X).

-blocked(X,                          Y)        :-  not blocked(X,Y),          arc(X,Y).

