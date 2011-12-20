import java.util.HashSet;
import java.util.Random;
import java.util.Iterator;

public class AuntSofie {

    private HashSet<String> belongings;

    public AuntSofie() {
	this.belongings = new HashSet<String>();
	this.belongings.add("Lion");
	this.belongings.add("Furcoat");
	this.belongings.add("Fancy hat");
    }
    
    public void inventory() {
	for (String item : this.belongings) {
	    System.out.println("One "+item);
	}
    }
    
    public boolean hasItems() {
	return (belongings.size() > 0);
    }

    public String steal(Thief t) {
	if (new Random().nextBoolean()) {
	    System.out.println("AuntSofie hits "+t.getName()+" in the head");
	    return null;
	} else {
	    int randomElementPos = new Random().nextInt(belongings.size());
	    Iterator<String> it = belongings.iterator();
	    String item = null;
	    for(int i = 0; i<=randomElementPos; i++) {
		item = it.next();
	    }
	    if (item != null) belongings.remove(item);
	    return item;
	}
    }
}
