import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PersonTests {

    private Person dude, notEvilOleks, evilOleks;
    private Photograph photo;

    @Before
    public void initializePerson() {
	photo = new Photograph(new String[] { "_O/   ", "  \\   ", "  /\\_ ",
		"  \\   " });

	dude = new Person("Dude", photo);
	notEvilOleks = new Person("HappyMan", new Photograph(new String[] {
		"   /  ", " \\_\\  ", "    \\ ", "   /0\\" }));
	evilOleks = new Person("oleks", new Photograph(new String[] { "  \\O_",
		"/\\/  ", " /   ", " \\   " }));
    }

    @Test
    public void testName() {
	assertEquals("Dude", dude.getName());

    }

    @Test
    public void testPhoto() {
	assertEquals(photo.getHeight(), dude.getPhoto().getHeight());
	assertEquals(photo.getWidth(), dude.getPhoto().getWidth());
	for (int i = 0; i < photo.getHeight(); i++) {
	    assertEquals(photo.getLine(i), dude.getPhoto().getLine(i));
	}
    }

    @Test
    public void testSendMessageAndFriendrequst() {
	// Fails because they are not friends yet.
	assertFalse(dude.sendMessage(notEvilOleks, "Hello world!"));
	// Cannot friends self
	assertFalse(dude.requestFriendship(dude));
	assertTrue(dude.requestFriendship(notEvilOleks));
	// re-requesting is okay
	assertTrue(dude.requestFriendship(notEvilOleks));
	// Oh no.. its evilOleks.. FAIL the request
	assertFalse(evilOleks.requestFriendship(notEvilOleks));
	// sendMessage success
	assertTrue(dude.sendMessage(notEvilOleks, "Hello world!"));
    }

    @Test
    public void testEquals() {
	Person cloneDude = new Person("Dude", new Photograph(null));
	Person cloneLittleDude = new Person("dude", new Photograph(null));
	assertTrue(dude.equals(cloneDude));
	assertFalse(dude.equals(cloneLittleDude));
    }

}
