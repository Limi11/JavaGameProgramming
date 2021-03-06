package milan.liebsch.chat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import milan.liebsch.mycomponents.TitleLabel;
import milan.liebsch.networking.CheckPortAndIPAddress;

public class ChatServer extends JFrame implements Runnable {
	
	private static final long serialVersionuID = 1L;
	private JTextArea logArea = new JTextArea(10,30);
	private JButton startButton = new JButton("start");
	private static final int PORT_NUMBER = 49688;
	private ServerSocket serverSocket;
	
	public ChatServer() {
		// own method for gui initialization
		initGUI();
		// sets the title of this frame
		setTitle("Chat Server");
		// the pack method sizes the frame so that all its contents are at or above their preferred sizes
		pack();
		// sets the location of the frame relative to a component, if null the location is set relative to the center
		setLocationRelativeTo(null);
		// we need to set the Frame visible
		setVisible(true);	
		// set default close operation to do nothing when closed
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}
	
	private void initGUI() {
		// new instance of a titleLabel we want to use
		TitleLabel titleLabel = new TitleLabel("Chat Server");
		// add our new titleLable to the GUI , use the BorderLayout
		add(titleLabel, BorderLayout.PAGE_START);
		// JPanel is a standard container for GUI elements
		JPanel mainPanel = new JPanel();
		// a BoxLayout is a layout that allows elements either to be in a horizontal or vertical row
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		// add the main panel to the borderlayout in the center
		add(mainPanel, BorderLayout.CENTER);
		// set the JTextArea not editable
		logArea.setEditable(true);
		// initialize a new scrollPane container for the logArea
		JScrollPane scrollPane = new JScrollPane(logArea);
		// add the scrollPane container to the mainPanel
		mainPanel.add(scrollPane);
		
		// why we need this caret stuff ?????
		//DefaultCaret caret = (DefaultCaret)logArea.getCaret();
		//caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		// button panel
		JPanel buttonPanel = new JPanel();
		// add buttonPanel to the bordered layout
		add(buttonPanel, BorderLayout.PAGE_END);
		// listen for an start button action, use anonymous class of ActionListener and overwrite the actionPerformed method
		startButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startServer();
			}
		});
		// add the startButton to the buttonPanel
		buttonPanel.add(startButton);
		// set the startButton as the default Button of the root layer 
		getRootPane().setDefaultButton(startButton);
		// listeners
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent a) {
				stop();
				System.exit(0);
			}
		});
	}
	
	private void startServer() {
		startButton.setEnabled(false);
		new Thread(this).start();
	}
	
	private void stop() {
		if(serverSocket != null && !serverSocket.isClosed()) {
			try {
				serverSocket.close();
			}
			catch(Exception e) {
				log("Program unable to close server");
				log(e.toString());
			}
		}
	}
	
	// each time ther server is started, connect to the PORT_NUMBER and then endlessly linsten for clients
	public void run() {
		log("The server is running.");
		
		try {
			serverSocket = new ServerSocket(PORT_NUMBER);
			while(true) {
				Socket socket = serverSocket.accept();
				log("There is a new connection, startet!");
			}
		}
		catch(Exception e) {
			log("Exception caught while trying to listen on port PORT_NUMBER");
			log(e.toString());
		}
		
		finally {
			try {
				if(serverSocket.isClosed() != true) {
					serverSocket.close();
				}
			}
			catch(Exception e) {
			// nothing todo	
			}
		}
	}
	
	// methd to add timestamp to log message
	public void log(String message) {
		// instantiate new date object
		Date time = new Date();
		// intsantiate ne SinmpleDateFormat object
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy,HH:mm:ss:");
		// save current time in SimpleDateFormat format in timeStamp
		String timeStamp = dateFormat.format(time);
		// create log message
		logArea.append(timeStamp + "   " + message + "\n");
	}


	public static void main(String[] args) {
		try {
			// find out the look and feel of the crossplatform
			String className = UIManager.getCrossPlatformLookAndFeelClassName();
			// set the look and feel of the crossplatform for the program gui
			UIManager.setLookAndFeel(className);
		}
		catch(Exception e) {
			// nothing todo until now
		}
		// SwingUtilities is a collection of utility methods for Swing 
		// We create an instance of an anonymous implementation of the Runnable
		// interface and passing it to invokeLater, which will put it on a queue for
		// the Event Dispatch Thread, which will invoke the Runnable  
		// we use the class Runnable to create an anonymous class and overright the run() method 
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ChatServer();
			}
		});
	}

}
