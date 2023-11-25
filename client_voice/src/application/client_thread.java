package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.sound.sampled.SourceDataLine;

public class client_thread extends Thread {
	 public DatagramSocket socket;
	    public SourceDataLine audio_out;
	    byte buffer_receive[] = new byte[11000];
	    @Override
	    public void run(){
	    	System.out.println("Nhan am thanh tu phia server");
	        while(client_form.Calling){
	            try {
	                DatagramPacket data = new DatagramPacket(buffer_receive, buffer_receive.length);
	                socket.receive(data);
	                buffer_receive = data.getData();
	                // audio_out.write(buffer_receive, 0, buffer_receive.length);
	                InetAddress clientAddress = data.getAddress();
	                System.out.println("From  server: " + clientAddress);
	                } catch (IOException ex) {
	               
	            }
	            
	            
	        }
	        
	        audio_out.close();
	        audio_out.drain();
	        System.out.print("stop");
	               
	    }
   
}