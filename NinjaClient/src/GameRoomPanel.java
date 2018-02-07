import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import itempack.ADoubleAttack;
import itempack.AHold;
import itempack.AMultiAttack;
import itempack.APowerUp;
import itempack.ASeeThroughAll;
import itempack.AttackItem;
import itempack.Item;
import itempack.PEnergy;
import itempack.PMoveUp;
import itempack.PSeeThrough;
import itempack.PShield;
import itempack.PTeleport;
import itempack.PassiveItem;

public class GameRoomPanel extends JPanel{
	private String status;
	private String roomNum;
	private String roomTitle;
	private ArrayList<User> playerList;
	private User roomOwner;
	private User me; User opponent;
	
	
	
	private Main_Client main_Client;
	private DataOutputStream dos;
	
	private JTextArea roomChat;
	private JTextField tf_chat;
	private JButton btn_send;
	private JTextArea onlineMembers;
	private JButton btn_waiting;
	private JButton btn_ready;
	
	
	private DrawingThread drawingThread;
	
	
	private int height,width;
	
	private Image imgBackground;
	
	private Image myImg;
	private int myX,myY;//내 이미지의 좌표
	private int myW,myH;//내 이미지의 절반 넓이, 절반 높이
	
	private int mouseX,mouseY;
	
	private Image openedDoor;
	private Image closedDoor;
	private Image itemDoor;
	private int doorX,doorY;
	
	private Image opImage;
	private int opX,opY;
	private int opW,opH;
	
	Image strAttack;
	Image strMove;
	Image strAttackSkip;
	Image strMoveSkip;
	Image strAItems;
	Image strMItems;
	Image startImg;
	
	private Room[][] rooms;
	
	int myHp,opHp;
	
	ArrayList<AttackItem> myAttackItems;
	ArrayList<PassiveItem> myPassiveItems;
	boolean myTurn;
	boolean attackable;
	boolean movable;
	boolean attackClicking;
	boolean moveClicking;
	boolean itemDropTime;
	boolean isAlive;
	boolean startImgshow;
	boolean roomspickable;
	
	boolean isRunning;
	
	
	
	
	
	
	public void setOpponent(String id) {
		System.out.println("id를 받았다."+id+" grp 69라인");
		this.opponent = new User(id);
		System.out.println("오포낸트 설정을 완료했다. grp71라인");
		
		
		me.setOpponent(opponent);
		System.out.println("내 오포낸트로 지정");
		opponent.setOpponent(me);
		System.out.println("상대의 오포낸트를 나로 지정");
	}
	
	public void setGameStart(boolean b) {
		
		
		myTurn=b;
		isAlive=true;
		attackable=false;
		movable=false;
		
		if(myTurn) {
			requestTimer();
			
			requestItem();
		}
		
		
		//checkHP죽는지 확인하는거있어야 함...
		
		
		
	}
	
	public void pickMyPlace() {
		
	}
	
	public void itemDrop(){
		
	}
	
	public void changeMyHp(int num) {
		myHp+=num;
	}
	
	public void changeOpHp(int num) {
		opHp+=num;
	}
	
	public void startShow() {
		startImgshow=true;
		System.out.println("보이게 변경했다.");
		
		
		try {
			System.out.println("잠이든다..");
			Thread.sleep(3000);
			System.out.println("잠이 깼다!");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startImgshow=false;
	}
	
	public void requestTimer() {
		try {
			dos.writeUTF("GAME:REQUEST:TIMER");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	void requestItem() {
		try {
			dos.writeUTF("GAME:REQUEST:ITEM");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void putItem(int xi,int yi,int type) {
		
		Item item=new Item();
		switch(type) {
		case 0:item=new APowerUp();break;
		case 1:item=new AHold();break;
		case 2:item=new ADoubleAttack();break;
		case 3:item=new AMultiAttack();break;
		case 4:item=new ASeeThroughAll();break;
		case 5:item=new PShield();break;
		case 6:item=new PMoveUp();break;
		case 7:item=new PTeleport();break;
		case 8:item=new PEnergy();break;
		case 9:item=new PSeeThrough();break;	
		}
		
		rooms[xi][yi].addItem(item);
		
	}
	

	public GameRoomPanel(String roomNum, String title, User me,int width,int height,DataOutputStream dos,Main_Client main_Client) {
		
		playerList=new ArrayList<>();
		playerList.add(me);
		roomOwner=me;//이거 이따 수정 
		roomTitle=title;
		this.roomNum=roomNum;
		this.width=width;
		this.height=height;
		this.dos=dos;
		me.setCurrentLocation(roomNum);
		this.main_Client=main_Client;
		doorX=146;doorY=135;
		
		myHp=100;
		opHp=100;
		
		myTurn=true;
		attackable=true;
		movable=true;
		
		rooms=new Room[5][];
		
		for(int i=0;i<5;i++) {
			rooms[i]=new Room[5];
			for(int j=0;j<5;j++) {
				rooms[i][j]=new Room(i,j);
			}
		}
		
		myAttackItems=new ArrayList<>();
		myPassiveItems=new ArrayList<>();
		
		
		this.me=me;
		
		setLayout(new BorderLayout());
		Toolkit toolkit=Toolkit.getDefaultToolkit();
		myImg=toolkit.getImage("images/ninja_redbody.png");
		
		myW=32; myH=64;
		myImg=myImg.getScaledInstance(myW*2, myH*2, Image.SCALE_SMOOTH);
		myX=520;
		myY=410;
		
		imgBackground=toolkit.getImage("images/sky_bg.jpg");
		imgBackground=imgBackground.getScaledInstance(width, height-200, Image.SCALE_SMOOTH);
		openedDoor=toolkit.getImage("images/opened.png");
		openedDoor=openedDoor.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
		
		closedDoor=toolkit.getImage("images/closed.png");
		closedDoor=closedDoor.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
		
		opImage=toolkit.getImage("images/ninja_bluebody.png");
		opImage=opImage.getScaledInstance(64, 128, Image.SCALE_SMOOTH);
		opX=52;  opY=410;
		opW=32;  opH=64;
		
		itemDoor=toolkit.getImage("images/item.png");
		itemDoor=itemDoor.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
		
		
		strAttack=toolkit.getImage("images/strattack.png");
		strAttack=strAttack.getScaledInstance(91, 29, Image.SCALE_SMOOTH);
		
		strMove=toolkit.getImage("images/strmove.png");
		strMove=strMove.getScaledInstance(91, 29, Image.SCALE_SMOOTH);
		
		strAttackSkip=toolkit.getImage("images/skipattack.png");
		strAttackSkip=strAttackSkip.getScaledInstance(91, 29, Image.SCALE_SMOOTH);
		
		strMoveSkip=toolkit.getImage("images/skipmove.png");
		strMoveSkip=strMoveSkip.getScaledInstance(91, 29, Image.SCALE_SMOOTH);
		
		strAItems=toolkit.getImage("images/strAItems.png");
		strAItems=strAItems.getScaledInstance(42, 29, Image.SCALE_SMOOTH);
		
		strMItems=toolkit.getImage("images/strMItems.png");
		strMItems=strMItems.getScaledInstance(42, 29, Image.SCALE_SMOOTH);
		
		startImg=toolkit.getImage("images/start.png");
		
		
		JPanel chatPlace=new JPanel();
		chatPlace.setLayout(new BorderLayout());
		chatPlace.setPreferredSize(new Dimension(width, 200));
	
		roomChat=new JTextArea();
		roomChat.setEditable(false);
		JScrollPane pane=new JScrollPane(roomChat);
		tf_chat=new JTextField();
		roomChat.setBackground(Color.black);
		roomChat.setForeground(Color.white);
		
		JPanel p=new JPanel();
		p.setLayout(new BorderLayout());
		p.add(pane,BorderLayout.CENTER);
		
		JPanel p1=new JPanel();
		p1.setLayout(new BorderLayout());
		p1.add(tf_chat, BorderLayout.CENTER);
		btn_send=new JButton("SEND");
		p1.add(btn_send,BorderLayout.EAST);
		
		p.add(p1,BorderLayout.SOUTH);
		
		tf_chat.setForeground(Color.WHITE);
		tf_chat.setBackground(Color.DARK_GRAY);
		btn_send.setBackground(Color.GRAY);
		btn_send.setForeground(Color.WHITE);
		
		chatPlace.add(p,BorderLayout.CENTER);
		
		onlineMembers=new JTextArea();
		
		onlineMembers.setBackground(Color.DARK_GRAY);
		onlineMembers.setForeground(Color.WHITE);
		
		onlineMembers.setText("   [현재 온라인인 멤버들]\n");
		onlineMembers.append( "-----------------------------------\n");
		onlineMembers.append("  "+me.getID()+" [방장]\n");
		
		
		
		
		
		
		ScrollPane sp=new ScrollPane();
		
		//sp.set
		
		JPanel spp=new JPanel();
		spp.setLayout(new BorderLayout());
		sp.add(onlineMembers);
		
		spp.add(sp,BorderLayout.CENTER);
		spp.setPreferredSize(new Dimension(160, 0));
		JPanel btns=new JPanel(new GridLayout(2, 0));
		
		btn_ready=new JButton("READY");
		btns.add(btn_ready);
		
		btn_ready.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String msg;
				if(me.getIsReady()) {
					btn_ready.setText("READY");
					roomChat.append("[SERVER] READY취소 하였습니다.\n");
					msg="GAME:READY:FALSE:"+me.getID();
					me.setIsReady(false);
				}else {
					msg="GAME:READY:TRUE:"+me.getID();
					roomChat.append("[SERVER] READY 하였습니다.\n");
					me.setIsReady(true);
					btn_ready.setText("READY 완료!");
				}
				try {
					dos.writeUTF(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		});
		
		
		btn_waiting=new JButton("나가기");
		btn_waiting.setLocation(50, 50);
	
		//korea.add(btn_waiting);
		btn_waiting.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
				try {
					dos.writeUTF(me.getCurrentLocation()+":EXIT");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//내가 있는 장소를 waiting으로 변경, 이걸 서버도 알아야 함.
				//더불어 패널 변경.
				me.setCurrentLocation("WAITING");
				main_Client.changePanel();
				
			}
		});
		btns.add(btn_waiting);
		spp.add(btns,BorderLayout.SOUTH);
		
		
		chatPlace.add(spp,BorderLayout.EAST);
		onlineMembers.setEditable(false);

		
		add(chatPlace,BorderLayout.SOUTH);
		
		btn_send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				roomSent();
			}
		});
		
		tf_chat.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) 	roomSent();
			}
			@Override
			public void keyPressed(KeyEvent arg0) {}
		});
		
		drawingThread=new DrawingThread();
		drawingThread.start();
		
	}

	public void roomSent() {
		if(tf_chat.getText()==null||tf_chat.getText().length()==0)return;
		
		//System.out.println(me.getID());
		String msg=roomNum+":CHAT:"+me.getID()+":"+tf_chat.getText();
		try {
			dos.writeUTF(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tf_chat.setText("");
		System.out.println(msg+"를 서버에 전송했습니다.");
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		//화면 다 지운다.
		g.clearRect(0, 0, width, height-200);
		
		//배경이미지를 그린다.
		g.drawImage(imgBackground, 0, 0, this);
		
		
		
		g.setColor(Color.WHITE);
		g.fillRect(109, 130, 356, 356);
		
		g.drawImage(closedDoor, doorX-32, doorY-32, this);
		
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				if(rooms[i][j].isOpened) {
					if(rooms[i][j].itemExist) {
						g.drawImage(itemDoor, doorX-32, doorY-32, this);
					}else {
						g.drawImage(openedDoor, doorX-32, doorY-32, this);
					}
					
					
				}else {
					g.drawImage(closedDoor, doorX-32, doorY-32, this);
					doorX+=70;
				}
				if(rooms[i][j].pickable) {
					g.setColor(Color.CYAN);
					g.fillRect(doorX-32-71, doorY-32, 66, 4);
					g.fillRect(doorX-32-71, doorY+28, 66, 4);
					g.fillRect(doorX-32-71, doorY-32, 4, 64);
					g.fillRect(doorX-32-8, doorY-32, 4, 64);
					
				}
			}
			doorX=146;
			doorY+=70;
			
		}
		doorY=170;
		
		g.drawImage(myImg, myX-myW, myY-myH, this);
		g.drawImage(opImage, opX-opW, opY-opH, this);
		
		g.setColor(Color.WHITE);
		g.fillRoundRect(13, 470, 80, 16,7, 7);
		g.fillRoundRect(480, 470, 80, 16,7, 7);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(17, 474, 72, 8);
		g.fillRect(484, 474, 72, 8);
		
		g.setColor(new Color(0x1B75BB));
		g.fillRect(17, 474, opHp-28, 8);
		
		g.setColor(new Color(0xCE2027));
		g.fillRect(484, 474, myHp-28, 8);
		
		g.setColor(new Color(0x422C15));
		g.fillRect(17, 510, 97, 35);
		g.fillRect(129, 510, 97, 35);
		g.fillRect(237, 510, 156, 35);
		g.fillRect(403, 510, 156, 35);
		
		Color c=new Color(0x975E1F);
		
		if(myTurn)	g.setColor(c);
		else g.setColor(Color.GRAY);
		
		if(attackable)g.setColor(c);
		else g.setColor(Color.GRAY);
		
		if(movable)g.setColor(c);
		else g.setColor(Color.GRAY);
		
		g.fillRect(20, 513, 91, 29);
		g.fillRect(132, 513, 91, 29);
		g.fillRect(240, 513, 150, 29);
		g.fillRect(406, 513, 150, 29);
		
		g.setColor(Color.WHITE);
		if(attackClicking) {
			g.drawImage(strAttackSkip, 20, 513,this);
		}else {
			g.drawImage(strAttack, 20, 513, this);
		}
		if(moveClicking) {
			g.drawImage(strMoveSkip, 132, 513, this);
		}else {
			g.drawImage(strMove, 132, 513, this);
		}
		
		g.drawImage(strAItems, 240, 512, this);
		
		g.drawImage(strMItems, 406, 512, this);
		
		int xx=288,yy=515;
		g.setColor(new Color(0xBE954F));
		for(int i=0;i<3;i++) {
			g.fillRect(xx, yy, 25, 25);
			xx+=35;
		}
		
		xx=454;
		for(int i=0;i<3;i++) {
			g.fillRect(xx, yy, 25, 25);
			xx+=35;
		}
		
		if(startImgshow) {
			g.drawImage(startImg, 60, 180,this);
			System.out.println("스타트 이미지 그렷다!!");
		}
		
		
		
	}
	
	class DrawingThread extends Thread{
		@Override
		public void run() {
			isRunning=true;
			while(isRunning) {
				try {
					
					//할일하고!
					repaint();
					System.out.println("그렸다.");
					Thread.sleep(100);
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public JTextField getTf_chat() {	return tf_chat;}
	public JTextArea getRoomChat() {	return roomChat;}
	public JButton getBtn_ready() {	return btn_ready;}
	public Room[][] getRooms() {	return rooms;}
	
	
}
