%% s_uncaring.sp -- SPARC version of Uncaring John from
%%   Chapter 5, Section 5.1
%% Last Modified: 2/14/14
%% Example of a default and its exceptions.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
sorts

#person = {john, sam, alice, pit, kathy, jim}.
#default = d_cares(#person, #person).
#country = {moldova, u}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
predicates

father(#person,#person).
mother(#person,#person).
parent(#person,#person).
child(#person,#person).
cares(#person,#person).
ab(#default).
born_in(#person,#country).
absent(#person).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
rules 

father(john,sam). 
mother(alice,sam). 

father(pit,jim).
mother(kathy,jim).

parent(X,Y) :- father(X,Y). 
parent(X,Y) :- mother(X,Y). 

child(X,Y) :-  parent(Y,X). 

% John doesn't care about his children:
-cares(john,X) :- child(X,john). 

% default rule -- Normally parents care about their children:
cares(X,Y) :- parent(X,Y),
              not ab(d_cares(X,Y)),
              not -cares(X,Y).
              
% Parents who are never seen at school cannot be assumed to
% care about their children; i.e., we cannot assume that 
% default d_cares(P,C) holds if P may have been always absent.
% (Note that does not mean that the parents don't care.
% It just means that we cannot assume that they do.)
ab(d_cares(P,C)) :- not -absent(P).
                    
% Parents who are born in country u do not care about their kids.
-cares(P,X) :- parent(P,X),
               born_in(P,u).
               
% We cannot assume that P cares about C if he may have been
% born in country u.
ab(d_cares(P,X)) :- not -born_in(P,u). 
                  

born_in(kathy,moldova).
%% A person can only be born in one country
-born_in(P,X) :- born_in(P,Y),
                 X != Y.      
                 
-absent(pit).
-absent(kathy). 
