public class Race {

    private RaceTrack track;

    public Race(RaceTrack t) {
	track = t;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	RaceTrack t = new RaceTrack(1000, 
            new Cow("Lund", "Datalogisk verdensmand"),
            new Cow("Dr. Jones", "Uber instructor"),
            new Cow("HK","Slikkemeister"), 
            new Cow("Steph","Australian PornBeast"));
	Race r = new Race(t);
	r.run();
    }

    public void run() {
	while (track.winner() == null) {
	    track.advance();
	}
	Cow luckyCow = track.winner();
	Cowsay s = new Cowsay();
	s.display("I , " + luckyCow.getName() + ", a(n) " + luckyCow.getBreed()
		+ ", won the race!");
    }
}
