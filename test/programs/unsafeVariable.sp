sort definitions
num=1..8.
alpha=$[a-z].
position=board(num,alpha).

predicate declarations
free_pos(position).
busy_pos(position).

program rules
free_pos(board(Y,a)):- not busy_pos(board(Y,a)),Y=1. 
