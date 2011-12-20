import java.util.ArrayList;
import java.util.LinkedList;

public class Person {

    private String name;
    private Photograph photo;
    private ArrayList<Person> friendList;
    private ArrayList<Person> enemyList;

    /**
     * LinkedLists are used cause it is faster for adding elements (constant
     * time)
     */
    private LinkedList<Message> messageList;

    public Person(String inputName, Photograph inputPhoto) {
	name = inputName;
	photo = inputPhoto;
	friendList = new ArrayList<Person>();
	enemyList = new ArrayList<Person>();
	messageList = new LinkedList<Message>();
    }

    public String getName() {
	return name;
    }

    public Photograph getPhoto() {
	return photo;
    }

    public boolean sendMessage(Person receiver, String message) {
	return receiver.receiveMessage(new Message(this, message));
    }

    public boolean requestFriendship(Person otherPerson) {
	if (otherPerson.equals(this))
	    return false;
	if (sendMessage(otherPerson, "/friend")) {
	    if (!friendList.contains(otherPerson))
		friendList.add(otherPerson);
	    return true;
	} else {
	    enemyList.add(otherPerson);
	    return false;
	}
    }

    private boolean receiveMessage(Message message) {
	Person author = message.getAuthor();
	// Deal with friend requests; special "/friend" message
	if (message.getMessage() == "/friend") {
	    if (message.getAuthor().getName() == "oleks") {
		if (!enemyList.contains(author))
		    enemyList.add(author);
		return false;
	    }
	    if (!friendList.contains(author))
		friendList.add(author);
	    return true;
	}
	// Is the sender my enemy?
	if (enemyList.contains(author))
	    return false;

	// Early return for non-friend senders
	if (!friendList.contains(author))
	    return false;

	messageList.add(message);
	return true;
    }

    // Override Object equals
    public boolean equals(Person e) {
	return this.getName() == e.getName();
    }
}
