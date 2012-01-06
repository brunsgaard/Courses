package calculator;
import java.util.EmptyStackException;

import stack.IStack;

public interface Token
{
    /**
     * @assume stack != null
     */
    public void evaluate(IStack<Integer> stack) throws EmptyStackException;
}
