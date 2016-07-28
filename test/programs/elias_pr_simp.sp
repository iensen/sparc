
 
% blocksWorld using a direct animation approach

#const n = 4.
#const size = 5. 

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

% bottom side
animate(draw_line(B,size/(n+1)*C+size/(n+1),size/(n+1)*R,size/(n+1)*C,size/(n+1)*R),I).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
