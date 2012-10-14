import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
/** a chat client class that includes graphics 
 * this class communicates with the user through a graphical display
 * upon running the program the user is prompted to enter the 
 * IP address of the server
 * once the connection is established, the user enters a screen name
 * after the submission of the screen name, the client is able to send 
 * messages to all other clients that are logged in at the moment
 */
public class ChatClient {

    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("Chatter");
    JTextField textField; 
    JTextPane messageArea; 
    JTextArea panel;
    StyledDocument doc;


    /**
     *  Constructor for the client
     *  also sets up the initial GUI
     */
    public ChatClient() {
        JPanel mainPanel = (JPanel) frame.getContentPane();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				getChat(), getPictures());
		
		splitPane.setDividerLocation(400);
		splitPane.setResizeWeight(0.5);
		mainPanel.add(splitPane);
		
		frame.setSize(600,500);
		frame.setTitle("Group chat");
		frame.setVisible(true);
		
        /**
         *  Add action listeners to detect the entry of the clients 
         */
        textField.addActionListener(new ActionListener() {
             /**
              *  takes in the entry in the textfield and clears it for the next entry
              */
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

    /**
     * Method to get the address of the server from the user
     */
    private String getServerAddress() {
        return  JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:",
            "Welcome to the Chatter",
           JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Method to get the screen name of the user
     */
    private String getName() {
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }
    
   

    /**
     * makes the connection to the server
     * enters the loop that executes the communication
     * 
     */
    public void run() throws IOException, BadLocationException, UnknownHostException, SocketException {
    	
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 9001);
        in = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        

        /**
         *  infinite loop that processes all the incoming input from the server
         */
        while (true) {
            String line = in.readLine();
            doc = messageArea.getStyledDocument();
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
                
            }
            else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                 
                 doc.insertString(doc.getLength(), line.substring(8)+ "\n", new SimpleAttributeSet());
            }
            else if(line.startsWith("NAMELIST")){
            	panel.setText("Members are: \n" + line.substring(8) + "\n");
            	
            }
            else if(line.startsWith("JOIN")){
            	doc.insertString(doc.getLength(), "\n" +line.substring(5)+ "\n", new SimpleAttributeSet());
            	StyledDocument doc = (StyledDocument)messageArea.getDocument();

               
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setIcon(style, getJoinImage());


                doc.insertString(doc.getLength(), "ignored text", style);
                doc.insertString(doc.getLength(), "\n", new SimpleAttributeSet());
            }
            else if(line.startsWith("LEFT")){
            	doc.insertString(doc.getLength(), "\n" +line.substring(5)+ "\n", new SimpleAttributeSet());
            	StyledDocument doc = (StyledDocument)messageArea.getDocument();

               
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setIcon(style, getLeaveImage());

               
                doc.insertString(doc.getLength(), "ignored text", style);
                doc.insertString(doc.getLength(), "\n", new SimpleAttributeSet());
            }
        }
    }

    /**
     * Method to set the image when a client leaves the chat room
     * @return
     */
    private ImageIcon getLeaveImage(){
    	URL url;
    	ImageIcon waiting = null;
    	try
    	{
			url = new URL("http://30.media.tumblr.com/tumblr_m207ohiK3r1rreqqao1_250.gif");
		
		 waiting = new ImageIcon(url);
		 
		 
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			  return waiting;
		
		
    }
    
    /**
     * Method to set the image when a new client joins the chat room
     * @return
     */
    private ImageIcon getJoinImage(){
    	URL url;
    	ImageIcon waiting = null;
    	
    	try
    	{
			url = new URL("http://i208.photobucket.com/albums/bb104/Brooksie_pwns/Pikachu.gif");
		
		 waiting = new ImageIcon(url);
		
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			  return waiting;
		
		
    }
    
    /**
     * method to set up the GUI that will enable the chat interaction 
     * @return
     */
    private JSplitPane getChat(){
		 messageArea = new JTextPane();
		
		messageArea.setEditable(false);
		JScrollPane scrollableMessages = new JScrollPane(messageArea);
		scrollableMessages.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			public void adjustmentValueChanged(AdjustmentEvent e){
			messageArea.select(messageArea.getHeight()+1000,0);
			}});

		Border etchedBorder = BorderFactory.createEtchedBorder();
		Border border = BorderFactory.createTitledBorder(etchedBorder, "Messages",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font("Serif", Font.BOLD, 20), Color.PINK);
		messageArea.setBorder(border);
		
	 textField = new JTextField();
		

		
		JScrollPane typeScrollableMessage = new JScrollPane(textField);
		
		
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				scrollableMessages,typeScrollableMessage);
		
		splitPane.setDividerLocation(400);
		
		return splitPane;
	}
	
    /**
     *  Method to get the pictures to set up the GUI
     * @return
     */
	private JSplitPane getPictures(){
		 panel = new JTextArea();
		panel.setBackground(Color.LIGHT_GRAY);
		
		panel.setFont(new Font("Serif", Font.BOLD, 20));
		panel.setForeground(Color.BLUE);
		panel.setLayout(new FlowLayout());
		panel.setText("Members of Chat: \n \n");
		
		
		JScrollPane scrollable = new JScrollPane(panel);
		 JPanel gifPanel = new JPanel();
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				scrollable,gifPanel);
		splitPane.setDividerLocation(300);
		addGIF(gifPanel);
		return splitPane;
	}
	
	/**
	 *  Method that will set up part of the GUI for the chat panel 
	 * @param gifPanel
	 */
    private void addGIF(JPanel gifPanel) {
    	URL url;
		try {
			url = new URL("http://chzgifs.files.wordpress.com/2011/08/funny-gifs-cute-puppy-chilling.gif");
		
		 ImageIcon waiting = new ImageIcon(url);
		 Image img = waiting.getImage();  
			Image newimg = img.getScaledInstance(80, 80,  java.awt.Image.SCALE_SMOOTH); 
		    JLabel waitingLabel = new JLabel(waiting);
		    gifPanel.setLayout(new GridLayout(0,1));
		    gifPanel.add(waitingLabel);
		}
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}