import java.util.Random;

public class RaceTrack {
    private int length;
    private Cow[] cows;
    private int[] positions;
    private Cow winner;
    
    /**
     * 
     * @param length_
     * @param a
     * @param b
     * @param c
     * @param d
     */
    public RaceTrack(int length_, Cow a, Cow b, Cow c, Cow d) {
	setLength(length_);
	cows = new Cow[]{a,b,c,d};

//	cows = new Cow[4];
//	cows[0] = a;
//	cows[1] = b;
//	cows[2] = c;
//	cows[3] = d;
	
	positions = new int[]{0,0,0,0};
	winner = null;
    }

    public void setLength(int l) {
	length = Math.abs(l);
    }


   
    public void advance() {
	int luckyCow = new Random().nextInt(4);
	positions[luckyCow]++;
	if (positions[luckyCow] == length) {
	    winner = cows[luckyCow];
	}
    }
    
    public Cow winner() {
	return winner;
    }
}
