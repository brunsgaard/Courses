package stack;
import java.util.EmptyStackException;

public class LinkedListStack<TypeOfValue> implements IStack<TypeOfValue> {

    private LinkedList<TypeOfValue> stack;

    public LinkedListStack() {
	this.stack = new LinkedList<TypeOfValue>();
    }

    @Override
    public boolean empty() {
	return stack.isEmpty();
    }

    @Override
    public void push(TypeOfValue value) {
	stack.insertItem(value);

    }

    @Override
    public TypeOfValue pop() throws EmptyStackException {
	TypeOfValue value = stack.getItem();
	stack.deleteItem();
	return value;
    }

    @Override
    public TypeOfValue peek() throws EmptyStackException {
	return stack.getItem();

    }

}
