%predicate with the same name defined twice.
sorts
#s={f(a)}.
#s2={f(a)}+{a}.
predicates
p(#s2).
p(#s2,#s2).
rules
