/**
 * 
 */
package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.AbstractField;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for accessing a field in a record.
 * @author Marc Pantel
 *
 */
public class FieldAccess extends AbstractField {

	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public FieldAccess(Expression _record, String _name) {
		super(_record, _name);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		int i = 0;
        int _keep = 0;
        int _removeBefore = 0;
        int _removeAfter = 0;
		
		while (i < this.recordType.getFields().size()) {
            if (this.recordType.getFields().get(i).getName().equals(name)) {
                _keep = this.recordType.getFields().get(i).getType().length();
                ++ i;
                break;
            }
            _removeBefore += this.recordType.getFields().get(i).getType().length();
            ++ i;
        }
        
        while (i < this.recordType.getFields().size()) {
            _removeAfter += this.recordType.getFields().get(i).getType().length();
            ++ i;
        }
		
		// Create a fragment to store the result
		Fragment _result = _factory.createFragment();
		
		// Generate code for the record expression
		_result.append(this.record.getCode(_factory));
		
		// Remove fields after the accessed field
		_result.add(_factory.createPop(0, _removeAfter));
		
		// Keep the accessed field
    	_result.add(_factory.createPop(_keep, _removeBefore));
    	
    	// Return the result
		return _result;


		
	}

}
