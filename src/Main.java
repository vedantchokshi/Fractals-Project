import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;

import com.sun.org.apache.xml.internal.security.Init;

/**
 * Runs the GUI
 * @author Vedant Chokshi
 */
public class Main {
	
	public static void main(String[] args) {		
		SwingUtilities.invokeLater (new Runnable () {
			public void run() {
				//Creates a new instance of DisplayFrame
				DisplayFrame window = new DisplayFrame("Fractals");
				try {
					//Sets look and feel of the JFrame
					javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					//Calls the init() method which fills the JFrame with elements
					window.init();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					window.init();
				}				
			}
		});			
	}
}

