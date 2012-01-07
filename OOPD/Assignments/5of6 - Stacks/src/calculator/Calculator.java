package calculator;
import java.util.EmptyStackException;
import stack.IStack;
import stack.LinkedListStack;

public class Calculator {

    public int evaluate(IStack<Token> tokens) throws EmptyStackException {
	LinkedListStack<Integer> rpnStack = new LinkedListStack<Integer>();
	while (!tokens.empty()) {
	    tokens.pop().evaluate(rpnStack);
	}
	return rpnStack.peek();
    }
}
