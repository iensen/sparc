%%%%%%%%%%%%%%
%  ASP Brain model
%  by Richard Watson
%  Texas Tech University
%  Last Update: 5/29/14
%  Clingo version 1.1.6
%
%  Changes:
%    Version 1.1.6
%      Made number of node rely on a constant 'n' rather than hardcoded as 8. 
%    Version 1.1.5
%      Changed pair congruency rule for case 1 for data relaying to match the
%      SAL model.
%    Version 1.1.4
%      Fixed problem in which propogation in case 2 could wrongly
%      forward data from the other direction.
%      Change so that reconstitution acceptance requires that inline
%      acceptance failed. 
%    Version 1.1.3
%      Fixed reconstitution acceptance rules
%      Cleaned up and clarified comments.
%%%%%%%%%%%%%%

% number of nodes
#const n = 8.

%------------------------------------------------------------------------
% Defintion of sorts for SPARC 
sorts

#node = 0..7.     % 0 to N - 1
#direction = {right,left}.
#data = {good} + bad(#node).
#hopc = 0..10.    % max should be N + 2 
#case_num = 1..3.  % case numbers

%------------------------------------------------------------------------
% Predicates to be used and the sorts of their arguements
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

   bad_node(#node).
   omit(#node,#node).
   discordant(#node,#node,#direction,#hopc).
   frame_sent(#node,#node).
   inline_accept(#node,#data,#direction).
   inline_passed(#node).
   recon_accept(#node,#data).
   recon_qualify_hop(#node,#node,#direction).
   recon_case_met(#node,#node,#node).
   accepted(#node,#data).
   bad_accepted().
   good_not_accepted().
   bad().
   hop_add(#node,#node,#hopc).
   adjusted_hop_sum(#node,#node,#node,#hopc).
   case(#case_num,#node,#direction).  

%------------------------------------------------------------------------
% The rules for the program
rules

% VALID NODES and HOP COUNTS
node(N).
v_hop(N).

% DEFINTION OF DISCORDANT
-discordant(N1,N2,right,H) :- 
			N2 > N1,
			N2-N1 == H.
-discordant(N1,N2,right,H) :- 
			N2 < N1,
			N2+n-N1 == H.
-discordant(N1,N2,left,H) :- 
			N2 < N1,
			N1-N2 == H.
-discordant(N1,N2,left,H) :- 
			N2 > N1,
			N1+n-N2 == H.		
			
% DEFINITION OF WHEN NO FRAME WAS SENT
frame_sent(N1,N2) :- sent_to(N1,N2,D).
-frame_sent(N1,N2) :- not frame_sent(N1,N2).

% DEFINITION OF HOP_ADD	
hop_add(N1,N2,1) :- direct_link(N1,N2,D).
hop_add(N1,N2,2) :- skip_link(N1,N2,D).		

% DEFINITION OF ADJUSTED_HOP_SUM
adjusted_hop_sum(N1,N2,N3,Num) :-
		hop_count(N2,N1,H1),
		hop_count(N3,N1,H2),
		hop_add(N2,N1,H3),
		hop_add(N3,N1,H4),
		H1 + H2 + H3 + H4 = Num.
		
% DESCRIPTION OF THE RING

direct_link(N1,N2,right) :- N2 = N1 + 1.

direct_link(n-1,0,right).

skip_link(N1,N2,right) :- N2 = N1 + 2.

skip_link(n-2,0,right).
skip_link(n-1,1,right).

direct_link(N1,N2,left) :- direct_link(N2,N1,right).

skip_link(N1,N2,left) :- skip_link(N2,N1,right).

link(N1,N2,Dir) :- direct_link(N1,N2,Dir).
link(N1,N2,Dir) :- skip_link(N1,N2,Dir).

% NORMAL DATA PROPAGATION

% If good, each sending node will send identical messages to each of its
% four linked. 
% as a simplifying assumption we will choose O and 1 as the sending nodes)
sending(0).
sending(1).  

% if not a bad node, a sending node send its own id in the id field.
id(N1,N2,N1) :-
       sending(N1),
       not bad_node(N1),
       link(N1,N2,Dir).

% if not a bad node, a sending node sends the id of the other sending node as
% the partner field.       
partner(N1,N2,N3) :-
       sending(N1),
       sending(N3),
       N1 != N3,
       not bad_node(N1),
       link(N1,N2,Dir).             

% if not a bad node, the sending node sends good data.
sent_to(N1,N2,good) :-
       sending(N1),
       not bad_node(N1),
       link(N1,N2,Dir).
 
% if not a bad node, the hop count sent by a sending node is 0.      
hop_count(N1,N2,0) :-
       sending(N1),
       not bad_node(N1),
       link(N1,N2,Dir).

% if not a bad node, the sending node sends true for the integrity flag.
integrity(N1,N2) :-
       sending(N1),
       not bad_node(N1),
       link(N1,N2,Dir).  

% if not a bad node, the sending node sends true for the pair congruency flag.       
pair_cong(N1,N2) :-
       sending(N1),
       not bad_node(N1),
       link(N1,N2,Dir).       

% PROPOGATION STEP: (Only apply to good, non-sending nodes)

% Propogation is based on the following 3 cases:
% case 1 - frames arrive on both links (propogation only occurs if 
% hop count is not 1 and is not discordant).
case(1,N1,Dir) :- 
		not sending(N1),
		sent_to(N2,N1,Data1),
		sent_to(N3,N1,Data2),
		skip_link(N2,N1,Dir),
		direct_link(N3,N1,Dir),
		hop_count(N2,N1,H),
		H != 1,
		id(N2,N1,N4),
		-discordant(N4,N1,Dir,H+2).
% case 2 - frame arrives only on direct link (propogation only occurs if 
% hop count is not < 2 and is not discordant).
case(2,N1,Dir) :- 
		not sending(N1),
		sent_to(N2,N1,Data),
		direct_link(N2,N1,Dir),
		skip_link(N3,N1,Dir),
		-frame_sent(N3,N1),
		hop_count(N2,N1,H),
		H >= 2,
		id(N2,N1,N4),
		-discordant(N4,N1,Dir,H+1).		
% case 3 - frame arrives only on skip link (propogation only occurs if 
% hop count is not 1 and is not discordant).
case(3,N1,Dir) :- 
		not sending(N1),
		sent_to(N2,N1,Data),
		skip_link(N2,N1,Dir),
		direct_link(N3,N1,Dir),
		-frame_sent(N3,N1),
		hop_count(N2,N1,H),
		H != 1,
		id(N2,N1,N4),
		-discordant(N4,N1,Dir,H+2).						

% the data relayed in the message is as follows based on which case is met:
% in cases 1 & 3 data received on the skip link is forwarded
sent_to(N2,N3,Data) :-
       sent_to(N1,N2,Data),
       not bad_node(N2),
       case(C,N2,Dir),
       C != 2,
       skip_link(N1,N2,Dir),
       link(N2,N3,Dir).      
% in case 2 the data received on the direct link is forwarded.      
sent_to(N2,N3,Data) :-
       sent_to(N1,N2,Data),
       not bad_node(N2),
       case(2,N2,Dir),
       direct_link(N1,N2,Dir),      
       link(N2,N3,Dir).

% the ID relayed in the message is as follows based on which case is met:      
% in cases 1 & 3 the id recieved on the skip link is forwarded.
id(N2,N3,ID) :-
       id(N1,N2,ID),
       not bad_node(N2),
       case(C,N2,Dir),
       C != 2,
       skip_link(N1,N2,Dir),
       link(N2,N3,Dir).       
% in case 2 the id received on the direct link is forwarded.      
id(N2,N3,ID) :-
       id(N1,N2,ID),
       not bad_node(N2),
       case(2,N2,Dir),
       direct_link(N1,N2,Dir),
       link(N2,N3,Dir).
       
% in cases 1 & 3 the partner received on the skip link is forwarded.
partner(N2,N3,ID) :-
       partner(N1,N2,ID),
       not bad_node(N2),
       case(C,N2,Dir),
       C != 2,
       skip_link(N1,N2,Dir),
       link(N2,N3,Dir).
       
% in case 2 the partner received on the direct link is forwarded.      
partner(N2,N3,ID) :-
       partner(N1,N2,ID),
       not bad_node(N2),
       case(2,N2,Dir),
       direct_link(N1,N2,Dir),
       link(N2,N3,Dir).

% in cases 1 & 3 the hop count recieved on the skip link incremented by 2 and 
% forwarded.
hop_count(N2,N3,Count+2) :-
       hop_count(N1,N2,Count),
       not bad_node(N2),
       case(C,N2,Dir),
       C != 2,
       skip_link(N1,N2,Dir),
       link(N2,N3,Dir).
       
% in case 2 the hop count recieved on the direct link is incremented by 1 and 
% forwarded.      
hop_count(N2,N3,Count+1) :-
       hop_count(N1,N2,Count),
       not bad_node(N2),
       case(2,N2,Dir),
       direct_link(N1,N2,Dir),
       link(N2,N3,Dir).

% in case 1 the pair congruency is forwarded as true if hop counts on both links 
% are 0, data on both links is the same, and integrity is true on both links.
pair_cong(N3,N4) :-
        not bad_node(N3),
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
		
% in case 1 the pair congruency is forwarded as true if the hop count on the 
% skip link is 0, hop count on direct link is non-zero, data on both links is  
% the same, integrity is true on both links, and pair congruency on direct 
% link is true.
pair_cong(N3,N4) :-
        not bad_node(N3),
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
		
% in case 1 pair congruency received on the skip link is forwarded if neither 
% of the hop counts recieved are 0.		
pair_cong(N3,N4) :-
        not bad_node(N3),
		skip_link(N1,N3,Dir),
		direct_link(N2,N3,Dir),
		case(1,N3,Dir),
		hop_count(N1,N3,Num1),
		hop_count(N2,N3,Num2),
		Num1 != 0,
%		Num2 != 0,   % according to SAL model, it is not when both 0 but when neith other case is met.
		pair_cong(N1,N3),
		link(N3,N4,Dir).		

% in case 2 & 3 pair congruency is sent as true if the incoming hop count was 
% not 0 and incoming pair congruency was true.
pair_cong(N2,N3) :-
        not bad_node(N2),
		case(C,N2,Dir),
		C != 1,
		link(N1,N2,Dir),
		hop_count(N1,N2,H),
		H !=0,
		pair_cong(N1,N2),
		link(N2,N3,Dir).
		
% The integrity status is true only if the meassages meet case 1, data on both 
% incoming links matches, and either the hops counts are both 0 or the 
% integrity status is true on both.
integrity(N3,N4) :-
        not bad_node(N3),
		skip_link(N1,N3,Dir),
		direct_link(N2,N3,Dir),
		case(1,N3,Dir),
		hop_count(N1,N3,0),
		hop_count(N2,N3,0),		
		sent_to(N1,N3,Data),
		sent_to(N2,N3,Data),
		link(N3,N4,Dir).	
integrity(N3,N4) :-
        not bad_node(N3),
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
             
% a bad node may send good data or bad data if it is sending (any bad data
% sent is different from any bad data sent by other bad nodes)
sent_to(N1,N2,good) | sent_to(N1,N2,bad(N1)) :- 
       bad_node(N1),
       not omit(N1,N2),
       link(N1,N2,Dir).                    
             
% a bad node may send any id as the origniating sender of the message
1{id(N1,N2,N3):node(N3)}1 :- 
       bad_node(N1),
       not omit(N1,N2),
       link(N1,N2,Dir).

% a bad node may send any partner id out in the message
1{partner(N1,N2,N3):node(N3)}1 :- 
       bad_node(N1),
       not omit(N1,N2),
       link(N1,N2,Dir).    

% a bad node may send any hop count out as part of the message
1{hop_count(N1,N2,H):v_hop(H)}1 :- 
       bad_node(N1),
       not omit(N1,N2),
       link(N1,N2,Dir).   
 
% a bad node may have integrity set to true or false
0{integrity(N1,N2)}1 :-
       bad_node(N1),
       link(N1,N2,Dir).   
       
% a bad node may have pair congruency set to true or false
0{pair_cong(N1,N2)}1 :-
       bad_node(N1),
       link(N1,N2,Dir).               

% INLINE DATA ACCEPTANCE TEST
% The Inline Data Acceptance Test is passed if data arriving on both links
% from the same directon is the same, the integrity status on both messages
% is set to true, and the id sent on each messge matches the partner of the
% other message.
inline_accept(N3,Data,Dir) :- 
		not sending(N3),       % verify this is correct
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
		
% Inline pass flag is true for a node if the node inline accepted something. 
inline_passed(N) :- inline_accept(N,Data,Dir).
		
% RECON DATA ACCEPTANCE TESTS
% A hop count sent in a message qualifies for reconstitution acceptance if
% it arrives on a direct link, is not less than 2, and is not discordant with 
% the expected hop count.
recon_qualify_hop(N1,N2,Dir) :-
		direct_link(N1,N2,Dir),
		id(N1,N2,N3),
		hop_count(N1,N2,H),
		H >= 2,
		-discordant(N3,N2,Dir,H+1). 
% A hop count sent in a message qualifies for reconstitution acceptance if
% it arrives on a skip link, is not equal to 1, and is not discordant with 
% the expected hop count.
recon_qualify_hop(N1,N2,Dir) :-
		skip_link(N1,N2,Dir),
		id(N1,N2,N3),
		hop_count(N1,N2,H),
		H != 1,
		-discordant(N3,N2,Dir,H+2). 

% passes recon accept test if 2 messages from opposite directions have same 
% data, the hop counts of the messages meet the requirements, and one of the
% 3 reconstitution cases are met.
recon_accept(N1,Data) :-		
		not sending(N1),       % verify this is correct
		not inline_passed(N1),  
		sent_to(N2,N1,Data),
		sent_to(N3,N1,Data),
		N2 != N3,
		recon_qualify_hop(N2,N1,Dir1),
		recon_qualify_hop(N3,N1,Dir2),
		Dir1 != Dir2,
		recon_case_met(N1,N2,N3).			

% There are 3 cases when qualified recon acceptances are met:
%   1) The Id of one message matches the partner of the other and 
%	vice-versa and the adjusted hop count sum is n-1.
recon_case_met(N1,N2,N3) :-
		id(N2,N1,N4),
		id(N3,N1,N5),
		partner(N2,N1,N5),
		partner(N3,N1,N4),
		adjusted_hop_sum(N1,N2,N3,n-1).	
%   2) The Id fields are the same, the pair congruency flag is set on one of 
%	messages, that message has a hop count > 1, and the adjusted hop count 
%   sum is n.		
recon_case_met(N1,N2,N3) :-
		id(N2,N1,N),
		id(N3,N1,N),
		pair_cong(N2,N1),
		hop_count(N2,N1,H),
		H > 1,
		adjusted_hop_sum(N1,N2,N3,n).
%   3) The Id of one message matches the partner of the other, the pair 
%	congruency flag is set on one of messages, that message has a 
%	hop count > 1, and the adjusted hop count sum is n + 1.				
recon_case_met(N1,N2,N3) :-
		id(N2,N1,N4),
		id(N3,N1,N5),
		partner(N2,N1,N5),
		partner(N3,N1,N4),
		pair_cong(N2,N1),
		hop_count(N2,N1,H),
		H >1,
		adjusted_hop_sum(N1,N2,N3,n+1).					

% NODE ACCEPTANCE CRITERIA 		
		
% A node accepted data if it passes either the inline or reconstitution 
% acceptance test		
accepted(N,Data) :-	inline_accept(N,Data,Dir).
accepted(N,Data) :- recon_accept(N,Data).

% BAD CASES TO BE CHECKED

% bad_accepted is true iff a node accepted data that wasn't good data
bad_accepted :- 
		not sending(N),
		accepted(N,Data), 
		Data != good.

% good_not_accepted is true iff a node does not have good data accepted
good_not_accepted :- 
		not sending(N),
		not accepted(N,good).

% The system is bad if either bad data was accepted by a node or if a node
% did not accept any data.
bad :- bad_accepted.
bad :- good_not_accepted, sending(N1), sending(N2), N1!=N2, not bad_node(N1), not bad_node(N2).

% -bad_node(N) :- sending(N).
0{bad_node(N)}2.

%:- not bad.


