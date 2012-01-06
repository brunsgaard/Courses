package calculator;
import java.util.EmptyStackException;

import stack.IStack;

public class BinaryAdd implements Token
{
    @Override
    public void evaluate(IStack<Integer> stack) throws EmptyStackException
    {
        int first = stack.pop();
        int second = stack.pop();

        int result = first + second;

        stack.push(result);
    }
}
