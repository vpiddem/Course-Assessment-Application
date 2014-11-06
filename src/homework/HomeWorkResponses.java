package homework;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import beans.Question;


public class HomeWorkResponses extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Container container =null;
	
	public HomeWorkResponses(final List<Question> questions) {
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
				JFrame frame = new JFrame("Home Work");
				JScrollPane pane = new JScrollPane(
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				frame.setLayout(new BorderLayout());
				frame.add(pane);
//				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JPanel panel=new ResponsesPane(questions); 
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