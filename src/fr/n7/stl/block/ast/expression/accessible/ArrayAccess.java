/**
 * 
 */
package fr.n7.stl.block.ast.expression.accessible;


import fr.n7.stl.block.ast.expression.AbstractArray;
import fr.n7.stl.block.ast.expression.BinaryOperator;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the Abstract Syntax Tree node for accessing an array element.
 * @author Marc Pantel
 *
 */
public class ArrayAccess extends AbstractArray implements AccessibleExpression {

	/**
	 * Construction for the implementation of an array element access expression Abstract Syntax Tree node.
	 * @param _array Abstract Syntax Tree for the array part in an array element access expression.
	 * @param _index Abstract Syntax Tree for the index part in an array element access expression.
	 */
	public ArrayAccess(Expression _array, Expression _index) {
		super(_array,_index);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment fragment = _factory.createFragment();
		fragment.append(this.array.getCode(_factory));
		fragment.append(this.index.getCode(_factory));
		int length = this.getType().length();
		fragment.add(_factory.createLoadL(length));		
		fragment.add(TAMFactory.createBinaryOperator(BinaryOperator.Multiply));
		fragment.add(TAMFactory.createBinaryOperator(BinaryOperator.Add));
		fragment.add(_factory.createLoadI(length));
		String message = "Accessing array element at index " + this.index + " of length " + length;
		Logger.debug(message);
		return fragment;


	}

}
