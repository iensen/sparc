package parser;

/**
 * Visitor removes choices from regular expression. Choice example :[a-z]
 * 
 */
public class RChoiceRemover implements SparcTranslatorVisitor {
	@Override
	public Object visit(SimpleNode node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTprogram node, Object data) {
		// TODO Auto-generated method stub

		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTsortDefinitions node, Object data) {
		// TODO Auto-generated method stub

		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTsortDefinition node, Object data) {

		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTsortExpression node, Object data) {
		// TODO Auto-generated method stub

		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTadditiveSortExpression node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTmultiplicativeSortExpression node, Object data) {
		// TODO Auto-generated method stub

		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTregularExpression node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTcomplexRegularExpressionChoices node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTcomplexRegularExpressionRepeatableUnit node,
			Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTcomplexRegularExpressionUnit node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTcomplexRegularExpressionUnitIdentifier node,
			Object data) {

		return null;
		// TODO Auto-generated method stub
	}

	@Override
	public Object visit(ASTcharacterList node, Object data) {
		// TODO Auto-generated method stub
		node.split();
		return null;
	}

	@Override
	public Object visit(ASTunarySortExpression node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTsortExpressionList node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTpredicateDeclarations node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTpredicateDeclaration node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTidentifierList node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTprogramRules node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTprogramRule node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTunlabeledProgramRule node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTunlabeledProgramCrRule node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASThead node, Object data) {
		// TODO Auto-generated method stub
		// System.out.println(node.)
		return null;
	}

	@Override
	public Object visit(ASTbody node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTnonRelAtom node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTextendedNonRelAtom node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTarithmeticTerm node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTatomicArithmeticTerm node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTadditiveArithmeticTerm node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTmultiplicativeArithmeticTerm node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTsymbolicFunction node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTsymbolicConstant node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTsymbolicTerm node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTterm node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTtermList node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTpredSymbol node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTatom node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTatomSequence node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTcomplexRegularExpression node, Object data) {
		// TODO Auto-generated method stub
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTrepeatition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTdisjunction node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTchoice_rule node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTchoice_elements node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTchoice_element node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTaggregate node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTaggregateElements node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTaggregateElement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTaggregateFunction node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTcharacterRegularExpression node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTcondition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTextendedSimpleAtomList node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTsimpleAtom node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTvar node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTconstraintParams node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

}
