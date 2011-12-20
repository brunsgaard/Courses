
import java.util.HashSet;
import java.util.Random;
import java.util.Iterator;

public class PolicemanBastian {

    private HashSet<String> belongings;
    private Kardemomme town;

    public PolicemanBastian(Kardemomme town) {
	this.town = town;
	this.belongings = new HashSet<String>();
	this.belongings.add("Railgun");
	this.belongings.add("Handcuffs");
	this.belongings.add("Badge");
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
	    System.out.println("Policeman bastian catches "+t.getName());
	    int i = 0;
	    for (Thief thief : town.thiefs) {
		for (String item : thief.belongings) {
		    if (++i % 2 == 0) {
			town.tobias.belongings.add(item);
		    } else {
			this.belongings.add(item);
		    }
		}
	    }
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
