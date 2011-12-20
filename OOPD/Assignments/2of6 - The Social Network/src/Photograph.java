public class Photograph {

    private String[] photo;
    private int width;
    private int height;

    public Photograph(String[] photograph) {
	if (photograph == null ) { 
	    photo = new String[] {};
	    width = 0;
	    height = 0;
	} else {
	    photo = photograph;
	    width = photograph[0].length();
	    height = photograph.length;
	}
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public String getLine(int index) {
	if (index < 0 || index >= this.photo.length)
	    return null;
	else { return this.photo[index];
	}
    }
}
