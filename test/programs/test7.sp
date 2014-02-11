sorts
#day={d1,d2}.
#default=d(#day).

predicates
day(#day).
schoolday(#day).
open_school(#day).
snowday(#day).
ab(#default).

rules
schoolday(D):-day(D),not -schoolday(D).

open_school(D):-day(D),not ab(d(D)),not -open_school(D).
-open_school(D):-day(D),snowday(D).
-open_school(D):-day(D),not ab(d(D)).
-snowday(D):-open_school(D).
day(D).
snowday(d1).

ab(d(D)):-schoolday(D), not -snowday(D).

%not schoolday(D).
