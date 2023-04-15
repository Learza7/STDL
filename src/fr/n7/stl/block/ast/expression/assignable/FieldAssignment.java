/**
 * 
 */
package fr.n7.stl.block.ast.expression.assignable;

import fr.n7.stl.block.ast.expression.AbstractField;
import fr.n7.stl.block.ast.expression.BinaryOperator;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Abstract Syntax Tree node for an expression whose computation assigns a field in a record.
 * @author Marc Pantel
 *
 */
public class FieldAssignment extends AbstractField implements AssignableExpression {

	/**
	 * Construction for the implementation of a record field assignment expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field assignment expression.
	 * @param _name Name of the field in the record field assignment expression.
	 */
	public FieldAssignment(AssignableExpression _record, String _name) {
		super(_record, _name);
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.impl.FieldAccessImpl#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
int offset = 0;
		for (FieldDeclaration fieldDeclaration: recordType.getFields()) {
			if (fieldDeclaration.getName().equals(name)) {
				break;
			}
			offset += fieldDeclaration.getType().length();
		}
		
		Fragment fragment = _factory.createFragment();
		fragment.append(this.record.getCode(_factory));
		if (this.field.getType() instanceof NamedType) {
			fragment = _factory.createFragment();
			fragment.add(_factory.createLoadL(VariableDeclaration.namedTypeOffset));
        	fragment.add(_factory.createLoadL(offset));
			fragment.add(TAMFactory.createBinaryOperator(BinaryOperator.Add));
		} else {
			fragment.add(_factory.createLoadL(offset));
			fragment.add(TAMFactory.createBinaryOperator(BinaryOperator.Add));
        	fragment.add(_factory.createStoreI(field.getType().length()));
		}
		Logger.log("FieldAssignment: " + name + " assigned successfully");
		return fragment;


	}
	
}
