package calculator;

import stack.IStack;

public class Value implements Token
{
    private int value;

    public Value(int value)
    {
        this.value = value;
    }

    @Override
    public void evaluate(IStack<Integer> stack)
    {
        stack.push(this.value);
    }
}
