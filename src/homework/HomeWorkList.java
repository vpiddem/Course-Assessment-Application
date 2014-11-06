package homework;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import beans.LoggedInUser;

public class HomeWorkList {

	public HomeWorkList() {

	JFrame frame = new JFrame("Gradience");
	JScrollPane pane = new JScrollPane(
			JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	frame.setLayout(new BorderLayout());
	frame.add(pane);
//	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	JPanel panel=new HomeWorkListPane(LoggedInUser.studentId); 
	JScrollPane scroller = new JScrollPane(panel);  
	frame.getContentPane().add(scroller, BorderLayout.CENTER); 

	//frame.add(panel);
	frame.pack();
	frame.setLocationRelativeTo(null);
	frame.setSize(600, 800);
	frame.setResizable(true);
	frame.setVisible(true);
}
}