import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MessageTests {

    private Person person;

    @Before
    public void initializePerson() {
	person = new Person("RandomGuy", new Photograph(new String[] {
		"  \\O_", "/\\/  ", " /   ", " \\   " }));
    }

    @Test
    public void testMessageConstruct() {
	Message testMessage = new Message(person, "string");
	assertEquals(testMessage.getAuthor(),person);
	assertEquals(testMessage.getMessage(),"string");
    }

}
