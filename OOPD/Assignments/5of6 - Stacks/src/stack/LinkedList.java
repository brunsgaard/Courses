package stack;
class LinkedList<TypeOfValue> {

    private Link<TypeOfValue> firstLink;

    public LinkedList() {
	firstLink = null;
    }

    public TypeOfValue getItem() {
	return firstLink.getValue();
    }

    public boolean isEmpty() {
	return firstLink == null;
    }

    public void insertItem(TypeOfValue value) {
	Link<TypeOfValue> link = new Link<TypeOfValue>(value);
	link.setNextLink(firstLink);
	firstLink = link;
    }

    public void deleteItem() {
	firstLink = firstLink.getNextLink();
    }

    private class Link<TypeOfData> {
	private TypeOfData value;
	private Link<TypeOfData> nextLink;

	public Link(TypeOfData value) {
	    this.value = value;
	}

	public TypeOfData getValue() {
	    return value;
	}

	public Link<TypeOfData> getNextLink() {
	    return nextLink;
	}

	public void setNextLink(Link<TypeOfData> nextLink) {
	    this.nextLink = nextLink;
	}

    }

}
