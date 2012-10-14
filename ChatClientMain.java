import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.lang.model.element.UnknownElementException;
import javax.swing.JFrame;
import javax.swing.text.BadLocationException;

/**
 *  main class for handling the client-side of the communication
 * @author adityanaganath
 *
 */
public class ChatClientMain {

	public static void main(String[] args) throws UnknownElementException, UnknownHostException, IOException, BadLocationException {
       /**
        * create a new client and call the run method of the client class 
        */
	   try
	   {
    	ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
       }
       catch(UnknownElementException e)
       {
    	   System.out.println("the connection to the server could not be established \n please enter a valid server location");
       }
       catch(SocketException e)
       {
    	   System.out.println("the connection to the server could not be established \n please enter a valid server location");
       }
       catch(Exception e)
       {
    	  e.printStackTrace();
       }
    }
}