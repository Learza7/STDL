/**
 * 
 */
package fr.n7.stl.block.ast.type;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;

/**
 * Implementation of the Abstract Syntax Tree node for a function type.
 * @author Marc Pantel
 *
 */
public class FunctionType implements Type {

	private Type result;
	private List<Type> parameters;

	public FunctionType(Type _result, Iterable<Type> _parameters) {
		this.result = _result;
		this.parameters = new LinkedList<Type>();
		for (Type _type : _parameters) {
			this.parameters.add(_type);
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#equalsTo(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean equalsTo(Type _other) {
		boolean _result = false;
		if (_other instanceof FunctionType) {
			FunctionType _otherFunction = (FunctionType) _other;
			_result = this.result.equalsTo(_otherFunction.result);
			if (_result) {
				Iterator<Type> _iter = this.parameters.iterator();
				Iterator<Type> _otherIter = _otherFunction.parameters.iterator();
				while (_iter.hasNext()) {
					_result = _result && _iter.next().equalsTo(_otherIter.next());
				}
			}
		}
		return _result;

	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#compatibleWith(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public boolean compatibleWith(Type _other) {
		boolean _result = false;
		if (_other instanceof FunctionType) {
			FunctionType _otherFunction = (FunctionType) _other;
			_result = this.result.compatibleWith(_otherFunction.result);
			if (_result) {
				Iterator<Type> _iter = this.parameters.iterator();
				Iterator<Type> _otherIter = _otherFunction.parameters.iterator();
				while (_iter.hasNext()) {
					_result = _result && _iter.next().compatibleWith(_otherIter.next());
				}
			}
		}
		return _result;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#merge(fr.n7.stl.block.ast.Type)
	 */
	@Override
	public Type merge(Type _other) {
		Type _result = AtomicType.ErrorType;
		if (_other instanceof FunctionType) {
			FunctionType _otherFunction = (FunctionType) _other;
			if (this.result.equalsTo(_otherFunction.result)) {
				_result = this;
			}
		}
		return _result;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Type#length(int)
	 */
	@Override
	public int length() {
		return this.result.length();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = "(";
		Iterator<Type> _iter = this.parameters.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
			while (_iter.hasNext()) {
				_result += " ," + _iter.next();
			}
		}
		return _result + ") -> " + this.result;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.type.Type#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean _result = this.result.resolve(_scope);
		for (Type _type : this.parameters) {
			_result = _result && _type.resolve(_scope);
		}
		return _result;
	}

}
