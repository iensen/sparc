package translating;

import java.util.ArrayList;

import parser.ASTaggregateElement;
import parser.ASTatom;
import parser.ASTchoice_element;
import parser.ASTprogramRule;
import parser.ASTterm;
import parser.ASTtermList;
import parser.ParseException;
import parser.SimpleNode;
import parser.SparcTranslatorTreeConstants;
import sorts.Condition;
import sorts.Relation;

/**
 * Class implements fetching of all conditional (constraint) atoms from given
 * program structure with respect to given sort definitions
 */
public class ConditionFetcher {

	/**
	 * Fetch conditions and clear records about them in atoms' images
	 * 
	 * @return list of conditional atoms relevant to given rule
	 */
	public ArrayList<ASTatom> fetchConditions(ASTprogramRule rule)
			throws ParseException {
		/*
		 * fetches conditional atoms from terms according to given sort
		 * definition.
		 */
		return fetchConditions(rule, true);
	}

	/**
	 * @return list of conditional atoms relevant to given aggregateElement
	 * @throws ParseException
	 *             if some atom gets incomparable elements (Ex.: p(a)>2)
	 */
	public ArrayList<ASTatom> fetchConditions(ASTaggregateElement agr)
			throws ParseException {
		return fetchConditions(agr, false);
	}

	/**
	 * @return list of conditional atoms relevant to given choice rule element
	 * @throws ParseException
	 *             if some atom gets incomparable elements (Ex.: p(a)>2)
	 */
	public ArrayList<ASTatom> fetchConditions(ASTchoice_element ch)
			throws ParseException {
		return fetchConditions(ch, false);
	}

	/**
	 * @param t1
	 *            term on the left hand side of resulting atom
	 * @param t2
	 *            term on the right hand side of resulting atom
	 * @param rel
	 *            relation between two terms
	 * @return conditional(constraint) atom
	 * @throws ParseException
	 *             if t1 and t2 are incomparable with respect to given relation
	 */
	private ASTatom CreateAtom(ASTterm t1, ASTterm t2, Relation rel)
			throws ParseException {
		/*
		 * it is impossible to compare two terms if they are of different types
		 * and relation is different from equality:
		 */
		if (rel != Relation.EQUAL
				&& rel != Relation.NOTEQUAL
				&& ((SimpleNode) (t1.jjtGetChild(0))).getId() != SparcTranslatorTreeConstants.JJTVAR
				&& ((SimpleNode) (t2.jjtGetChild(0))).getId() != SparcTranslatorTreeConstants.JJTVAR
				&& (SimpleNode) t1.jjtGetChild(0) != (SimpleNode) t2
						.jjtGetChild(0)) {
			throw new ParseException("terms at line " + t1.getBeginLine()
					+ ", column " + t1.getBeginColumn() + " and at line "
					+ t2.getBeginLine() + ", column " + t2.getBeginColumn()
					+ " are incomparable with respect to relation "
					+ rel.toString());
		}
		// create new atom
		ASTatom newAtom = new ASTatom(SparcTranslatorTreeConstants.JJTATOM);
		int newAtomNumChildren = 0;
		for (int i = 0; i < t1.jjtGetNumChildren(); i++) {
			newAtom.jjtAddChild(t1.jjtGetChild(i), newAtomNumChildren);
			newAtomNumChildren++;
		}

		for (int i = 0; i < t2.jjtGetNumChildren(); i++) {
			newAtom.jjtAddChild(t2.jjtGetChild(i), newAtomNumChildren);
			newAtomNumChildren++;
		}

		newAtom.image = rel.toString();

		return newAtom;

	}

	/**
	 * Fetch conditions and clear records about them in atoms' images
	 * 
	 * @param node
	 *            AST node to explore
	 * @param ignoreLocals
	 *            ignore conditions in aggregates and choice rules
	 * @return list of conditional atoms relevant to given rule.
	 * @throws ParseException
	 *             if some atom gets incomparable elements (Ex.: p(a)>2)
	 */

	private ArrayList<ASTatom> fetchConditions(SimpleNode node,
			boolean ignoreLocals) throws ParseException {
		ArrayList<ASTatom> result = new ArrayList<ASTatom>();
		if ((node.getId() == SparcTranslatorTreeConstants.JJTAGGREGATE || node
				.getId() == SparcTranslatorTreeConstants.JJTCHOICE_RULE)
				&& ignoreLocals) {
			return result;
		}
		if (node.getId() == SparcTranslatorTreeConstants.JJTSYMBOLICTERM) {
			SimpleNode child = (SimpleNode) node.jjtGetChild(0);
			if (child.getId() == SparcTranslatorTreeConstants.JJTSYMBOLICFUNCTION) {
				if (child.image.indexOf('{') != -1) {
					Condition cond = new Condition(child.image.substring(
							child.image.indexOf('{'),
							child.image.indexOf('}') + 1));
					child.image = child.image.substring(0,
							child.image.indexOf('{')) + '(';
					ASTtermList tlist = (ASTtermList) node.jjtGetChild(1);
					result.add(CreateAtom((ASTterm) tlist.jjtGetChild(cond
							.getFirstArgument()), (ASTterm) tlist
							.jjtGetChild(cond.getSecondArgument()), cond
							.getRelation()));

				}
			}
		}
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			result.addAll(fetchConditions((SimpleNode) (node.jjtGetChild(i)),
					ignoreLocals));
		}
		return result;
	}
}
