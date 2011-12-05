
public class Cow {
    private String name;
    private String breed;
    public Cow(){
	name = "";
	breed = "";
    }
    public Cow(String name_, String breed_){
	name = name_;
	breed = breed_;
    }
    public void setName(String n){
	name = n;
    }
    public String getName() {
	return name;
    }
    public void setBreed(String b){
	breed = b;
    }
    public String getBreed() {
	return breed;
    }
}
