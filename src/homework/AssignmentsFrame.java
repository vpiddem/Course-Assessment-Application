package homework;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AssignmentsFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Container container =null;
	private  int courseId;
	public AssignmentsFrame(int courseID) {
            this.courseId = courseID;
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
				}
				JFrame frame = new JFrame("Assignments");
				JScrollPane pane = new JScrollPane(
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				frame.setLayout(new BorderLayout());
				frame.add(pane);
//				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JPanel panel=new AssignmentPane(courseId); 
				JScrollPane scroller = new JScrollPane(panel);
				frame.getContentPane().add(scroller, BorderLayout.CENTER); 
				//frame.add(panel);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setSize(600, 400);
				frame.setResizable(true);
				frame.setVisible(true);
			}
		});
	}
}