package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class clientFormController {
	public TargetDataLine audio_in;
	public SourceDataLine audio_out;
	public DatagramSocket socket;
	public DatagramPacket packet;
	byte buffer[] = new byte[11000];
	public String ip_server = "10.10.59.172";
	public int port = 9999;
	public void out_audio() {
	    try {
	        // Khởi tạo định dạng âm thanh
	        AudioFormat format = new AudioFormat(44100.0F,16,2,true,false);

	            // Tạo và cấu hình dòng đầu ra âm thanh (SourceDataLine)
	            DataLine.Info outInfo = new DataLine.Info(SourceDataLine.class, format);
	            if (!AudioSystem.isLineSupported(outInfo)) {
	                System.out.println("Không hỗ trợ định dạng âm thanh đầu ra này");
	                return;
	            }
	                audio_out = (SourceDataLine) AudioSystem.getLine(outInfo) ;
	                audio_out.open(format);
	                audio_out.start();

	               client_thread thr = new client_thread();
				   client_form.Calling = true;
	               thr.socket = new DatagramSocket(port);
	               thr.audio_out = audio_out;
	               thr.start();
	              speaker.setVisible(false);
	    } catch (Exception ex) {
	      
	    }
	   }
	   
     Thread client_send =  new Thread( ()-> {
    	 
    	 try { 
    		 // Khởi tạo định dạng âm thanh
	        AudioFormat format = new AudioFormat(44100.0F,16,2,true,false);
    	// Tạo thông tin cho dòng đầu vào âm thanh (TargetDataLine)
	        DataLine.Info infor = new DataLine.Info(TargetDataLine.class, format);

	        // Kiểm tra xem hệ thống có hỗ trợ định dạng âm thanh được chỉ định hay không
	        if (!AudioSystem.isLineSupported(infor)) {
	            System.out.println("Không hỗ trợ định dạng âm thanh này");
	            return;
	        }
	            
	        // Tạo và cấu hình dòng đầu vào âm thanh (TargetDataLine)
	           
					audio_in = (TargetDataLine) AudioSystem.getLine(infor);
					audio_in.open(format);
					audio_in.start();
					System.out.print("Gửi âm thanh đến server");
					while(true) {
					//đọc dữ liệu âm thanh để lưu vào buffer
					audio_in.read(buffer, 0, buffer.length);
					socket = new DatagramSocket();
					//đối tượng datagrampacke t để gửi âm thanh
					InetAddress ip = InetAddress.getByName(ip_server);
					DatagramPacket data = new DatagramPacket(buffer, buffer.length,ip,port);
					socket.send(data);
					}
					
				} catch (LineUnavailableException | UnknownHostException | SocketException e) {} catch (IOException e) {
					
					e.printStackTrace();
				} 
				
		    	 socket.close();
		         audio_in.stop();
		         audio_in.close();

		
     });
     @FXML
     private Button mic;

     @FXML
     private Button speaker;

     @FXML
     void btn_mic(ActionEvent event) {
    	 client_send.start();
    	 mic.setVisible(false);
     }

     @FXML
     void btn_speaker(ActionEvent event) {
    	 out_audio();
     }

}