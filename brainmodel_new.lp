%%%%%%%%%%%%%%
%  ASP Brain model
%  by Richard Watson
%  Texas TEch University
%  Last Update: 4/18/14
%  version 1.0
%%%%%%%%%%%%%%

sorts

#node = 0..7.     % 0 to N - 1
#direction = {right,left}.
#data = {good} + bad(#node).
#hopc = 0..10.    % max should be N + 2 
#case_num = 1..3.  % case numbers

predicates

   node(#node).
   v_hop(#hopc).

   sending(#node).
   direct_link(#node,#node,#direction).
   skip_link(#node,#node,#direction).
   link(#node,#node,#direction).
   
   id(#node,#node,#node).
   partner(#node,#node,#node).
   sent_to(#node,#node,#data).
   integrity(#node,#node).
   hop_count(#node,#node,#hopc).
   pair_cong(#node,#node).
%   present(#node,#node).

   bad_node(#node).
   omit(#node,#node).
   discordant(#node,#node,#direction,#hopc).
   frame_sent(#node,#node).
   inline_accept(#node,#data,#direction).
   recon_accept(#node,#data).
   accepted(#node,#data).
   bad_accepted().
   good_not_accepted().
   hop_add(#node,#node,#hopc).
   case(#case_num,#node,#direction).
   

rules

% VALID NODES and HOP COUNTS
node(N).
v_hop(N).

% DEFINTION OF DISCORDANT
-discordant(N1,N2,right,H) :- N2 > N1,
			N2-N1 == H.
-discordant(N1,N2,right,H) :- N2 < N1,
			N2+8-N1 == H.
-discordant(N1,N2,left,H) :- N2 < N1,
			N1-N2 == H.
-discordant(N1,N2,left,H) :- N2 > N1,
			N1+8-N2 == H.		
			
% DEFINITION OF WHEN NO FRAME WAS SENT
frame_sent(N1,N2) :- sent_to(N1,N2,D).
-frame_sent(N1,N2) :- not frame_sent(N1,N2).

% DEFINITION OF HOP_ADD	
hop_add(N1,N2,1) :- direct_link(N1,N2,D).
hop_add(N1,N2,2) :- skip_link(N1,N2,D).		

% DESCRIPTION OF THE RING

direct_link(N1,N2,right) :-
            N2 = N1 + 1.

direct_link(7,0,right).

skip_link(N1,N2,right) :-
            N2 = N1 + 2.

skip_link(6,0,right).
skip_link(7,1,right).


direct_link(N1,N2,left) :-
            direct_link(N2,N1,right).

skip_link(N1,N2,left) :-
            skip_link(N2,N1,right).

link(N1,N2,Dir) :-
  direct_link(N1,N2,Dir).
link(N1,N2,Dir) :-
  skip_link(N1,N2,Dir).

% NORMAL DATA PROPAGATION

% The base:

sending(0).
sending(1).  

id(N1,N2,N1) :-
       sending(N1),
       not bad_node(N1),
       link(N1,N2,Dir).
       
partner(N1,N2,N3) :-
       sending(N1),
       sending(N3),
       N1 != N3,
       not bad_node(N1),
       link(N1,N2,Dir).             

sent_to(N1,N2,good) :-
       sending(N1),
       not bad_node(N1),
       link(N1,N2,Dir).
       
hop_count(N1,N2,0) :-
       sending(N1),
       not bad_node(N1),
       link(N1,N2,Dir).

integrity(N1,N2) :-
       sending(N1),
       not bad_node(N1),
       link(N1,N2,Dir).  
       
pair_cong(N1,N2) :-
       sending(N1),
       not bad_node(N1),
       link(N1,N2,Dir).       

% PROPOGATION STEP: (Only apply to non-sending nodes)

% case 1 - frames arrive on both links
case(1,N1,Dir) :- not sending(N1),
		sent_to(N2,N1,Data),
		sent_to(N3,N1,Data),
		skip_link(N2,N1,Dir),
		direct_link(N3,N1,Dir),
		hop_count(N2,N1,H),
		H != 1,
		id(N2,N1,N4),
		-discordant(N4,N2,Dir,H).

% case 2 - frame arrives only on direct link
case(2,N1,Dir) :- not sending(N1),
		sent_to(N2,N1,Data),
		direct_link(N2,N1,Dir),
		skip_link(N3,N1,Dir),
		-frame_sent(N3,N1),
		hop_count(N2,N1,H),
		H < 2,
		id(N2,N1,N4),
		-discordant(N4,N2,Dir,H).	
		
% case 3 - frame arrives only on direct link
case(3,N1,Dir) :- not sending(N1),
		sent_to(N2,N1,Data),
		skip_link(N2,N1,Dir),
		direct_link(N3,N1,Dir),
		-frame_sent(N3,N1),
		hop_count(N2,N1,H),
		H != 1,
		id(N2,N1,N4),
		-discordant(N4,N2,Dir,H).						

% in cases 1 & 3 data recieved on skip link is forwarded
sent_to(N2,N3,Data) :-
       sent_to(N1,N2,Data),
       not bad_node(N2),
       case(C,N2,Dir),
       C != 2,
       skip_link(N1,N2,Dir),
       link(N2,N3,Dir).
       
% in case 2 data on direct link is forwarded      
sent_to(N2,N3,Data) :-
       sent_to(N1,N2,Data),
       not bad_node(N2),
       case(2,N2,Dir),
       link(N2,N3,Dir).
       
% in cases 1 & 3 id recieved on skip link is forwarded
id(N2,N3,ID) :-
       id(N1,N2,ID),
       not bad_node(N2),
       case(C,N2,Dir),
       C != 2,
       skip_link(N1,N2,Dir),
       link(N2,N3,Dir).
       
% in case 2 id on direct link is forwarded      
id(N2,N3,ID) :-
       id(N1,N2,ID),
       not bad_node(N2),
       case(2,N2,Dir),
       link(N2,N3,Dir).
       
% in cases 1 & 3 partner recieved on skip link is forwarded
partner(N2,N3,ID) :-
       partner(N1,N2,ID),
       not bad_node(N2),
       case(C,N2,Dir),
       C != 2,
       skip_link(N1,N2,Dir),
       link(N2,N3,Dir).
       
% in case 2 partner on direct link is forwarded      
partner(N2,N3,ID) :-
       partner(N1,N2,ID),
       not bad_node(N2),
       case(2,N2,Dir),
       link(N2,N3,Dir).

% in cases 1 & 3 hop count recieved on skip link incremented by 2 and forwarded
hop_count(N2,N3,Count+2) :-
       hop_count(N1,N2,Count),
       not bad_node(N2),
       case(C,N2,Dir),
       C != 2,
       skip_link(N1,N2,Dir),
       link(N2,N3,Dir).
       
% in case 2 hop count recieved on direct link is incremented by 1 and forwarded      
hop_count(N2,N3,Count+1) :-
       hop_count(N1,N2,Count),
       not bad_node(N2),
       case(2,N2,Dir),
       link(N2,N3,Dir).

% in case 1 pair congruency is forwarded as true if hop counts on both links are 0,
% data on both links is the same, and integrity is true on both links.
pair_cong(N3,N4) :-
		skip_link(N1,N3,Dir),
		direct_link(N2,N3,Dir),
		case(1,N3,Dir),
		hop_count(N1,N3,0),
		hop_count(N2,N3,0),		
		sent_to(N1,N3,Data),
		sent_to(N2,N3,Data),
		integrity(N1,N3),
		integrity(N2,N3),
		link(N3,N4,Dir).
		
% in case 1 pair congruency is forwarded as true if hop counts on skip link is 0,
% hop count on direct link is non-zero, data on both links is the same, 
% integrity is true on both links, and pair congruency on direct link is true.
pair_cong(N3,N4) :-
		skip_link(N1,N3,Dir),
		direct_link(N2,N3,Dir),
		case(1,N3,Dir),
		hop_count(N1,N3,0),
		hop_count(N2,N3,Num),
		Num != 0,		
		sent_to(N1,N3,Data),
		sent_to(N2,N3,Data),
		integrity(N1,N3),
		integrity(N2,N3),
		pair_cong(N2,N3),
		link(N3,N4,Dir).
		
% in case 1 pair congruency on skip link is forwarded if both hop counts are not 0.		
pair_cong(N3,N4) :-
		skip_link(N1,N3,Dir),
		direct_link(N2,N3,Dir),
		case(1,N3,Dir),
		hop_count(N1,N3,Num1),
		hop_count(N2,N3,Num2),
		Num1 != 0,
		Num2 != 0,
		pair_cong(N1,N3),
		link(N3,N4,Dir).		

% in case 2 & 3 pair congruency is sent as true only if incoming pair conruency was true.
pair_cong(N2,N3) :-
		case(C,N3,Dir),
		C != 1,
		link(N1,N2,Dir),
		pair_cong(N1,N2).
		
% The integrity status is true only if case 1, data on both incoming links matches,
% and either hops counts are both 0 or itegrity status is true on both.
integrity(N3,N4) :-
		skip_link(N1,N3,Dir),
		direct_link(N2,N3,Dir),
		case(1,N3,Dir),
		hop_count(N1,N3,0),
		hop_count(N2,N3,0),		
		sent_to(N1,N3,Data),
		sent_to(N2,N3,Data),
		link(N3,N4,Dir).	
integrity(N3,N4) :-
		skip_link(N1,N3,Dir),
		direct_link(N2,N3,Dir),
		case(1,N3,Dir),
		integrity(N1,N3),
		integrity(N2,N3),		
		sent_to(N1,N3,Data),
		sent_to(N2,N3,Data),
		link(N3,N4,Dir).							
		
% BAD NODE DATA PROPOGATION:

% a bad node may omit sending
0{omit(N1,N2)}1 :-
       bad_node(N1),
       link(N1,N2,Dir).
             
% a bad node may send any id out
1{id(N1,N2,N3):node(N3)}1 :- 
       bad_node(N1),
       not omit(N1,N2),
       link(N1,N2,Dir).

% a bad node may send any partner id out
1{partner(N1,N2,N3):node(N3)}1 :- 
       bad_node(N1),
       not omit(N1,N2),
       link(N1,N2,Dir).    

% a bad node may send any hop count out
1{hop_count(N1,N2,H):v_hop(H)}1 :- 
       bad_node(N1),
       not omit(N1,N2),
       link(N1,N2,Dir).   
 
% a bad node may have integrity as true or false
0{integrity(N1,N2)}1 :-
       bad_node(N1),
       link(N1,N2,Dir).   
       
% a bad node may have pair congruency as true or false
0{pair_cong(N1,N2)}1 :-
       bad_node(N1),
       link(N1,N2,Dir).               


% INLINE DATA ACCEPTANCE TEST
inline_accept(N3,Data,Dir) :- 
		skip_link(N1,N3,Dir),
		direct_link(N2,N3,Dir),
		integrity(N1,N3),
		integrity(N2,N3),		
		sent_to(N1,N3,Data),
		sent_to(N2,N3,Data),
		id(N1,N3,N4),
		partner(N2,N3,N4),
		id(N2,N3,N5),
		partner(N1,N3,N5).
		
% RECON DATA ACCEPTANCE TESTS
% passes recon accept test if 2 messages have same data, id of each message 
% matches the partner of the other, and the adjusted hop counts sum to N 
recon_accept(N1,Data) :-		
		sent_to(N2,N1,Data),
		sent_to(N3,N1,Data),
		N2 != N3,
		id(N2,N1,N4),
		partner(N3,N1,N4),
		id(N3,N1,N5),
		partner(N2,N1,N5),
		hop_add(N2,N1,H1),
		hop_add(N3,N1,H2),
		hop_count(N2,N1,H3),
		hop_count(N3,N1,H4),
		H1 + H2 + H3 + H4 = 8.

% passes recon accept test if 2 messages have same data, pair congruency of 
% at least one message is true and that node has a hopcount > 1, 
% the adjusted hop counts sum to N, nodes and partners are identical. 		
recon_accept(N1,Data) :-		
		sent_to(N2,N1,Data),
		sent_to(N3,N1,Data),
		N2 != N3,
		id(N2,N1,N4),
		id(N3,N1,N4),
		partner(N2,N1,N5),
		partner(N3,N1,N5),
        pair_cong(N2,N1),
		hop_add(N2,N1,H1),
		hop_add(N3,N1,H2),
		hop_count(N2,N1,H3),
		hop_count(N3,N1,H4),
		H3 > 1,
		H1 + H2 + H3 + H4 = 8.		
		
% A node accpeted data if it was either inline accepted or recon accepted		
accepted(N,Data) :-	inline_accept(N,Data,Dir).
accepted(N,Data) :- recon_accept(N,Data).

% bad_accepted is true iff a node accepted data that wasn't good data
bad_accepted :- accepted(N,Data), Data != good.

% good_not_accepted is true iff a node does not have good data accepted
good_not_accepted :- not accepted(N,good).
