import java.util.ArrayList;

public class DikuPlus {

    private ArrayList<Person> network;

    public DikuPlus() {
	network = new ArrayList<Person>();
    }

    public void addPerson(Person person) {
	network.add(person);
    }
    
    public void  removePersonWithName(String name){
	// I have to find a way to search the array and remove from the array
    }
}
