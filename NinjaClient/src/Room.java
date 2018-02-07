import itempack.Item;

public class Room {
	boolean isOpened;
	boolean meExist;
	boolean opExist;
	boolean itemExist;
	int Xpos;
	int Ypos;
	Item item;
	boolean pickable;
	
	
	public Room(int x,int y) {
		Xpos=x;
		Ypos=y;
	}
	
	public void addItem(Item item) {
		this.item=item;
		itemExist=true;
	}
	
	
	
}
