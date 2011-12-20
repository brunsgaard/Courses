public class Race {

    private RaceTrack track;

    public Race(RaceTrack t) {
	track = t;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	RaceTrack t = new RaceTrack(500, new Cow("joe", "persian fullblood"),
		new Cow("william", "american jersey"), new Cow("jack",
			"danish red"), new Cow("averell",
			"australian shorthorn"));
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
