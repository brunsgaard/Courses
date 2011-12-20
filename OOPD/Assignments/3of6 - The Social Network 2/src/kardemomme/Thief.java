import java.util.HashSet;

public class Thief {
    private String name;
    
    /**
     * Public to allow our PolicemanBastian full access
     */
    public HashSet<String> belongings;
    
    public Thief(String name) {
	this.name = name;
	this.belongings = new HashSet<String>();
    }
    
    public String getName() {
	return this.name;
    }
}
