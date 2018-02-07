import java.io.DataOutputStream;
import java.util.ArrayList;

public class GameManager {
	
	private ArrayList<User> onlineUserList;
	private ArrayList<Room> roomList;
	
	public GameManager(ArrayList<Room> roomList) {
		
		this.roomList=roomList;
		
	}
	
	public boolean createCheck(int num) {
		if(roomList.size()==4) {
			return false;
		}
		for(int i=0;i<roomList.size();i++) {
			if(num+""==roomList.get(i).getRoomNum()) return false;
		}

		return true;
	}
	
	public String createRoom(User user,String msg,DataOutputStream dos) {
		Room room=new Room(roomList.size()+1+"",user,msg,dos,this);
		roomList.add(room);
		return room.getRoomNum();//roomNum은 ROOM4형태
		
	}
	
	public void removeRoom(Room room) {
		roomList.remove(room);
	}
	
	public String checkRoomState() {
		
		System.out.println("check roomstate입장");
		String msg="WAITING:ROOMSTATE";
		
		for(int i=0;i<roomList.size();i++) {
			
			System.out.println(i+"개 쌓았음");
			System.out.println("state : "+roomList.get(i).getState());
			System.out.println("player1 : "+roomList.get(i).getPlayerList().get(0).getID());
				msg+=":"+roomList.get(i).getState()+":"+roomList.get(i).getPlayerList().get(0).getID();
				System.out.println("player2 null인가요? : " + (roomList.get(i).getPlayerList().size()==1));
				if(roomList.get(i).getPlayerList().size()==1) msg+=":NULL";
				else msg+=roomList.get(i).getPlayerList().get(1).getID();
		
		}
		
		System.out.println("처음에 다해서 null도 쌓는데로 들어옴");
		msg+=":EMPTY:NULL:NULL";
		msg+=":EMPTY:NULL:NULL";
		msg+=":EMPTY:NULL:NULL";
		msg+=":EMPTY:NULL:NULL";
		

		return msg;
	}
	
	public String exitRoom(User user,String location) {
		String exit="";
		for(int i=0;i<roomList.size();i++) {
			if(("ROOM"+roomList.get(i).getRoomNum()).equals(location)) {
				exit=roomList.get(i).exit(user);
				
				break;
			}
		}
		return exit;
		
	}
	
	public String enterRoom(User user,String location) {
		System.out.println("gm 유저 "+user+" 지역 "+location +"78라인");
		String enter="";
		for(int i=0;i<roomList.size();i++) {
			if(("ROOM"+roomList.get(i).getRoomNum()).equals(location)) {
				System.out.println("gm 같은 룸을 찾음 82라인 ");
				roomList.get(i).enter(user);
				
				enter="WAITING:"+location+":FULL:"+roomList.get(i).getPlayerList().get(0).getID()+":"+user.getID();
				System.out.println("gm enter에 해당 문구 입력됨  86라인");
				
				break;
			}
		}
		return enter;
		
		
		
	}
	
	public ArrayList<Room> getRoomList() {
		return roomList;
	}
	
	
}
