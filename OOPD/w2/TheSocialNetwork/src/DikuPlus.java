import java.util.HashMap;

public class DikuPlus {
    /**
     * HashMap is used because it does not allow duplicates
     * and allows to reference elements by a string key. 
     */
    private HashMap<String, Person> network;

    public DikuPlus() {
	network = new HashMap<String, Person>();
    }

    public void addPerson(Person person) {
	network.put(person.getName(), person);
    }

    public void removePersonWithName(String name) {
	network.remove(name);
    }
}
