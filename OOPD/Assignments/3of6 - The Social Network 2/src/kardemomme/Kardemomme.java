import java.util.Random;

public class Kardemomme {
    public Thief[] thiefs;
    public OldTobias tobias;
    public AuntSofie sofie;
    public PolicemanBastian bastian;
    private boolean running;

    public Kardemomme() {
	this.thiefs = new Thief[] { new Thief("Kasper"), new Thief("Jesper"),
		new Thief("Johnathan") };
	this.tobias = new OldTobias();
	this.sofie = new AuntSofie();
	this.bastian = new PolicemanBastian(this);
	this.running = false;
    }

    public void simulate() {
	this.running = true;
	int day = 1;
	while (this.running) {
	    System.out.println("Day: " + day++);
	    int thiefIndex = new Random().nextInt(3);
	    visitTobias(this.thiefs[thiefIndex]);
	    if (++thiefIndex > 2) thiefIndex = 0;
	    visitSofie(this.thiefs[thiefIndex]);
	    if (++thiefIndex > 2) thiefIndex = 0;
	    visitBastian(this.thiefs[thiefIndex]);
	}
    }

    private void visitTobias(Thief thief) {
	String itemA = OldTobias.stealFirstItem(this.tobias);
	if (itemA != null) {
	    System.out.println(thief.getName() + " steals a '" + itemA
		    + "' from old tobias");
	    thief.belongings.add(itemA);
	}
    }

    private void visitSofie(Thief thief) {
	if (this.sofie.hasItems()) {
	    String itemB = this.sofie.steal(thief);
	    if (itemB != null) {
		System.out.println(thief.getName() + " steals a '" + itemB
			+ "' from aunt sofie");
		thief.belongings.add(itemB);
	    }
	}
    }

    private void visitBastian(Thief thief) {
	if (!this.bastian.hasItems()) {
	    this.running = false;
	    System.out.println("End of simulation");
	    System.out
		    .println("Thieves win since policeman bastian is out of items");
	    return;
	}
	String itemC = this.bastian.steal(thief);
	if (itemC != null) {
	    System.out.println(thief.getName() + " steals a '" + itemC
		    + "' from policeman bastian");
	    thief.belongings.add(itemC);
	} else {
	    this.running = false;
	    System.out.println("End of simulation");
	    System.out.println("");
	    System.out.println("Old tobias inventory:");
	    this.tobias.inventory();
	    System.out.println("");
	    System.out.println("Bastian inventory:");
	    this.bastian.inventory();
	    return;
	}
    }

    public static void main(String[] args) {
	Kardemomme k = new Kardemomme();
	k.simulate();
    }

}
