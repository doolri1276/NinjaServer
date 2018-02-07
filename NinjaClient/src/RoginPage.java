import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RoginPage extends JPanel {
	
	JTextField tf_id;
	JPasswordField tf_psw;
	
	
	public RoginPage() {
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel label=new JLabel("LOGIN");
		//label.set
		label.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		tf_id=new JTextField(20);
		tf_id.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		tf_psw=new JPasswordField(20);
		add(label);
		add(tf_id);
		add(tf_psw);
		
	
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame=new JFrame();
		frame.setTitle("NINJA GAME");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300,200);
		
		RoginPage roginPage=new RoginPage(); 
		
		frame.add(roginPage);
		//frame.pack();
		frame.setVisible(true);
	}

}
