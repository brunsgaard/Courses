package calculator;

import java.util.EmptyStackException;
import stack.IStack;
import stack.LinkedListStack;

public class Calculator {

    /**
     * Tokens have to be in logic order (12,2,-)
     */
    public int evaluate(IStack<Token> tokens) throws EmptyStackException {
	LinkedListStack<Integer> rpnStack = new LinkedListStack<Integer>(){};
	while (!tokens.empty()) {
	    tokens.pop().evaluate(rpnStack);
	}
	return rpnStack.peek();
    }
}
