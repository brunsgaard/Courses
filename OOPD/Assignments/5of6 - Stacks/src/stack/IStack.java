package stack;
import java.util.EmptyStackException;

/**
* A last-in-first-out collection.
*/
public interface IStack<TypeOfValue>
{
    public boolean empty();
    public void push(TypeOfValue value);
    public TypeOfValue pop() throws EmptyStackException;
    public TypeOfValue peek() throws EmptyStackException;
}
