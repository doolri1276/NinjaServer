import itempack.Item;

public class Room {
	boolean isOpened;
	boolean meExist;
	boolean opExist;
	boolean itemExist;
	boolean isPicked;
	int Xpos;
	int Ypos;
	Item item;
	boolean pickable;
	
	int x,y;
	
	
	public Room(int x,int y,int xx,int yy) {
		Xpos=x;
		Ypos=y;
		
		this.x=xx;
		this.y=yy;
	}
	
	
	
	public void addItem(Item item) {
		this.item=item;
		itemExist=true;
	}
	
	public int getXpos() {	return Xpos;}
	public int getYpos() {	return Ypos;}
	public int getX() {	return x;}
	public int getY() {	return y;}
	
	public void setItemExist(boolean itemExist) {
		this.itemExist = itemExist;
	}
	
	
	
	
	
	
	public boolean checkLocation(int mX,int mY) {
		if(mX>x&&mX<x+64&&mY>y&&mY<y+64) return true;
		return false;
	}
	
	
	
}
