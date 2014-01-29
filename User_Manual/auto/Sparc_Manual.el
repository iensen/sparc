(TeX-add-style-hook "Sparc_Manual"
 (lambda ()
    (LaTeX-add-bibliographies
     "mybib")
    (LaTeX-add-environments
     "definition"
     "collorary"
     "proposition"
     "invariant"
     "property"
     "claim"
     "example")
    (LaTeX-add-labels
     "marker"
     "fig:dlv_solver_check"
     "fig:clingo_solver_check"
     "option"
     "ss"
     "type_warnings"
     "asp_type_warnings"
     "clp_type_warnings"
     "fig:plug_install"
     "fig:sparc_file")
    (TeX-add-symbols
     '("blk" 2)
     '("ee" 1)
     '("ie" 1)
     '("ft" 1)
     '("fe" 1)
     '("pg" 1)
     '("set" 1)
     '("otherquestions" 1)
     '("future" 1)
     '("exercise" 1)
     '("hide" 1)
     "emptyclause"
     "lplus"
     "eoa"
     "st")
    (TeX-run-style-hooks
     "listings"
     "multicol"
     "palatino"
     "hyperref"
     "breaklinks"
     "footnote"
     "fontenc"
     "ulem"
     "normalem"
     "amsmath"
     "graphicx"
     "amssymb"
     ""
     "url"
     "hyphens"
     "latex2e"
     "art12"
     "article"
     "letterpaper"
     "12pt")))

