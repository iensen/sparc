%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Template for a SPARC file
%% Author:
%% Description:
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 
sorts
    #people = {f(a,sara), f(b, sara), f(a, bob)}.
predicates
    father(#people, #people).
rules
    father(f(a,bob), f(a,sara)).
    father(f(a, bob), f(b, sara)).
    
display
father(X, f(Z, sara)).
