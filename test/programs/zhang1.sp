sorts
    #num = 1..3.
    #idType = id(#num).
    #sp = startPoint(#num, #num, #num).
    #ep = endPoint(#num, #num, #num).
    #lineType = ccline(#sp, #ep).
    #fluent=on(#num(X), #num(Y)): (X != Y).
predicates
    object(#idType, #lineType).
    holds(#fluent).
rules
    holds(on(X,Y)).
    object(id(1), ccline(startPoint(1,2,2), endPoint(1,2,3))).
    % object(id(1), ccline(startPoint(0,1, 1), endPoint(0,2,2 ))).
display
                #sp.
                % object(id(1), X).
                object.
