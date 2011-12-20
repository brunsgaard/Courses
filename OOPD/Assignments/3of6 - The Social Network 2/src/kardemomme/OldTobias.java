import java.util.HashSet;
import java.util.Iterator;

public class OldTobias {

    public HashSet<String> belongings;

    public OldTobias() {
	this.belongings = new HashSet<String>();
	this.belongings.add("Flatscreen TV");
	this.belongings.add("Antique Lamp");
	this.belongings.add("Gay porn magazine");
    }

    public void inventory() {
	for (String item : this.belongings) {
	    System.out.println("One " + item);
	}
    }

    public static String stealFirstItem(OldTobias t) {
	Iterator<String> it = t.belongings.iterator();
	if (!it.hasNext())
	    return null;
	String item = it.next();
	t.belongings.remove(item);
	return item;
    }
}