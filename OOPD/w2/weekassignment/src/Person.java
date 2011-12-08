import java.util.ArrayList;

public class Person {

    private String name;
    private Photograph photo;
    private ArrayList<Person> friendList;
    private ArrayList<Person> enemyList;
    private ArrayList<Message> messageList;

    public Person(String inputName, Photograph inputPhoto) {
	name = inputName;
	photo = inputPhoto;
	friendList = new ArrayList<Person>();
	enemyList = new ArrayList<Person>();
	messageList = new ArrayList<Message>();
    }

    private void setName(String n) {
	name = n;
    }

    public String getName() {
	return name;
    }

    private void setPhoto(Photograph p) {
	photo = p;
    }

    public Photograph getPhoto() {
	return photo;
    }

    public boolean sendMessage(Person receiver, String message) {
	if (receiver == null) {
	    return false;
	} else if (receiver.getName() == this.getName()) {
	    Message theMessage = new Message(this, message); // This ??
	    receiveMessage(theMessage);
	    return true;
	} else {
	    receiver.sendMessage(receiver, message);
	    return true;
	}
    }

    public boolean requestFriendship(Person otherPerson) {
	if (otherPerson.getName() == "oleks") {
	    enemyList.add(otherPerson);
	    return false;
	} else {
	    friendList.add(otherPerson);
	    return true;
	}
    }

    private boolean receiveMessage(Message message) {
	messageList.add(otherPerson);
	return false;
    }
}
