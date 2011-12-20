public class Cowsay {
    public static void main(String[] args) {
	String message = "";
	for (int i = 0; i < args.length; i++) {
	    message += args[i] + " ";
	}
	message = message.trim();
	Cowsay cow = new Cowsay();
	cow.display(message);
    }

    public void display(String message) {
	if (message.length() > 76) {
	    multiLine(message);
	} else {
	    singleLine(message);
	}

	System.out.println("        \\   ^__^");
	System.out.println("         \\  (xx)\\_______");
	System.out.println("            (__)\\       )\\/\\");
	System.out.println("             U  ||----w |");
	System.out.println("                ||     ||");
    }

    private void singleLine(String message) {
	int messageLength = message.length();
	System.out.println(" " + getLine('_', messageLength + 2));
	System.out.println("< " + message + " >");
	System.out.println(" " + getLine('-', messageLength + 2));
    }

    private void multiLine(String message) {
	int messageLength = message.length();
	System.out.println(" " + getLine('_', 78));
	for (int pos = 0; pos < messageLength; pos += 76) {
	    int cut = pos + 76;
	    if (cut > messageLength)
		cut = messageLength;
	    if (pos == 0) {
		System.out.println("/ " + message.substring(pos, cut) + " \\");
	    } else if (cut == messageLength) {
		System.out.println("\\ " + message.substring(pos, cut)
			+ getLine(' ', 76 - (cut - pos)) + " /");
	    } else {
		System.out.println("| " + message.substring(pos, cut) + " |");
	    }
	}
	System.out.println(" " + getLine('-', 78));
    }

    private String getLine(char c, int l) {
	String s = "";
	for (int i = 0; i < l; i++) {
	    s += c;
	}
	return s;
    }
}
