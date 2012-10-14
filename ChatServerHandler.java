
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;

import javax.swing.ImageIcon;

public  class ChatServerHandler extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String namesList;
        private static HashSet<String> names = new HashSet<String>();
        private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();

      
        /**the set of all the names of the clients in the chat room
        // helps to keep screen names being used
      
        
        //the set of all the writers of the clients
        // this enables us to broadcast messages to each client  
      
        /**
         * gets the socket from the server
         */
        public ChatServerHandler(Socket socket) {
            this.socket = socket;
            namesList ="";
        }

        /**
         * run method for the thread
         * handels the main part of the communication
         */
        public void run() {
            try {

                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                /** gets the name from the client
                 * 
                 */
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            for(String name:names){
                            namesList = namesList + name +", ";
                            }
                            break;
                        }
                    }
                }
                
                /**once a name has been accepted, the communication can proceed
                 * 
                 */
                out.println("NAMEACCEPTED");
                writers.add(out);
                for (PrintWriter writer : writers) {
                	
                    writer.println("JOIN YAY! " + name + " has joined the chat! " );
                    writer.println("NAMELIST" + namesList);
                }
                
                while (true) {
                	
                	
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
                    for (PrintWriter writer : writers) {
                        writer.println("MESSAGE " + name + ": " + input);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            	/**
            	 * when the client exits, remove its name and close its socket
            	 */
                if (name != null) {
                    names.remove(name);
                    namesList = "";
                    for(String name:names){
                        namesList = namesList + name +", ";
                        }
                    
                }
                if (out != null) {
                    writers.remove(out);
                }
                for (PrintWriter writer : writers) {
                    writer.println("LEFT NOOOOO!" + name + " has left the chat!" );
                    writer.println("NAMELIST" + namesList);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                	e.printStackTrace();
                }
            }
        }
    }