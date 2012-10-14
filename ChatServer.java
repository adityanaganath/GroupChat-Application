import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Main class for handling the server-side of the chat program
 * allows clients to establish connections
 * first asks for a screen name and keeps on asking until 
 * it receives a unique screen name
 *  waits for connections on port number 9001 which is kept constant 
 */

public class ChatServer {

    /**set the port number
     * 
     */
    private static final int PORT = 9001;

    /**
     * the main method keeps on waiting for connections
     * once a connection is established, a thread is started to handle 
     * the communication
     */
    public static void main(String[] args) throws Exception {
    	
    	ServerSocket listener = null;
    	try{
    	listener = new ServerSocket(PORT);
    	}
    	catch(Exception e){
    		 System.out.println("ERROR! Try again");
    		 System.exit(0);
    	}
        System.out.println("The chat server is running.");
       
       
        
       try{
    	   
            while (true) {
                new ChatServerHandler(listener.accept()).start();
            }
        } 
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally {
            listener.close();
        }
    }

   
}