/**
 * 
 */
package fr.n7.stl.block.ast.instruction;


import fr.n7.stl.block.ast.Block;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for a conditional instruction.
 * @author Marc Pantel
 *
 */
public class Conditional implements Instruction {

	protected Expression condition;
	protected Block thenBranch;
	protected Block elseBranch;

	public Conditional(Expression _condition, Block _then, Block _else) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = _else;
	}

	public Conditional(Expression _condition, Block _then) {
		this.condition = _condition;
		this.thenBranch = _then;
		this.elseBranch = null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "if (" + this.condition + " )" + this.thenBranch + ((this.elseBranch != null)?(" else " + this.elseBranch):"");
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#collect(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean collectAndBackwardResolve(HierarchicalScope<Declaration> _scope) {
		return condition.collectAndBackwardResolve(_scope) && thenBranch.collect(_scope) && elseBranch.collect(_scope);
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean fullResolve(HierarchicalScope<Declaration> _scope) {
		return condition.fullResolve(_scope) && thenBranch.resolve(_scope) && elseBranch.resolve(_scope);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
		boolean _result = condition.getType().compatibleWith(AtomicType.BooleanType);
		if (_result) {
			_result = thenBranch.checkType();
			if (elseBranch != null) {
				_result = _result && elseBranch.checkType();
			}
		}
		return _result;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register _register, int _offset) {
		this.thenBranch.allocateMemory(_register, _offset);
		if (this.elseBranch != null) {
			this.elseBranch.allocateMemory(_register, _offset);
		}
		return _offset;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory _factory) {
		
		Fragment _result = _factory.createFragment();
		int _label = _factory.createLabelNumber();
		_result.append(this.condition.getCode(_factory));
		if (this.elseBranch != null) {
			_result.add(_factory.createJumpIf("else" + _label, 0));
			_result.append(this.thenBranch.getCode(_factory));
			_result.add(_factory.createJump("endif" + _label));
			_result.addSuffix("else" + _label + ":");
			_result.append(this.elseBranch.getCode(_factory));
			_result.addSuffix("endif" + _label + ":");
		} else {
			_result.add(_factory.createJumpIf("endif" + _label, 0));
			_result.append(this.thenBranch.getCode(_factory));
			_result.addSuffix("endif" + _label + ":");
		}
		return _result;
	}

}
