public class Message {

    private Person author;
    private String message;

    public Message(Person inputAuthor, String inputMessage) {
	author = inputAuthor;
	message = inputMessage;
    }

    public Person getAuthor() {
	return author;
    }

    public String getMessage() {
	return message;
    }

}
