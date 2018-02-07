import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class Room {
	
	String roomTitle;
	ArrayList<User> playerList;
	User roomOwner;
	
	String roomNum;//숫자로 되어있다.1~4로..
	String state;
	
	DataOutputStream dos;
	GameManager gameManager;
	
	final static int POWERUP=0;
	final static int HOLD=1;
	final static int DOUBLEATTACK=2;
	final static int MULTIATTACK=3;
	final static int SEETHROUGHALL=4;
	final static int SHIELD=5;
	final static int MOVEUP=6;
	final static int TELEPORT=7;
	final static int ENERGY=8;
	final static int SEETHROUGH=9;

	
	public Room(String roomNum,User user,String title,DataOutputStream dos, GameManager gameManager) {
		this.roomNum=roomNum;
		playerList=new ArrayList<>();
		playerList.add(user);
		roomTitle=title;
		roomOwner=user;
		this.dos=dos;
		this.gameManager=gameManager;
		state="RWAITING";
		user.setRoom(this);
		
		
	}
	
	public String exit(User user) {
		user.opponent=null;
		user.room=null;
		roomOwner.opponent=null;
		playerList.remove(user);
		user.setCurrentLocation("WAITING");
		
		if(playerList.size()<1) {
			gameManager.removeRoom(this);
			return"WAITING:ROOM"+roomNum+":REMOVED";
		}else {
			this.roomOwner=playerList.get(0);
			return"WAITING:ROOM"+roomNum+":CHANGED:"+roomOwner.getID();
		}
		
	}
	
	public void enter(User user) {
		playerList.add(user);
		System.out.println("room playerlist추가완료 59라인");
		user.setOpponent(roomOwner);
		roomOwner.setOpponent(user);
		state="READY";
		System.out.println("room enter됨. 53라인");
		user.setRoom(this);
	}
	
	public boolean setReady() {
		for(User u:playerList) {
			if(!u.getIsReady()) {
				return false;
			}
		}
		
		return true;
	}
	
	public String getRndItem() {
		Random rnd=new Random();
		String iteminfo="GAME:ITEM:";
		
		int x=rnd.nextInt(5);
		int y=rnd.nextInt(5);
		
		int item=rnd.nextInt(10);
		iteminfo+=x+":"+y+":"+item;
		
		return iteminfo;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	public String getRoomTitle() {return roomTitle;}
	public ArrayList<User> getPlayerList() {return playerList;}
	public User getRoomOwner() {return roomOwner;}
	public String getRoomNum() {return roomNum;}
	public String getState() {return state;	}
	
	
	public void setRoomTitle(String roomTitle) {this.roomTitle = roomTitle;}
	public void setPlayerList(ArrayList<User> playerList) {	this.playerList = playerList;}
	public void setRoomOwner(User roomOwner) {	this.roomOwner = roomOwner;}
	public void setRoomNum(String roomNum) {	this.roomNum = roomNum;}

	
	
	
	
	
}
