import org.junit.Before;
import org.junit.Test;

public class DikuPlusTests {

    private Person dude;
    private DikuPlus network;

    @Before
    public void initializePerson() {
	dude = new Person("Dude", new Photograph(new String[] { "_O/   ",
		"  \\   ", "  /\\_ ", "  \\   " }));
    }

    @Before
    public void initializeNetwork() {
	network = new DikuPlus();
    }

    @Test
    public void addPerson() {

	network.addPerson(dude);
	network.addPerson(dude);
    }

    @Test
    public void removePerson() {

	network.removePersonWithName(dude.getName());
	network.removePersonWithName("StrangeName");
    }

}
