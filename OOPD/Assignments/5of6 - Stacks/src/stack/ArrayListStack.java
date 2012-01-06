import java.util.EmptyStackException;
import java.util.ArrayList;

public class ArrayListStack<TypeOfValue> implements IStack<TypeOfValue> {

    private ArrayList<TypeOfValue> stack;
    
    public ArrayListStack(){
	this.stack = new ArrayList<TypeOfValue>();
    }

    @Override
    public boolean empty() {
	return stack.isEmpty();
    }

    @Override
    public void push(TypeOfValue value) {
	this.stack.add(0,value);

    }

    @Override
    public TypeOfValue pop() throws EmptyStackException {
	if (this.empty() == true) throw new EmptyStackException();
	TypeOfValue value = this.stack.get(0);
	this.stack.remove(0);
	return value;
	
    }

    @Override
    public TypeOfValue peek() throws EmptyStackException {
	TypeOfValue value = this.stack.get(0);
	return value;
    }

}
