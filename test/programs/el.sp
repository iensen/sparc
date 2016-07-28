
 
% blocksWorld using a direct animation approach

#const n = 4.
#const size = 500. 

sorts
#block = [b][0..1].  % blocks in the world
#co = 1..n. % coordinates of the world
#fluent = on(#block,#co,#co). % where a block is (row,col)
#action = put(#block,#co,#co). % where a block will be
#step = 0..2. % passage of time
#done = {true}. % are you done?

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% drawing stuff

% each frame is equal to 1/60th of a second
#frame = 1..180.

% [row] and [col] specify pixel coordinates
% on the canvas
#row = 1..size.
#col = 1..size.

   % given two coordinate pairs, draw a line
   % from the first to the second with the 
   % styling of the object specified
#line = draw_line(#block,#col,#row,#col,#row).
  
      % [clr] is the color for any object
   #clr = {green,red}.
   % style the color of a specified object
   #color = line_color(#block,#clr).

   % general drawing sort
   #drawing_command = #color+#line.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

predicates
holds(#fluent,#step). % fluent state at a step
occurs(#action,#step). % fluent will change at step
goal(#step). % is the goal met at step?
success(#done). % was there success? I hope so

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% drawing stuff

% drawing command applies at specified frame
animate(#drawing_command,#frame).

% frames for fps logic
frame(#frame).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

rules
holds(on(b0,1,2),0). % initial
holds(on(b1,1,1),0). % configuration

-holds(F,I) :- not holds(F,I). % not sure there at first? 
% probably not there then.

holds(on(B,R,C),I) :-  % if you put it there, it
occurs(put(B,R,C),I-1). % will next be there

-holds(on(B,R,C),I) :- % one block,
holds(on(B,R1,C1),I), % two places?
R != R1. % Nope.
-holds(on(B,R,C),I) :- % row or col
holds(on(B,R1,C1),I),
C != C1.

holds(F,I) :-  % i a
holds(F,I-1),  % n x
not -holds(F,I). % e i
-holds(F,I) :-  % r o
-holds(F,I-1),  % t m
not holds(F,I). % i s
% a !

-occurs(put(B,R1,C1),I) :- % if you try to move a block
holds(on(B,R,C),I), % under a stack, everything is
holds(on(B1,R+1,C),I). % probably going to fall

-occurs(put(B,R,C),I) :- % superposition not
holds(on(B1,R,C),I). % invented yet

-occurs(put(B,R,C),I) :- % nor
-holds(on(B1,R-1,C),I), % has
C > 1, B != B1. % flying

goal(I) :- % gooooooooooooooooaaaaaalllll:
holds(on(b1,2,3),I). % block1 at cooardinate (3,2)

success(true) :- % I heard someone yell goal, so
goal(I). % I guess there has been success

:- not success(true). % I really hope so.

occurs(A,I) | -occurs(A,I) :- % no one shall rest until
not goal(I). % our goal has been achieved

:- occurs(A,I), occurs(A1,I), % but no one shall get tired
A != A1. % with one action maximum

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% drawing stuff

% 60 frames per second
frame(1..60).

% b0 will be green!
animate(line_color(b0,green),F).

% b1 will be red!
animate(line_color(b1,red),F).

  % draw a box where the square is located at every frame
   %%% method: proportionally travel from where holding
   %%% to where it occurs that the box is moving to

% left side
animate(draw_line(B,size/(n+1)*C,size/(n+1)*R,size/(n+1)*C,size/(n+1)*R+size/(n+1)),F+60*I) :- 
holds(on(B,R1,C1),I), occurs(put(B,R2,C2),I), frame(F), R=R1+(R2-R1)/60*F, C=C1+(C2-C1)/60*F.

% top side
animate(draw_line(B,size/(n+1)*C,size/(n+1)*R+size/(n+1),size/(n+1)*C+size/(n+1),size/(n+1)*R+size/(n+1)),I) :- 
holds(on(B,R1,C1),I), occurs(put(B,R2,C2),I), frame(F), R=R1+(R2-R1)/60*F, C=C1+(C2-C1)/60*F.

% right side
animate(draw_line(B,size/(n+1)*C+size/(n+1),size/(n+1)*R+size/(n+1),size/(n+1)*C+size/(n+1),size/(n+1)*R),I) :- 
holds(on(B,R1,C1),I), occurs(put(B,R2,C2),I), frame(F), R=R1+(R2-R1)/60*F, C=C1+(C2-C1)/60*F.

% bottom side
animate(draw_line(B,size/(n+1)*C+size/(n+1),size/(n+1)*R,size/(n+1)*C,size/(n+1)*R),I) :- 
holds(on(B,R1,C1),I), occurs(put(B,R2,C2),I), frame(F), R=R1+(R2-R1)/60*F, C=C1+(C2-C1)/60*F.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
