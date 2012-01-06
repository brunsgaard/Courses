package calculator;
import java.util.EmptyStackException;

import stack.IStack;


public class TrinaryAdd implements Token
{
    @Override
    public void evaluate(IStack<Integer> stack) throws EmptyStackException
    {
        int first = stack.pop();
        int second = stack.pop();
        int third = stack.pop();

        int result = first + second + third;

        stack.push(result);
    }
}
