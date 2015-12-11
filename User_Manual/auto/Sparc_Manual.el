(TeX-add-style-hook
 "Sparc_Manual"
 (lambda ()
   (TeX-add-to-alist 'LaTeX-provided-class-options
                     '(("article" "12pt" "letterpaper")))
   (TeX-add-to-alist 'LaTeX-provided-package-options
                     '(("url" "hyphens") ("ulem" "normalem") ("hyperref" "breaklinks")))
   (TeX-run-style-hooks
    "latex2e"
    "article"
    "art12"
    "url"
    "amssymb"
    "graphicx"
    "amsmath"
    "xcolor"
    "ulem"
    "fontenc"
    "footnote"
    "hyperref"
    "palatino"
    "multicol"
    "listings")
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
    "fig:createfile"
    "fig:openqueries"
    "fig:runquery"
    "fig:ansshow"
    "fig:ansset"
    "fig:ansres"
    "fig:addswipl"
    "fig:addprop")
   (LaTeX-add-environments
    "definition"
    "collorary"
    "proposition"
    "invariant"
    "property"
    "claim"
    "example")
   (LaTeX-add-bibliographies
    "mybib")))

